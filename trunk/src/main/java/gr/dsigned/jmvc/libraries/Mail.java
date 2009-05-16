/*
 *  Mail.java
 * 
 *  Copyright (C) 2008 Nikosk <nikosk@dsigned.gr>
 * 
 *  This module is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *  any later version. See http://www.gnu.org/licenses/lgpl.html.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Library;
import gr.dsigned.jmvc.types.Hmap;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;

/**
 * 02 ��� 2008, gr.dsigned.jmvc.libraries Mail.java
 * TODO : Handle html emails
 *        Handle attachments
 *        Handle Bcc
 *        Handle cc
 *        Create mail form (maybe)
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Mail extends Library {

    public enum RecipientType {

        TO, CC, BCC
    }
    private String smtpHost;
    private String from;
    private String to;
    private String subject;
    private String messageBody;
    private Pattern p = Pattern.compile("[0-9a-zA-Z]+(\\.{0,1}[0-9a-zA-Z\\+\\-_]+)*@[0-9a-zA-Z\\-]+(\\.{1}[a-zA-Z]{2,6})+");

    /**
     * Send the email. Example: 
     * <pre>
     *   Mail m = new Mail();
     *   m.setFrom("mail@domain.com");
     *   m.setTo("mail@mail.com");
     *   m.setMessageBody("test");
     *   m.setSubject("Test");
     *   m.send();
     * </pre>
     * @throws java.lang.Exception
     */
    public void send() throws Exception {
        if (!this.getSmtpHost().isEmpty() && !this.getTo().isEmpty()) {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", getSmtpHost());
            Session session = Session.getDefaultInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(getFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getTo(), false));
            message.setSubject(getSubject());
            message.setContent(getMessageBody(), "text/html; charset=UTF-8");
            Transport.send(message);
        } else {
            String er = "SMTP server not set or tried to send email with empty TO: address.";
            Jmvc.logError(er);
            throw new Exception(er);
        }
    }

    /**
     * Send the email. This is static so you dont need a Mail instance. Example: 
     * <pre>
     *   Hmap h = new Hmap();
     *   h.put("mail@domain.com");
     *   h.put("mail@mail.com");
     *   h.put("test");
     *   h.put("Test");
     *   Mail.send(h);
     * </pre>
     * @throws java.lang.Exception
     */
    public static void send(Hmap settings) throws Exception {
        if (Settings.get("SMTP_HOST") != null && !settings.get("to").isEmpty()) {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", settings.get("smtpHost"));
            Session session = Session.getDefaultInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(settings.get("from")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(settings.get("to"), false));
            message.setSubject(settings.get("subject"));
            message.setContent(settings.get("messageBody"), "text/html; charset=UTF-8");
            Transport.send(message);
        } else {
            String er = "SMTP server not set or tried to send email with empty TO: address.";
            Jmvc.logError(er);
            throw new Exception(er);
        }
    }

    /**
     * Sends email to only one recipient.
     * Returns true if message sent successfully, else false.
     * @param to
     * @param from
     * @param subject
     * @param body
     * @return 
     */
    public static boolean send(String to, String from, String subject, String body) {
        boolean result = false;
        if (Settings.get("SMTP_HOST") != null) {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", Settings.get("SMTP_HOST"));
            Session session = Session.getDefaultInstance(props, null);
            try {
                Message message = new MimeMessage(session);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                message.setFrom(new InternetAddress(from));
                message.setSubject(subject);
                message.setContent(body, "text/html; charset=UTF-8");
                Transport.send(message);
                result = true;
            } catch (Exception ex) {
                Jmvc.logError("Error while trying to send email to: " + to + " from: " + from + ". Exception: " + ex);
            }
        } else {
            String er = "SMTP server not set or tried to send email with empty TO: address.";
            Jmvc.logError(er);
        }
        return result;
    }

    /**
     * Send email to many recipients. Their emails are comma separated.
     * @param to
     * @param from
     * @param subject
     * @param body
     * @param adresses
     * @param type
     * @return 
     */
    public static boolean send(String to, String from, String subject, String body, String adresses, RecipientType type) {
        boolean result = false;
        if (Settings.get("SMTP_HOST") != null) {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", Settings.get("SMTP_HOST"));
            Session session = Session.getDefaultInstance(props, null);
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                ArrayList<InternetAddress> recipients = new ArrayList<InternetAddress>();
                for (String r : adresses.split(",")) {
                    recipients.add(new InternetAddress(r, true));
                }
                switch (type) {
                    case TO:
                        message.setRecipients(Message.RecipientType.TO, recipients.toArray(new InternetAddress[0]));
                        break;
                    case CC:
                        message.setRecipients(Message.RecipientType.CC, recipients.toArray(new InternetAddress[0]));
                        break;
                    case BCC:
                        message.setRecipients(Message.RecipientType.BCC, recipients.toArray(new InternetAddress[0]));
                        break;
                }
                if (type != RecipientType.TO && to != null) {
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                }
                message.setSubject(subject);
                message.setContent(body, "text/html; charset=UTF-8");
                Transport.send(message);
                result = true;
            } catch (Exception ex) {
                Jmvc.logError("Error while trying to send email to: " + to + " from: " + from + ". Exception: " + ex);
            }
        } else {
            Jmvc.logError("SMTP server not set or tried to send email with empty TO: address.");
        }
        return result;
    }

    /**
     * 
     * @param settings
     * @param adressess
     * @param type
     * @throws java.lang.Exception
     */
    public static void send(String to, String from,String subject, String body, ArrayList<String> adressess, RecipientType type) throws Exception {
        if (Settings.get("SMTP_HOST") != null) {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", Settings.get("SMTP_HOST"));
            Session session = Session.getDefaultInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            ArrayList<InternetAddress> recipients = new ArrayList<InternetAddress>();
            for (String r : adressess) {
                recipients.add(new InternetAddress(r, true));
            }
            switch (type) {
                case TO:
                    message.setRecipients(Message.RecipientType.TO, recipients.toArray(new InternetAddress[0]));
                    break;
                case CC:
                    message.setRecipients(Message.RecipientType.CC, recipients.toArray(new InternetAddress[0]));
                    break;
                case BCC:
                    message.setRecipients(Message.RecipientType.BCC, recipients.toArray(new InternetAddress[0]));
                    break;
            }
            if (type != RecipientType.TO && to != null) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            }
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=UTF-8");
            Transport.send(message);
        } else {
            String er = "SMTP server not set or tried to send email with empty TO: address.";
            Jmvc.logError(er);
            throw new Exception(er);
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    private String getSmtpHost() {
        smtpHost = Settings.get("SMTP_HOST");
        return smtpHost;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean validEmail(String email) {
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            return false;
        }
        return true;
    }
}
