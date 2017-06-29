package com.enviosya.notifications.domain;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailFactory {

    public void send(String notificationMessage) {
        String emailReceiver = getEmailReceiver(notificationMessage);
        String subject = getSubject(notificationMessage);
        String message = getMessage(notificationMessage);

        Properties properties = new Properties();
        properties.put("mail.smtp.user", "");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        SecurityManager security = System.getSecurityManager();

        try {
            Authenticator gmailAuth = new GmailAUTH();
            Session session = Session.getInstance(properties, gmailAuth);
            MimeMessage mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(""));
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.addRecipient(
                    Message.RecipientType.TO, new InternetAddress(emailReceiver)
            );
            Transport.send(mailMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private String getEmailReceiver(String notificationMessage) {
        String email = null;
        int startEmailPosition = notificationMessage.indexOf("<start-email>");
        int endEmailPosition   = notificationMessage.indexOf("<end-email>");
        email = notificationMessage.substring(startEmailPosition, endEmailPosition);
        return email;
    }

    private String getSubject(String notificationMessage) {
        String subject = null;
        int startSubjectPosition = notificationMessage.indexOf("<start-subject>");
        int endSubjectPosition   = notificationMessage.indexOf("<end-subject>");
        subject = notificationMessage.substring(startSubjectPosition, endSubjectPosition);
        return subject;
    }

    private String getMessage(String notificationMessage) {
        String message = null;
        int startMessagePosition = notificationMessage.indexOf("<start-message>");
        int endMessagePosition   = notificationMessage.indexOf("<end-message>");
        message = notificationMessage.substring(startMessagePosition, endMessagePosition);
        return message;
    }
    
    private class GmailAUTH extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("", "");
        }
    }
    
}
