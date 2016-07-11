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
                .addFamily("TEST")
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

        /*
             "address": [
        {
          "use": "home",
          "type": "both",
          "line": [
            "534 Erewhon St"
          ],
          "city": "PleasantVille",
          "district": "Rainbow",
          "state": "Vic",
          "postalCode": "3999",
          "period": {
            "start": "1974-12-25"
          }
        }
      ],
         */

        patientFhir.addAddress().setCity("RandomCity");
        patientFhir.addAddress().setState("RandomState");
        patientFhir.addAddress().setPostalCode("80-010");
        patientFhir.addAddress().setCountry("RandomCountry");
        patientFhir.addAddress().addLine(patientHL7.getStreet());

        /*
        Organization org = new Organization();
        org.setId("Organization/65546");
        org.getNameElement().setValue("Contained Test Organization");

        Extension ext = new Extension();
        ext.setUrl("http://example.com/extensions#someext");
        ext.setValue(new DateTimeType("2011-01-02T11:13:15"));

        Patient patient = new Patient();
        String patientId = UUID.randomUUID().toString();
        patient.setId(new IdType("Patient", patientId));
        patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
        //patient.addIdentifier().setSystem("SYS").setValue("VAL").setType(new CodeableConcept().addCoding(new Coding().setSystem("http://hl7.org/fhir/v2/0203").setCode("MR")));
        patient.getManagingOrganization().setResource(org);
        patient.getText().setDivAsString("<div>BARFOO</div>");
        patient.addName().addGiven("John").addFamily("Smith").addSuffix("Junior").addPrefix("Mr");
        patient.getBirthDateElement().setValueAsString("1998-02-22");
        //patient.setBirthDate(new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse("2016-04-15 10:15:30"));
        //patient.setBirthDateElement(new DateType("2016-04-14"));
        patient.setGender(Enumerations.AdministrativeGender.MALE);
        patient.getMaritalStatus().addCoding().setCode("D");
        patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue("patpain@ehealthinnovation.org");
        patient.addExtension(ext);
        patient.getMeta().addProfile("http://foo/Profile1");
        patient.getMeta().addProfile("http://foo/Profile2");
        patient.getMeta().addTag().setSystem("scheme1").setCode("term1").setDisplay("label1");
        patient.getMeta().addTag().setSystem("scheme2").setCode("term2").setDisplay("label2");
        patient.getMeta().addSecurity().setSystem("sec_scheme1").setCode("sec_term1").setDisplay("sec_label1");
        patient.getMeta().addSecurity().setSystem("sec_scheme2").setCode("sec_term2").setDisplay("sec_label2");
        //patient.addAddress().setUse(Address.AddressUse.HOME);
        //patient.addExtension(new Extension("urn:foo", new Reference("Organization/123")));
        //patient.addAddress().setUse(Address.AddressUse.HOME);
        //EnumFactory<Address.AddressUse> fact = new Address.AddressUseEnumFactory();
        //PrimitiveType<Address.AddressUse> enumeration = new Enumeration<Address.AddressUse>(fact).setValue(Address.AddressUse.HOME);
        //patient.addExtension().setUrl("urn:foo").setValue(enumeration);
        patient.addAddress().setUse(Address.AddressUse.HOME);
        patient.addAddress().addLine("LINE1");
        patient.addAddress().setCity("RandomCity");
        patient.addAddress().setState("RandomState");
        patient.addAddress().setPostalCode("80-010");
        patient.addAddress().setCountry("RandomCountry");
        patient.addAddress().setUse(Address.AddressUse.WORK);
        patient.addAddress().addLine("LINE1");
        patient.addAddress().setCity("RandomCity");
        patient.addAddress().setState("RandomState");
        patient.addAddress().setPostalCode("80-010");
        patient.addAddress().setCountry("RandomCountry");
        */

        String encoded = ctx.newJsonParser().encodeResourceToString(patientFhir);
        System.out.println(encoded);
    }
}
