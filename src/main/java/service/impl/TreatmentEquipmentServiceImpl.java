package service.impl;

import service.TreatmentEquipmentService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class TreatmentEquipmentServiceImpl implements TreatmentEquipmentService {
    @Override
    public void addTreatmentEquipment(Connection con, long equipmentId) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter treatment method: ");
        String treatmentMethod = scanner.nextLine();

        try {
            String insertTreatmentSql = "INSERT INTO TREATMENT_EQUIPMENT (ID, TREATMENT_METHOD) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertTreatmentSql);
            pstmt.setLong(1, equipmentId);
            pstmt.setString(2, treatmentMethod);
            pstmt.executeUpdate();

            System.out.println("Treatment equipment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
