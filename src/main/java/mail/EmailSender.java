package mail;

import enums.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    private final EmailProperties properties = new EmailProperties();
    private static final Logger LOG = LogManager.getLogger(EmailSender.class);

    private Session connect(String username, String password){
        return Session.getInstance(properties.getPropValues(Protocol.SMTP), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendMessage(String username, String password, String sendTo, String text) {
        try {
            Session session = connect(username, password);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
            message.setSubject("Mail Subject");
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            LOG.error("could not send the message", e);
        }
    }
}
