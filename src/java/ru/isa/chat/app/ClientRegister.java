/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.isa.chat.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import ru.isa.chat.db.DBManager;

/**
 *
 * @author dns2
 */
public class ClientRegister {

    private Set<String> registredThreads = new CopyOnWriteArraySet<String>();
    private volatile String message;
    private CountDownLatch cdl;
    private DBManager dbm;

    public ClientRegister() {
        cdl = new CountDownLatch(1);
    }

    public CountDownLatch register(String thread) {
        registredThreads.add(thread);
        System.out.println("The thread has been registred");
        return cdl;
    }

    public void putMessage(String message) {
        this.message = message;
        cdl.countDown();
        cdl = new CountDownLatch(1);
//        addMsgToHistory();
    }

    public String getMessage(String thread) {
        if (registredThreads.remove(thread)) {
            return message;
        } else {
            return "";
        }
    }

    public void unregister(String thread) {
        registredThreads.remove(thread);
        System.out.println("The thread has been unregistred");
    }

    public void setDBM(DBManager dBManager) {
        this.dbm = dBManager;
    }

    public String getHistory() {
        String historicalMsg = "";
        Connection conn = dbm.getConnection();
        System.out.println("Get connection for select");
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT x.message FROM (SELECT * FROM obj_load.history ORDER BY crdatetime desc) x where ROWNUM <=100 ORDER BY x.crdatetime asc");
            st.execute();
            rs = st.getResultSet();
            while (rs.next()) {
                historicalMsg += rs.getString(1);
            }

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return historicalMsg;
    }

    public void addMsgToHistory() {
        Connection conn = dbm.getConnection();
        System.out.println("Get connection for insert");
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("insert into obj_load.history (message) values (?)");
            st.setString(1, message);
            if (st.executeUpdate() == 0) {
                System.out.println("Insert didn't work");
            }

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }
}
