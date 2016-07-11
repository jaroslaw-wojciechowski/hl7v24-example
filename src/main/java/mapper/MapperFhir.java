package mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hl7v2.HL7Exception;
import model.PatientHL7;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.*;

import java.text.ParseException;

public class MapperFhir {
    public void mapToFhir(PatientHL7 patientHL7) throws FHIRException, ParseException, HL7Exception {
        // Create a context for DSTU3
        FhirContext ctx = FhirContext.forDstu3();
        Patient patientFhir = new Patient();

        patientFhir.addIdentifier()
                .setSystem(patientHL7.getPatientIdentifierNamespace() + " " + patientHL7.getPatientIdentifierTypeCode())
                .setValue(patientHL7.getPatientIdentifierId());

        patientFhir.addName()
                .addGiven(patientHL7.getPatientNameGiven())
                .addFamily(patientHL7.getPatientNameFamily())
                .addPrefix(patientHL7.getPatientNamePrefix())
                .addSuffix(patientHL7.getPatientNameSuffix());

        patientFhir.addTelecom()
                .setSystem(ContactPoint.ContactPointSystem.PHONE)
                .setValue(patientHL7.getPatienPhoneHome())
                .setUse(ContactPoint.ContactPointUse.HOME);

        patientFhir.addTelecom()
                .setSystem(ContactPoint.ContactPointSystem.PHONE)
                .setValue(patientHL7.getPatienPhoneBusiness())
                .setUse(ContactPoint.ContactPointUse.WORK);

        patientFhir.setGender(Enumerations.AdministrativeGender.fromCode(patientHL7.getPatientGender()));

        patientFhir.setBirthDate(patientHL7.getPatientDateTimeOfBirth());

        //deceased can be date or boolean:
        if ((!patientHL7.getPatientDeceasedIndicator().isEmpty()) && (patientHL7.getPatientDeceasedIndicator().toString().equals("Y"))) {
            patientFhir.setDeceased(new BooleanType().setValue(true));
        }
        if (!patientHL7.getPatientDeceasedDate().isEmpty()) {
            patientFhir.setDeceased(new DateTimeType().setValue(patientHL7.getPatientDeceasedDate().getTimeOfAnEvent().getValueAsDate()));
        }

        patientFhir.getMaritalStatus().addCoding().setCode(patientHL7.getMartialStatus().getIdentifier().toString());

        patientFhir.addAddress().addLine(patientHL7.getStreet());
        patientFhir.addAddress().setCity(patientHL7.getCity());
        patientFhir.addAddress().setState(patientHL7.getState());
        patientFhir.addAddress().setPostalCode(patientHL7.getPostalCode());

        String encoded = ctx.newJsonParser().encodeResourceToString(patientFhir);
        System.out.println(encoded);
    }
}
