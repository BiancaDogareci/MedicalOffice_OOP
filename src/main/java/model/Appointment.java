package model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "APPOINTMENT")
public class Appointment extends BaseEntity {
    @Temporal(TemporalType.DATE) // ex. it would store 08-07-17 in database
    @Column(name = "DATE")
    private Date date;
    @Column(name = "TIME")
    private String time;
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;
    @OneToOne
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

    public Appointment(){}
    public Appointment(Date date, String time, Patient patient, Doctor doctor, Payment payment){
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
        this.payment = payment;
    }

    // Getters and setters
    public Date getDate() {
        return date;
    }

    public String getTime(){
        return time;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
