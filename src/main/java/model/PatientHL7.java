package model;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.datatype.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class PatientHL7 {
    private final Logger slf4jLogger = LoggerFactory.getLogger(PatientHL7.class);

    private CX[] identifier;
    private XPN[] patientNames;
    private XTN[] phoneHome;
    private XTN[] phoneBusiness;
    private IS gender;
    private TS dob;
    private ID deceasedInd;
    private TS deceasedDate;
    private XAD[] addresses;
    private CE martialStatus;

    public void setIdentifier(CX[] identifier) {
        this.identifier = identifier;
    }

    public void setPatientNames(XPN[] patientNames) {
        this.patientNames = patientNames;
    }

    public void setPhoneHome(XTN[] phoneHome) {
        this.phoneHome = phoneHome;
    }

    public void setPhoneBusiness(XTN[] phoneBusiness) {
        this.phoneBusiness = phoneBusiness;
    }

    public void setGender(IS gender) {
        this.gender = gender;
    }

    public void setDateTimeOfBirth(TS dob) {
        this.dob = dob;
    }

    public void setDeceasedInd(ID deceasedInd) {
        this.deceasedInd = deceasedInd;
    }

    public void setDeceasedDate(TS deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    public void setAddresses(XAD[] addresses) {
        this.addresses = addresses;
    }

    public void setMartialStatus(CE martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String getPatientIdentifierId() {
        return identifier[0].getID().toString();
    }

    public String getPatientIdentifierNamespace() {
        return identifier[0].getAssigningAuthority().getNamespaceID().toString();
    }

    public String getPatientIdentifierTypeCode() {
        return identifier[0].getIdentifierTypeCode().toString();
    }

    public String getPatientNameGiven() {
        return patientNames[0].getGivenName().toString();
    }

    public String getPatientNameFamily() {
        return patientNames[0].getFamilyName().getSurname().toString();
    }

    public String getPatientNameSuffix() {
        return patientNames[0].getSuffixEgJRorIII().toString();
    }

    public String getPatientNamePrefix() {
        return patientNames[0].getPrefixEgDR().toString();

    }

    public String getPatienPhoneHome() {
        return phoneHome[0].get9999999X99999CAnyText().toString();
    }

    public String getPatienPhoneBusiness() {
        return phoneBusiness[0].get9999999X99999CAnyText().toString();
    }

    public String getPatientGender() {
        return gender.getValue().toLowerCase();
    }

    public Date getPatientDateTimeOfBirth() {
        try {
            return dob.getTimeOfAnEvent().getValueAsDate();
        } catch (DataTypeException e) {
            slf4jLogger.error(e.toString());
            throw new RuntimeException("Error while paring Date by getPatientDateTimeOfBirth method: " + e);
        }
    }

    public TS getPatientDeceasedDate() {
        return deceasedDate;
    }

    public ID getPatientDeceasedIndicator() {
        return deceasedInd;
    }

    public CE getMartialStatus() {
        return martialStatus;
    }

    /*
        patientFhir.addAddress().setCity("RandomCity");
        patientFhir.addAddress().setState("RandomState");
        patientFhir.addAddress().setPostalCode("80-010");
        patientFhir.addAddress().setCountry("RandomCountry");

            System.out.println("\taddress tokens: " + address.getStreetAddress().getStreetOrMailingAddress() + " "
                    + address.getOtherDesignation() + " "
                    + address.getCity() + " "
                    + address.getStateOrProvince() + " "
                    + address.getZipOrPostalCode());
     */

    public String getStreet() {
        return addresses[0].getStreetAddress().getStreetOrMailingAddress().toString();
    }
}
