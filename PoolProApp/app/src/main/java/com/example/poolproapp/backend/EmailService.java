package com.example.poolproapp.backend;

import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {

    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        // Sender's email and password
        String from = "poolproapp@gmail.com";
        String password = "vieo xnhh wyqk yvsk"; // Make sure to keep this secure

        // SMTP server details
        String host = "smtp.gmail.com";
        int port = 587;

        // Set up email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        // Create a MimeMessage object
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        // Send the message
        Transport.send(message);
    }



}
