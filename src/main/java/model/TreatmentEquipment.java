package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TREATMENT_EQUIPMENT")
public class TreatmentEquipment extends Equipment {
    @Column(name = "TREATMENT_METHOD")
    private String treatmentMethod;

    public TreatmentEquipment() {}
    public TreatmentEquipment(String name, Integer quantity, Date purchaseDate, String treatmentMethod) {
        super(name, quantity, purchaseDate);
        this.treatmentMethod = treatmentMethod;
    }

    // Getters and setters
    public String getTreatmentMethod() {
        return treatmentMethod;
    }

    public void setTreatmentMethod(String treatmentMethod) {
        this.treatmentMethod = treatmentMethod;
    }
}
