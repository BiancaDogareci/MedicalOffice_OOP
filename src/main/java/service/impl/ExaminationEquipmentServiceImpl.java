package service.impl;

import service.ExaminationEquipmentService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ExaminationEquipmentServiceImpl implements ExaminationEquipmentService {
    @Override
    public void addExaminationEquipment(Connection con, long equipmentId) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter examination type: ");
        String examinationType = scanner.nextLine();

        try {
            String insertExaminationSql = "INSERT INTO EXAMINATION_EQUIPMENT (ID, EXAMINATION_TYPE) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertExaminationSql);
            pstmt.setLong(1, equipmentId);
            pstmt.setString(2, examinationType);
            pstmt.executeUpdate();

            System.out.println("Examination equipment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
