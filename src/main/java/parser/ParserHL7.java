package parser;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.datatype.*;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.*;

public class ParserHL7 {
    //to be defined:
    private static final String IDENTIFIER_SYSTEM = "http://hl7.org/fhir/sid/us-ssn";
    private static final String MARITAL_STATUS_SYSTEM = "http://hl7.org/fhir/v3/MaritalStatus";

    public Patient parseHl7ToFhirObject(ADT_A05 adtMsg) throws FHIRException, HL7Exception {
        Patient patientFhir = new Patient();

        //identifier PID-3
        CX[] identifiers = adtMsg.getPID().getPid3_PatientIdentifierList();
        for (CX id : identifiers) {
            patientFhir.addIdentifier()
                    .setSystem(IDENTIFIER_SYSTEM)
                    .setValue(id.getID().toString());
        }

        //name PID-5, PID-9
        XPN[] patientNames = adtMsg.getPID().getPatientName();
        for (XPN patientName : patientNames) {
            patientFhir.addName()
                    .addGiven(patientName.getGivenName().toString())
                    .addGiven(patientName.getSecondAndFurtherGivenNamesOrInitialsThereof().toString())
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
        if(!gender.isEmpty()) {
            patientFhir.setGender(Enumerations.AdministrativeGender.fromCode(gender.getValue().toLowerCase()));
        }

        //birthDate	PID-7
        TS dob = adtMsg.getPID().getDateTimeOfBirth();
        patientFhir.setBirthDate(dob.getTimeOfAnEvent().getValueAsDate());

        //deceased[x] PID-30 (bool) and PID-29 (datetime)
        ID deceasedInd = adtMsg.getPID().getPatientDeathIndicator();
        if ((!deceasedInd.isEmpty()) && (deceasedInd.toString().equals("Y"))) {
            patientFhir.setDeceased(new BooleanType().setValue(true));
        }

        //address PID-11
        XAD[] addresses = adtMsg.getPID().getPatientAddress();
        for (XAD address : addresses) {
            patientFhir.addAddress()
                    .addLine(address.getStreetAddress().getStreetOrMailingAddress().toString())
                    .addLine(address.getOtherDesignation().toString())
                    .setCity(address.getCity().toString())
                    .setState(address.getStateOrProvince().toString())
                    .setPostalCode(address.getZipOrPostalCode().toString());
        }

        //maritalStatus	PID-16
        CE maritalStatus = adtMsg.getPID().getMaritalStatus();
        if (!maritalStatus.isEmpty()) {
            patientFhir.getMaritalStatus()
                    .addCoding().setSystem(MARITAL_STATUS_SYSTEM)
                    .setCode(maritalStatus.getIdentifier().toString());
        }

        return patientFhir;
    }
}
