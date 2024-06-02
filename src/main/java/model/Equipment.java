package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "EQUIPMENT")
public class Equipment extends BaseEntity {
    @Column(name = "NAME")
    private String name;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Temporal(TemporalType.DATE)
    @Column(name = "PURCHASE_DATE")
    private Date purchaseDate;

    public Equipment(){}
    public Equipment(String name, Integer quantity, Date purchaseDate){
        this.name = name;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
