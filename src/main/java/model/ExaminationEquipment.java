package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EXAMINATION_EQUIPMENT")
public class ExaminationEquipment extends Equipment {
    @Column(name = "EXAMINATION_TYPE")
    private String examinationType;

    public ExaminationEquipment() {}
    public ExaminationEquipment(String name, Integer quantity, Date purchaseDate, String examinationType) {
        super(name, quantity, purchaseDate);
        this.examinationType = examinationType;
    }

    // Getters and setters
    public String getExaminationType() {
        return examinationType;
    }

    public void setExaminationType(String examinationType) {
        this.examinationType = examinationType;
    }
}
