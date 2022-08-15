package mail;

import enums.Protocol;
import interfaces.IProperties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailProperties implements IProperties {

    @Override
    public Properties getPropValues(Protocol protocol) {

        Properties prop = new Properties();
        String propFileName = "email-config.properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            switch (protocol){
                case IMAP:
                    prop.getProperty("mail.store.protocol");
                    prop.getProperty("mail.imaps.host");
                    prop.getProperty("mail.imap.socketFactory.class");
                    prop.getProperty("mail.imap.socketFactory.fallback");
                    prop.getProperty("mail.imaps.port");
                    break;
                case SMTP:
                    prop.getProperty("mail.smtp.auth");
                    prop.getProperty("mail.smtp.starttls.enable");
                    prop.getProperty("mail.smtp.host");
                    prop.getProperty("mail.smtp.port");
                    prop.getProperty("mail.smtp.socketFactory.class");
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
