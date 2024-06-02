package service;

import java.sql.Connection;

public interface TreatmentEquipmentService {
    public void addTreatmentEquipment(Connection con, long equipmentId);
}
