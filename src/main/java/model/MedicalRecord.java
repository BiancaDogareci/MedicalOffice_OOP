package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MEDICAL_RECORD")
public class MedicalRecord extends BaseEntity {
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE")
    private Date date;
    @Column(name = "DIAGNOSIS")
    private String diagnosis;
    @Column(name = "PRESCRIBED_MEDICATION")
    private String prescribedMedication;
    @Column(name = "TREATMENT_PERIOD")
    private String treatmentPeriod;
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    public MedicalRecord() {}
    public MedicalRecord(Date date, String diagnosis, String prescribedMedication, String treatmentPeriod, Patient patient){
        this.date = date;
        this.diagnosis = diagnosis;
        this.prescribedMedication = prescribedMedication;
        this.treatmentPeriod = treatmentPeriod;
        this.patient = patient;
    }

    // Getters and setters
    public Date getDate() {
        return date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPrescribedMedication() {
        return prescribedMedication;
    }

    public String getTreatmentPeriod() {
        return treatmentPeriod;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setPrescribedMedication(String prescribedMedication) {
        this.prescribedMedication = prescribedMedication;
    }

    public void setTreatmentPeriod(String treatmentPeriod) {
        this.treatmentPeriod = treatmentPeriod;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public String toString() {
        return "MedicalRecord{" +
                "date=" + date +
                ", diagnosis='" + diagnosis + '\'' +
                ", prescribedMedication='" + prescribedMedication + '\'' +
                ", treatmentPeriod='" + treatmentPeriod + '\'' +
                ", patient=" + patient.getFirstName() + " " + patient.getLastName() +
                '}';
    }
}
