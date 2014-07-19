/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.isa.chat.app;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import ru.isa.chat.db.DBManager;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        System.out.println("ServletContext initialized");
        ClientRegister register = new ClientRegister();
        String user = ctx.getInitParameter("DBUSER");
        String pswd = ctx.getInitParameter("DBPWD");
        String url = ctx.getInitParameter("DBURL");
        register.setDBM(new DBManager(url, user, pswd));
        ctx.setAttribute("register", register);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
