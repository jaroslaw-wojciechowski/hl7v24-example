import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.parser.Parser;
import org.hl7.fhir.dstu3.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.ParserHL7;

public class MainApp {
    private final static Logger slf4jLogger = LoggerFactory.getLogger(MainApp.class);
    private static Message hapiMsg;
    private static Patient patient;

    private static final String msg = "MSH|^~\\&|IPM|LSP|RAD|RGQ|20100705100137||ADT^A28|765043596|P|2.4|12478673\r" +
            "EVN|A28|20100705100131\r" +
            "PID|||111111^^^RGQ^MR~2222222222^^^NHS^NH||Kowalski^Jan^Maria^III^Mr||20110105000000|Male|||RandomStreet^128B^RandomCity^RandomState^3333||(444)444-4444|(555)555-5555||M|||||||||||||20160709224441|Y\r" +
            "PD1|||PracticeName^^9999999999|GPCode^GPSurname^GPForename^^^DR^^NATGP\r";

    public static void main(String[] args) {
        slf4jLogger.info("HAPIv24 example");
        try {
            HapiContext context = new DefaultHapiContext();
            context.getParserConfiguration().setValidating(true);
            Parser p = context.getPipeParser();

            hapiMsg = p.parse(msg);
            ADT_A05 adtMsg = (ADT_A05) hapiMsg;
            patient = new ParserHL7().parseHl7ToFhirObject(adtMsg);
            FhirContext ctx = FhirContext.forDstu3();
            String jsonEncoded = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
            System.out.println(jsonEncoded);

        } catch (Exception e) {
            slf4jLogger.error("Error: " + e);
        }
    }
}
