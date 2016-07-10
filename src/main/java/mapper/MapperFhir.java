package mapper;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.*;

import java.util.UUID;

public class MapperFhir {
    public void mapToFhir() {
        // Create a context for DSTU3
        FhirContext ctx = FhirContext.forDstu3();

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

        String encoded = ctx.newJsonParser().encodeResourceToString(patient);
        System.out.println(encoded);
    }
}
