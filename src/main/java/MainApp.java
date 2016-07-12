import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.ParserHL7;

public class MainApp {
    private final static Logger slf4jLogger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        slf4jLogger.info("HAPIv24 example");
        try {
            new ParserHL7().parseHl7ToFhirObject();
        } catch (Exception e) {
            slf4jLogger.error("Error: " + e);
        }
    }
}
