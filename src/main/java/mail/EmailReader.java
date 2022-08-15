package mail;

import enums.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;

public class EmailReader {

    private static final Logger LOG = LogManager.getLogger(EmailReader.class);
    private final EmailProperties properties = new EmailProperties();
    private boolean hasAttachments;
    private String text;


    public void readMail(String user, String password) {
        try {
            Store store = connectToServer(user, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] arrayMessages = inbox.getMessages();
            for (Message message : arrayMessages) {
                hasAttachments = saveAttachment(message);
                text = getMessageBodyText(message);
            }
            closeConnection(store, inbox);
        } catch (MessagingException | IOException ex) {
            LOG.error("failed to read mail", ex);
        }
    }

    public boolean isHasAttachments() {
        return hasAttachments;
    }

    public String getText() {
        return text;
    }

    private void closeConnection(Store store, Folder inbox) throws MessagingException {
        inbox.close(false);
        store.close();
    }

    private boolean saveAttachment(Message message) throws IOException, MessagingException {
        boolean hasAttachments = hasAttachments(message);
        if (!hasAttachments) return false;
        Multipart multiPart = (Multipart) message.getContent();
        for (int i = 0; i < multiPart.getCount(); i++) {
            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                String fileName = part.getFileName();
                part.saveFile(String.format("/Users/shay/mail-project/src/main/resources/%s", fileName));
            }
        }
        return true;
    }

    private Store connectToServer(String user, String password) throws MessagingException {
        Session session = Session.getDefaultInstance(properties.getPropValues(Protocol.IMAP));
        Store store = session.getStore("imaps");
        store.connect(user, password);
        return store;
    }

    private boolean hasAttachments(Message msg) throws MessagingException, IOException {
        if (msg.isMimeType("multipart/mixed")) {
            Multipart mp = (Multipart)msg.getContent();
            if (mp.getCount() > 1)
                return true;
        }
        return false;
    }

    private String getMessageBodyText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            return (String)p.getContent();
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                return getMessageBodyText(mp.getBodyPart(i));
            }
        }
        return "";
    }
}