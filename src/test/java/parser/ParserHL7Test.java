package parser;

import org.hl7.fhir.dstu3.model.Patient;

import static org.junit.Assert.assertTrue;

public class ParserHL7Test {
    Patient patient;

    @org.junit.Before
    public void setUp() throws Exception {
        String msg = "MSH|^~\\&|IPM|LSP|RAD|RGQ|20100705100137||ADT^A28|765043596|P|2.4|12478673\r" +
                "EVN|A28|20100705100131\r" +
                "PID|||111111^^^RGQ^MR~2222222222^^^NHS^NH||Kowalski^Jan^Maria^III^Mr||20110105000000|Male|||RandomStreet^128B^RandomCity^RandomState^3333||4444444444|5555555555||M|||||||||||||20160709224441|Y\r" +
                "PD1|||PracticeName^^PracticeCode|GPCode^GPSurname^GPForename^^^DR^^NATGP\r";

        patient = new ParserHL7().parseHl7ToFhirObject(msg);
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void identifiersTest() {
        assertTrue("Wrong first identifier", patient.getIdentifier().get(0).getValue().equals("111111"));
        assertTrue("Wrong second identifier", patient.getIdentifier().get(1).getValue().equals("2222222222"));
    }

    @org.junit.Test
    public void namesTest() throws Exception {
        String nameGiven = patient.getName().get(0).getGiven().get(0).toString();
        String nameFamily = patient.getName().get(0).getFamily().get(0).toString();
        String namePrefix = patient.getName().get(0).getPrefix().get(0).toString();
        String nameSuffix = patient.getName().get(0).getSuffix().get(0).toString();
        assertTrue("Wrong given name", nameGiven.equals("Jan"));
        assertTrue("Wrong family name", nameFamily.equals("Kowalski"));
        assertTrue("Wrong name prefix", namePrefix.equals("Mr"));
        assertTrue("Wrong name suffix", nameSuffix.equals("III"));
    }

    @org.junit.Test
    public void telecomTest() throws Exception {
        assertTrue("Wrong 1st telecom value", patient.getTelecom().get(0).getValue().equals("4444444444"));
        assertTrue("Wrong 1st telecom system", patient.getTelecom().get(0).getSystem().toString().equals("PHONE"));
        assertTrue("Wrong 1st telecom use", patient.getTelecom().get(0).getUse().toString().equals("HOME"));
        assertTrue("Wrong 2nd telecom value", patient.getTelecom().get(1).getValue().equals("5555555555"));
        assertTrue("Wrong 2nd telecom system", patient.getTelecom().get(1).getSystem().toString().equals("PHONE"));
        assertTrue("Wrong 2nd telecom use", patient.getTelecom().get(1).getUse().toString().equals("WORK"));
    }

    @org.junit.Test
    public void genderTest() throws Exception {
        assertTrue("Wrong gender", patient.getGender().toString().equals("MALE"));
    }

    @org.junit.Test
    public void dobTest() throws Exception {
        assertTrue("Wrong date of birth", patient.getBirthDate().getTime() == 1294182000000L);
    }
}