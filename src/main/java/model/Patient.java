package model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PATIENT")
public class Patient extends BaseEntity {
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DATE")
    private Date birthDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "REGISTRATION_DATE")
    private Date registrationDate;
    @Column(name = "SEX")
    private String sex;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;
    @OneToMany(mappedBy = "patient")
    private List<MedicalRecord> medicalRecords;
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    public Patient() {}
    public Patient (String firstName, String lastName, Date birthDate, Date registrationDate, String sex, String phoneNumber, Doctor doctor, List<MedicalRecord> medicalRecords, List<Appointment> appointments){
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.doctor = doctor;
        this.medicalRecords = medicalRecords;
        this.appointments = appointments;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public String getSex() {
        return sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
