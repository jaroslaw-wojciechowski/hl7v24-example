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

    public String getPatientIdentifierId() {
        if (identifier.length > 0) {
            return identifier[0].getID().toString();
        }
        return null;
    }

    public String getPatientIdentifierNamespace() {
        if (identifier.length > 0) {
            return identifier[0].getAssigningAuthority().getNamespaceID().toString();
        }
        return null;
    }

    public String getPatientIdentifierTypeCode() {
        if (identifier.length > 0) {
            return identifier[0].getIdentifierTypeCode().toString();
        }
        return null;
    }

    public String getPatientNameGiven() {
        if (patientNames.length > 0) {
            return patientNames[0].getGivenName().toString();
        }
        return null;
    }

    public String getPatientNameFamily() {
        if (patientNames.length > 0) {
            return patientNames[0].getFamilyName().getSurname().toString();
        }
        return null;
    }

    public String getPatientNameSuffix() {
        if (patientNames.length > 0) {
            return patientNames[0].getSuffixEgJRorIII().toString();
        }
        return null;
    }

    public String getPatientNamePrefix() {
        if (patientNames.length > 0) {
            return patientNames[0].getPrefixEgDR().toString();
        }
        return null;
    }

    public String getPatienPhoneHome() {
        if (phoneHome.length > 0) {
            return phoneHome[0].get9999999X99999CAnyText().toString();
        }
        return null;
    }

    public String getPatienPhoneBusiness() {
        if (phoneBusiness.length > 0) {
            return phoneBusiness[0].get9999999X99999CAnyText().toString();
        }
        return null;
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

    public void setMartialStatus(CE martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String getStreet() {
        if (addresses.length > 0) {
            return addresses[0].getStreetAddress().getStreetOrMailingAddress().toString();
        }
        return null;
    }

    public String getCity() {
        if (addresses.length > 0) {
            return addresses[0].getCity().toString();
        }
        return null;
    }

    public String getState() {
        if (addresses.length > 0) {
            return addresses[0].getStateOrProvince().toString();
        }
        return null;
    }

    public String getPostalCode() {
        if (addresses.length > 0) {
            return addresses[0].getZipOrPostalCode().toString();
        }
        return null;
    }
}
