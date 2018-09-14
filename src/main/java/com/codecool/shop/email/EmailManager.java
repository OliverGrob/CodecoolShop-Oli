package com.codecool.shop.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailManager {

    private static EmailManager instance = null;


    public static EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
        }
        return instance;
    }

    public void sendEmail(String emailAddress) {
        // TODO --- probably needs an email account on gmail :S
//        String sentFrom = "intAirnet";
//        String pass = "intAirnetSzOZD";
//        String sentTo = "groboliver1117@gmail.com";
//        String host = "smtp.gmail.com";
//
//        Properties properties = System.getProperties();
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.user", sentFrom);
//        properties.put("mail.smtp.password", pass);
//        properties.put("mail.smtp.port", "587");
//        properties.put("mail.smtp.auth", "true");
//
//        Session session = Session.getDefaultInstance(properties);
//
//        try {
//            MimeMessage message = new MimeMessage(session);
//
//            message.setFrom(new InternetAddress(sentFrom));
//
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sentTo));
//
//            message.setSubject("Registration");
//            message.setText("You registered successfully!");
//
//            Transport transport = session.getTransport("smtp");
//            transport.connect(host, sentFrom , pass);
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//            System.out.println("Message sent successfully!");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }

}
