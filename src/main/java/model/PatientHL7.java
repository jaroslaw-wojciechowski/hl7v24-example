package model;

import ca.uhn.hl7v2.model.v24.datatype.*;

public class PatientHL7 {
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
}
