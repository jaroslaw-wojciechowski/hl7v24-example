import mapper.MapperFhir;
import model.PatientHL7;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.ParserHL7;

public class MainApp {
    private final static Logger slf4jLogger = LoggerFactory.getLogger(PatientHL7.class);

    public static void main(String[] args) {
        slf4jLogger.info("HAPIv24 example");

        try {
            PatientHL7 patientHL7 = new PatientHL7();
            new ParserHL7().startSimpleExample(patientHL7);
            new MapperFhir().mapToFhir(patientHL7);
        } catch (Exception e) {
            slf4jLogger.error("Error: " + e);
        }
    }
}
