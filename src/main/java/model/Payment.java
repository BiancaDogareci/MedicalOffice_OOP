package model;

import javax.persistence.*;

@Entity
@Table(name = "PAYMENT")
public class Payment extends BaseEntity {
    @Column(name = "TYPE")
    private String type;
    @Column(name = "AMOUNT")
    private Integer amount;
    @OneToOne(mappedBy = "payment")
    private Appointment appointment;

    public Payment(){}
    public Payment (String type, Integer amount, Appointment appointment){
        this.type = type;
        this.amount = amount;
        this.appointment = appointment;
    }

    public Payment (String type, Integer amount){
        this.type = type;
        this.amount = amount;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
