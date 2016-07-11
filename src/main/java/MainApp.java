import mapper.MapperFhir;
import model.PatientHL7;
import parser.ParserHL7;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("HAPIv24 example");

        PatientHL7 patientHL7 = new PatientHL7();
        new ParserHL7().startSimpleExample(patientHL7);
        new MapperFhir().mapToFhir(patientHL7);
    }
}
