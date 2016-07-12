import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.ParserHL7;

public class MainApp {
    private final static Logger slf4jLogger = LoggerFactory.getLogger(MainApp.class);

    private final static String msg = "MSH|^~\\&|IPM|LSP|RAD|RGQ|20100705100137||ADT^A28|765043596|P|2.4|12478673\r" +
            "EVN|A28|20100705100131\r" +
            "PID|||111111^^^RGQ^MR~2222222222^^^NHS^NH||Kowalski^Jan^Maria^III^Mr||20110105000000|Male|||RandomStreet^128B^RandomCity^RandomState^3333||4444444444|5555555555||M|||||||||||||20160709224441|Y\r" +
            "PD1|||PracticeName^^PracticeCode|GPCode^GPSurname^GPForename^^^DR^^NATGP\r";

    public static void main(String[] args) {
        slf4jLogger.info("HAPIv24 example");
        try {
            new ParserHL7().parseHl7ToFhirObject(msg);
        } catch (Exception e) {
            slf4jLogger.error("Error: " + e);
        }
    }
}
