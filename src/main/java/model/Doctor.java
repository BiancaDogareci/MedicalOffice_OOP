package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "DOCTOR")
public class Doctor extends BaseEntity {
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "SPECIALIZATION")
    private String specialization;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients;
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    public Doctor(){}
    public Doctor(String firstName, String lastName, String specialization, String phoneNumber){ // ,List<Patient> patients, List<Appointment> appointments
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        // this.patients = patients;
        // this.appointments = appointments;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Patient> getPatients() {
        return patients;
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

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
