package service.impl;

import service.OtherEquipmentService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class OtherEquipmentServiceImpl implements OtherEquipmentService {
    @Override
    public void addOtherEquipment(Connection con, long equipmentId) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter usage: ");
        String usage = scanner.nextLine();

        try {
            String insertOtherSql = "INSERT INTO OTHER_EQUIPMENT (ID, USAGE) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertOtherSql);
            pstmt.setLong(1, equipmentId);
            pstmt.setString(2, usage);
            pstmt.executeUpdate();

            System.out.println("Other equipment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
