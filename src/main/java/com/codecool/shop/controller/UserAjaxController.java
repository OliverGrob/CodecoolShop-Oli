package com.codecool.shop.controller;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.ShoppingCartDaoJDBC;
import com.codecool.shop.dao.implementation.UserDaoJDBC;
import com.codecool.shop.model.User;
import com.google.gson.Gson;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet(urlPatterns = {"/handle-user"})
public class UserAjaxController extends HttpServlet {
    private SessionManager sessionManager = SessionManager.getInstance();
    private ShoppingCartDao shoppingCart = ShoppingCartDaoJDBC.getInstance();
    private UserDao userHandler = UserDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = sessionManager.getHttpSessionRedirect(req);

        if (session == null) {
            resp.sendRedirect("/");
            return;
        }

        Map<String, String> newData = new HashMap<>();

        session.setAttribute("userId", null);

        newData.put("alertColor", "success");
        newData.put("alertMessage", "You logged out successfully!");

        String json = new Gson().toJson(newData);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> newData = new HashMap<>();

        switch (req.getParameter("event")) {
            case "register":
                if (userHandler.validRegister(req.getParameter("userEmail"),
                        req.getParameter("userPassword"),
                        req.getParameter("userPasswordConfirm"))) {
                    userHandler.add(req.getParameter("userEmail"), req.getParameter("userPassword"));
                    newData.put("alertColor", "success");
                    newData.put("alertMessage", "You registered successfully!");
                } else {
                    newData.put("alertColor", "danger");
                    newData.put("alertMessage", "Email is already in use or your passwords do not match!");
                }
                shoppingCart.add(userHandler.find(req.getParameter("userEmail")).getId(), new java.sql.Date(new Date().getTime()));
//                this.sendEmail(req.getParameter("userEmail"));
                break;
            case "login":
                if (userHandler.validLogin(req.getParameter("userEmail"), req.getParameter("userPassword"))) {
                    User user = userHandler.find(req.getParameter("userEmail"));

                    req.getSession().setAttribute("userId", user.getId());

                    newData.put("userId", Integer.toString(user.getId()));
                    newData.put("userName", user.getFirstName());
                    newData.put("alertColor", "success");
                    newData.put("alertMessage", "You logged in successfully!");
                } else {
                    newData.put("userId", null);
                    newData.put("alertColor", "danger");
                    newData.put("alertMessage", "Incorrect email or password!");
                }
                break;
        }

        String json = new Gson().toJson(newData);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

    }

    private void sendEmail(String emailAddress) {
        String sentFrom = "intAirnet";
        String pass = "intAirnetSzOZD";
        String sentTo = "groboliver1117@gmail.com";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", sentFrom);
        properties.put("mail.smtp.password", pass);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sentFrom));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sentTo));

            message.setSubject("Registration");
            message.setText("You registered successfully!");

            Transport transport = session.getTransport("smtp");
            transport.connect(host, sentFrom , pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Message sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
