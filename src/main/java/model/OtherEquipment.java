package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "OTHER_EQUIPMENT")
public class OtherEquipment extends Equipment{
    @Column(name = "USAGE")
    private String usage;

    public OtherEquipment() {}
    public OtherEquipment(String name, Integer quantity, Date purchaseDate, String usage) {
        super(name, quantity, purchaseDate);
        this.usage = usage;
    }

    // Getters and setters
    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
