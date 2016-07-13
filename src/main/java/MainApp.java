import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.ParserHL7;

public class MainApp {
    private final static Logger slf4jLogger = LoggerFactory.getLogger(MainApp.class);
    private static Message hapiMsg;

    private final static String msg = "MSH|^~\\&|IPM|LSP|RAD|RGQ|20100705100137||ADT^A28|765043596|P|2.4|12478673\r" +
            "EVN|A28|20100705100131\r" +
            "PID|||111111^^^RGQ^MR~2222222222^^^NHS^NH||Kowalski^Jan^Maria^III^Mr||20110105000000|Male|||RandomStreet^128B^RandomCity^RandomState^3333||4444444444|5555555555||M|||||||||||||20160709224441|Y\r" +
            "PD1|||PracticeName^^PracticeCode|GPCode^GPSurname^GPForename^^^DR^^NATGP\r";

    public static void main(String[] args) {
        slf4jLogger.info("HAPIv24 example");
        try {
            HapiContext context = new DefaultHapiContext();
            context.getParserConfiguration().setValidating(false);
            //Parser p = context.getGenericParser();
            Parser p = context.getPipeParser();

            try {
                hapiMsg = p.parse(msg);
            } catch (EncodingNotSupportedException e) {
                e.printStackTrace();
            } catch (HL7Exception e) {
                e.printStackTrace();
            }

            ADT_A05 adtMsg = (ADT_A05) hapiMsg;
            new ParserHL7().parseHl7ToFhirObject(adtMsg);
        } catch (Exception e) {
            slf4jLogger.error("Error: " + e);
        }
    }
}
