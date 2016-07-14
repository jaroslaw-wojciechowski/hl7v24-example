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

import static org.junit.Assert.*;

public class ParserHL7EmptyTest {
    private static Patient patient;
    private static Message hapiMsg;

    @BeforeClass
    public static void setUpClass() throws Exception {
        String msg = "MSH|^~\\&|IPM|LSP|RAD|RGQ|20100705100137||ADT^A28|765043596|P|2.4|12478673\r" +
                "EVN|A28|20100705100131\r" +
                "PID||||||||||||||||||||||||||||||\r" +
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
    }

    @Test
    public void identifiersTest() {
        assertTrue("Identifier list should be empty", patient.getIdentifier().isEmpty());
    }

    @Test
    public void namesTest() throws Exception {
        assertTrue("Names list should be empty", patient.getName().isEmpty());
    }

    @Test
    public void telecomTest() throws Exception {
        assertTrue("Telecom list should be empty", patient.getTelecom().isEmpty());
    }

    @Test
    public void genderTest() throws Exception {
        assertFalse("Gender should be empty", patient.hasGender());
    }

    @Test
    public void dobTest() throws Exception {
        assertNull("Dob should be empty", patient.getBirthDate());
    }

    @Test
    public void deceasedTest() throws Exception {
        assertFalse("Deceased ind should be empty", patient.hasDeceasedBooleanType());
    }

    @Test
    public void addressesTest() throws Exception {
        assertTrue("Address list should be empty", patient.getAddress().isEmpty());
    }

    @Test
    public void maritalStatusTest() throws Exception {
        assertTrue("Marital status should be empty", patient.getMaritalStatus().isEmpty());
    }
}
