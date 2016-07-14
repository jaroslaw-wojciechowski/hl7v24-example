package parser;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.Parser;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ParserHL7Test {
    private static Patient patient;
    private static Message hapiMsg;

    @BeforeClass
    public static void setUpClass() throws Exception {
        String msg = "MSH|^~\\&|IPM|LSP|RAD|RGQ|20100705100137||ADT^A28|765043596|P|2.4|12478673\r" +
                "EVN|A28|20100705100131\r" +
                "PID|||111111^^^RGQ^MR~2222222222^^^NHS^NH||Kowalski^Jan^Maria^III^Mr||20110105000000|Male|||RandomStreet^128B^RandomCity^RandomState^3333||4444444444|5555555555||M|||||||||||||20160709224441|Y\r" +
                "PD1|||PracticeName^^PracticeCode|GPCode^GPSurname^GPForename^^^DR^^NATGP\r";

        HapiContext context = new DefaultHapiContext();
        context.getParserConfiguration().setValidating(false);
        Parser p = context.getPipeParser();

        try {
            hapiMsg = p.parse(msg);
        } catch (EncodingNotSupportedException e) {
            e.printStackTrace();
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        ADT_A05 adtMsg = (ADT_A05) hapiMsg;
        patient = new ParserHL7().parseHl7ToFhirObject(adtMsg);

        /*
            FhirContext ctx = FhirContext.forDstu3();
            String jsonEncoded = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
            System.out.println(jsonEncoded);
        */
    }

    @Test
    public void identifiersTest() {
        assertTrue("Wrong first identifier", patient.getIdentifier().get(0).getValue().equals("111111"));
        assertTrue("Wrong second identifier", patient.getIdentifier().get(1).getValue().equals("2222222222"));
    }

    @Test
    public void namesTest() throws Exception {

        String nameGiven = patient.getName().get(0).getGiven().get(0).toString();
        String nameSecond = patient.getName().get(0).getGiven().get(1).toString();
        String nameFamily = patient.getName().get(0).getFamily().get(0).toString();
        String namePrefix = patient.getName().get(0).getPrefix().get(0).toString();
        String nameSuffix = patient.getName().get(0).getSuffix().get(0).toString();
        assertTrue("Wrong given name", nameGiven.equals("Jan"));
        assertTrue("Wrong second name", nameSecond.equals("Maria"));
        assertTrue("Wrong family name", nameFamily.equals("Kowalski"));
        assertTrue("Wrong name prefix", namePrefix.equals("Mr"));
        assertTrue("Wrong name suffix", nameSuffix.equals("III"));
    }

    @Test
    public void telecomTest() throws Exception {
        if (patient.getTelecom().size() > 0) {
            assertTrue("Wrong 1st telecom value", patient.getTelecom().get(0).getValue().equals("4444444444"));
            assertTrue("Wrong 1st telecom system", patient.getTelecom().get(0).getSystem().toString().equals("PHONE"));
            assertTrue("Wrong 1st telecom use", patient.getTelecom().get(0).getUse().toString().equals("HOME"));
            assertTrue("Wrong 2nd telecom value", patient.getTelecom().get(1).getValue().equals("5555555555"));
            assertTrue("Wrong 2nd telecom system", patient.getTelecom().get(1).getSystem().toString().equals("PHONE"));
            assertTrue("Wrong 2nd telecom use", patient.getTelecom().get(1).getUse().toString().equals("WORK"));
        }
    }

    @Test
    public void genderTest() throws Exception {
        assertTrue("Wrong gender", patient.getGender().toString().equals("MALE"));
    }

    @Test
    public void dobTest() throws Exception {
        assertTrue("Wrong date of birth", patient.getBirthDate().getTime() == 1294182000000L);
    }

    @Test
    public void deceasedTest() throws Exception {
        assertTrue("Wrong deceased indicator", patient.getDeceasedBooleanType().getValue().equals(true));
    }

    @Test
    public void addressesTest() throws Exception {
        assertTrue("Wrong address Line1", patient.getAddress().get(0).getLine().get(0).toString().equals("RandomStreet"));
        assertTrue("Wrong address Line2", patient.getAddress().get(0).getLine().get(1).toString().equals("128B"));
        assertTrue("Wrong address City", patient.getAddress().get(0).getCity().equals("RandomCity"));
        assertTrue("Wrong address State", patient.getAddress().get(0).getState().equals("RandomState"));
        assertTrue("Wrong address PostalCode", patient.getAddress().get(0).getPostalCode().equals("3333"));
    }

    @Test
    public void maritalStatusTest() throws Exception {
        assertTrue("Wrong marital coding system", patient.getMaritalStatus().getCoding().get(0).getSystem()
                .equals("http://hl7.org/fhir/v3/MaritalStatus"));
        assertTrue("Wrong marital code", patient.getMaritalStatus().getCoding().get(0).getCode().equals("M"));
    }
}
