package service;

import java.sql.Connection;

public interface EquipmentService {

    public void addEquipment(Connection con);
    public void showAllEquipmentNames(Connection con);
    public void showEquipmentDetailsByName(Connection con);
    public void editEquipmentByName(Connection con);
    public void deleteEquipmentByName(Connection con);
}
