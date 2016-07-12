package parser;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.datatype.*;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.Parser;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.*;

public class ParserHL7 {
    public Patient parseHl7ToFhirObject() throws FHIRException, HL7Exception {
        String msg = "MSH|^~\\&|IPM|LSP|RAD|RGQ|20100705100137||ADT^A28|765043596|P|2.4|12478673\r" +
                "EVN|A28|20100705100131\r" +
                "PID|||111111^^^RGQ^MR~2222222222^^^NHS^NH||Kowalski^Jan^Maria^III^Mr||20110105000000|Male|||RandomStreet^128B^RandomCity^RandomState^3333||4444444444|5555555555||M|||||||||||||20160709224441|Y\r" +
                "PD1|||PracticeName^^PracticeCode|GPCode^GPSurname^GPForename^^^DR^^NATGP\r";

        Patient patientFhir = new Patient();

        HapiContext context = new DefaultHapiContext();
        context.getParserConfiguration().setValidating(false);
        //Parser p = context.getGenericParser();
        Parser p = context.getPipeParser();

        Message hapiMsg;
        try {
            hapiMsg = p.parse(msg);
        } catch (EncodingNotSupportedException e) {
            e.printStackTrace();
            return null;
        } catch (HL7Exception e) {
            e.printStackTrace();
            return null;
        }

        ADT_A05 adtMsg = (ADT_A05) hapiMsg;

        CX[] identifiers = adtMsg.getPID().getPid3_PatientIdentifierList();
        for (CX id : identifiers) {
            patientFhir.addIdentifier()
                    .setSystem("http://hl7.org/fhir/sid/us-ssn")
                    .setValue(id.getID().toString());
        }

        //name PID-5, PID-9
        XPN[] patientNames = adtMsg.getPID().getPatientName();
        for (XPN patientName : patientNames) {
            patientFhir.addName()
                    .addGiven(patientName.getGivenName().toString())
                    .addFamily(patientName.getFamilyName().getSurname().toString())
                    .addPrefix(patientName.getPrefixEgDR().toString())
                    .addSuffix(patientName.getSuffixEgJRorIII().toString());
        }

        //telecom PID-13, PID-14, PID-40
        XTN[] phoneHome = adtMsg.getPID().getPhoneNumberHome();
        for (XTN phone : phoneHome) {
            patientFhir.addTelecom()
                    .setSystem(ContactPoint.ContactPointSystem.PHONE)
                    .setValue(phone.get9999999X99999CAnyText().toString())
                    .setUse(ContactPoint.ContactPointUse.HOME);
        }

        XTN[] phoneBusiness = adtMsg.getPID().getPhoneNumberBusiness();
        for (XTN phone : phoneBusiness) {
            patientFhir.addTelecom()
                    .setSystem(ContactPoint.ContactPointSystem.PHONE)
                    .setValue(phone.get9999999X99999CAnyText().toString())
                    .setUse(ContactPoint.ContactPointUse.WORK);
        }

        //gender PID-8
        IS gender = adtMsg.getPID().getAdministrativeSex();
        patientFhir.setGender(Enumerations.AdministrativeGender.fromCode(gender.getValue().toLowerCase()));

        //birthDate	PID-7
        TS dob = adtMsg.getPID().getDateTimeOfBirth();
        patientFhir.setBirthDate(dob.getTimeOfAnEvent().getValueAsDate());

        //deceased[x] PID-30 (bool) and PID-29 (datetime)
        ID deceasedInd = adtMsg.getPID().getPatientDeathIndicator();
        TS deceasedDate = adtMsg.getPID().getPatientDeathDateAndTime();
        //deceased can be date or boolean:
        if ((!deceasedInd.isEmpty()) && (deceasedInd.toString().equals("Y"))) {
            patientFhir.setDeceased(new BooleanType().setValue(true));
        }
        if (!deceasedDate.isEmpty()) {
            patientFhir.setDeceased(new DateTimeType().setValue(deceasedDate.getTimeOfAnEvent().getValueAsDate()));
        }

        //address PID-11
        XAD[] addresses = adtMsg.getPID().getPatientAddress();
        for (XAD address : addresses) {
            patientFhir.addAddress()
                    .addLine(address.getStreetAddress().getStreetOrMailingAddress().toString())
                    .setCity(address.getCity().toString())
                    .setState(address.getStateOrProvince().toString())
                    .setPostalCode(address.getZipOrPostalCode().toString());
        }

        //maritalStatus	PID-16
        CE maritalStatus = adtMsg.getPID().getMaritalStatus();
        patientFhir.getMaritalStatus()
                .addCoding().setSystem("http://hl7.org/fhir/v3/MaritalStatus")
                .setCode(maritalStatus.getIdentifier().toString());

        // only for debug
        /*
            FhirContext ctx = FhirContext.forDstu3();
            String jsonEncoded = ctx.newJsonParser().encodeResourceToString(patientFhir);
            String xmlEncoded = ctx.newXmlParser().encodeResourceToString(patientFhir);
            System.out.println(xmlEncoded);
        */

        return patientFhir;
    }
}