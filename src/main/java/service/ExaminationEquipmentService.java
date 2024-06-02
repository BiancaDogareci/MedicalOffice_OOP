package service;

import java.sql.Connection;

public interface ExaminationEquipmentService {
    public void addExaminationEquipment(Connection con, long equipmentId);
}
