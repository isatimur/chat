package ru.isa.chat.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.encoder.Encode;
import ru.isa.chat.app.ClientRegister;

/**
 *
 * @author timur.isachenko
 */
@WebServlet(urlPatterns = {"/chat"})
public class ChatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
        res.setHeader("Cache-Control", "no-cache");
        res.setHeader("Pragma", "no-cache");
        PrintWriter writer = res.getWriter();

        ClientRegister client = (ClientRegister) getServletContext().getAttribute("register");
        String uid = UUID.randomUUID().toString();
        CountDownLatch start = client.register(uid);

        try {
            String firstTime = req.getParameter("first");
            System.out.println("Thread " + Thread.currentThread().getName() + " is waiting for response");
            String histMsg = "";
            if (!"1".equals(firstTime)) {
                long time = System.currentTimeMillis();
                start.await(60, TimeUnit.SECONDS);
                long totalTime = System.currentTimeMillis() - time;
                System.out.println("Thread " + Thread.currentThread().getName() + " is realeased + time: " + totalTime);
                String message = (totalTime >= 59999) ? "" : client.getMessage(uid);
                writer.write(message != null ? message : "");

            } else {
                histMsg = "";//client.getHistory();
                writer.write(histMsg);
                writer.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (writer != null) {
                writer.flush();
            }
            writer.close();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ClientRegister client = (ClientRegister) getServletContext().getAttribute("register");

        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
        res.setHeader("Cache-Control", "no-cache");
        res.setHeader("Pragma", "no-cache");

        PrintWriter writer = res.getWriter();
        String name = req.getRemoteAddr();
        try {
            String message = req.getParameter("msg");

            message = Encode.forHtml(message);
            System.out.println("doPost: New message received:" + message);
            if (message != null && !message.isEmpty()) {
                String msg = name + ":\n" + message + "\n";
                client.putMessage(msg);
                writer.write("success");
                System.out.println("ClientRegister: " + msg);
            } else {
                res.sendError(422, "No data found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
            }
            writer.close();
        }

    }

}
