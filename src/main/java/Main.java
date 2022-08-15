import mail.EmailReader;
import mail.EmailSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import python.PythonHandler;

import java.io.IOException;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);


    public static void main(String[] args) {
            performTask();
    }

    private static void performTask() {
        EmailReader emailReader = new EmailReader();
        EmailSender emailSender = new EmailSender();
        PythonHandler pythonHandler = new PythonHandler();
        IOException ioException = null;
        emailReader.readMail("md7343@gmail.com", "dejzhnsvaucxivih");
        if(emailReader.getText().contains("banana")){
            try {
                String output = pythonHandler.runPythonScript("/Users/shay/mail-project/src/main/resources/main.py");
                emailSender.sendMessage("md7343@gmail.com", "dejzhnsvaucxivih", "md7343@gmail.com", output);
            } catch (IOException e) {
                LOG.error("failed to run the python file");
                ioException = e;
            }
        } else emailSender.sendMessage("md7343@gmail.com", "dejzhnsvaucxivih", "md7343@gmail.com", "Invalid keyword");
        
        if(!emailReader.isHasAttachments()) emailSender.sendMessage("md7343@gmail.com", "dejzhnsvaucxivih", "md7343@gmail.com", "Attachment missing");
        if(ioException != null) emailSender.sendMessage("md7343@gmail.com", "dejzhnsvaucxivih", "md7343@gmail.com", String.valueOf(ioException));
    }
}
