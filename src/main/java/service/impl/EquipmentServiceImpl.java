package service.impl;

import liquibase.pro.packaged.O;
import service.EquipmentService;

import java.sql.*;
import java.util.Scanner;

public class EquipmentServiceImpl implements EquipmentService {
    @Override
    public void addEquipment(Connection con) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Get equipment details from user input
            System.out.print("Enter equipment name: ");
            String name = scanner.nextLine();

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter purchase date (YYYY-MM-DD): ");
            String purchaseDate = scanner.nextLine();

            // Insert equipment details into the EQUIPMENT table
            String insertEquipmentSql = "INSERT INTO EQUIPMENT (NAME, QUANTITY, PURCHASE_DATE) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertEquipmentSql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDate(3, Date.valueOf(purchaseDate));
            pstmt.executeUpdate();

            // Retrieve the generated equipment ID
            ResultSet rs = pstmt.getGeneratedKeys();
            long equipmentId = -1;
            if (rs.next()) {
                equipmentId = rs.getLong(1);
            }

            // Ask user for the type of equipment
            System.out.print("Enter equipment type (1. examination, 2. treatment, 3. other): (1-3) ");
            int type = scanner.nextInt();
            scanner.nextLine();

            while (type < 1 || type > 3) {
                System.out.print("Enter valid equipment type (examination, treatment, other): (1-3) ");
                type = scanner.nextInt();
                scanner.nextLine();
            }

            switch (type) {
                case 1:
                    ExaminationEquipmentServiceImpl examinationEquipmentServiceImpl = new ExaminationEquipmentServiceImpl();
                    examinationEquipmentServiceImpl.addExaminationEquipment(con, equipmentId);
                    break;
                case 2:
                    TreatmentEquipmentServiceImpl treatmentEquipmentServiceImpl = new TreatmentEquipmentServiceImpl();
                    treatmentEquipmentServiceImpl.addTreatmentEquipment(con, equipmentId);
                    break;
                case 3:
                    OtherEquipmentServiceImpl otherEquipmentService = new OtherEquipmentServiceImpl();
                    otherEquipmentService.addOtherEquipment(con, equipmentId);
                    break;
                default:
                    System.out.println("Invalid equipment type selected.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showAllEquipmentNames(Connection con) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String selectSql = "SELECT NAME FROM EQUIPMENT";
            stmt = con.createStatement();
            rs = stmt.executeQuery(selectSql);

            System.out.println("Equipment names in the database:");
            while (rs.next()) {
                String name = rs.getString("NAME");
                System.out.println(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showEquipmentDetailsByName(Connection con) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the equipment: ");
        String equipmentName = scanner.nextLine();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String selectSql = "SELECT ID, NAME, QUANTITY, PURCHASE_DATE FROM EQUIPMENT WHERE NAME = ?";
            pstmt = con.prepareStatement(selectSql);
            pstmt.setString(1, equipmentName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long equipmentId = rs.getLong("ID");
                String name = rs.getString("NAME");
                int quantity = rs.getInt("QUANTITY");
                Date purchaseDate = rs.getDate("PURCHASE_DATE");

                System.out.println("Equipment Details:");
                System.out.println("Name: " + name);
                System.out.println("Quantity: " + quantity);
                System.out.println("Purchase Date: " + purchaseDate);

                showSpecificEquipmentDetails(con, equipmentId);
            } else {
                System.out.println("No equipment found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSpecificEquipmentDetails(Connection con, long equipmentId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String checkExaminationSql = "SELECT EXAMINATION_TYPE FROM EXAMINATION_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkExaminationSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String examinationType = rs.getString("EXAMINATION_TYPE");
                System.out.println("Examination Type: " + examinationType);
                return;
            }

            String checkTreatmentSql = "SELECT TREATMENT_METHOD FROM TREATMENT_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkTreatmentSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String treatmentMethod = rs.getString("TREATMENT_METHOD");
                System.out.println("Treatment Method: " + treatmentMethod);
                return;
            }

            String checkOtherSql = "SELECT USAGE FROM OTHER_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkOtherSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String usage = rs.getString("USAGE");
                System.out.println("Usage: " + usage);
                return;
            }

            System.out.println("No specific equipment details found for the given equipment ID.");

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }

    @Override
    public void editEquipmentByName(Connection con) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the equipment to edit: ");
        String equipmentName = scanner.nextLine();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String selectSql = "SELECT ID, NAME, QUANTITY, PURCHASE_DATE FROM EQUIPMENT WHERE NAME = ?";
            pstmt = con.prepareStatement(selectSql);
            pstmt.setString(1, equipmentName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long equipmentId = rs.getLong("ID");
                String name = rs.getString("NAME");
                int quantity = rs.getInt("QUANTITY");
                Date purchaseDate = rs.getDate("PURCHASE_DATE");

                // Display current equipment details
                System.out.println("Current Equipment Details:");
                System.out.println("Name: " + name);
                System.out.println("Quantity: " + quantity);
                System.out.println("Purchase Date: " + purchaseDate);

                // Ask for new details
                System.out.print("Enter new name (leave blank to keep current): ");
                String newName = scanner.nextLine();
                if (newName.isEmpty()) newName = name;

                System.out.print("Enter new quantity (leave blank to keep current): ");
                String newQuantityInput = scanner.nextLine();
                int newQuantity = newQuantityInput.isEmpty() ? quantity : Integer.parseInt(newQuantityInput);

                System.out.print("Enter new purchase date (YYYY-MM-DD, leave blank to keep current): ");
                String newPurchaseDateInput = scanner.nextLine();
                Date newPurchaseDate = newPurchaseDateInput.isEmpty() ? purchaseDate : Date.valueOf(newPurchaseDateInput);

                // Update equipment details
                String updateSql = "UPDATE EQUIPMENT SET NAME = ?, QUANTITY = ?, PURCHASE_DATE = ? WHERE ID = ?";
                pstmt = con.prepareStatement(updateSql);
                pstmt.setString(1, newName);
                pstmt.setInt(2, newQuantity);
                pstmt.setDate(3, new java.sql.Date(newPurchaseDate.getTime()));
                pstmt.setLong(4, equipmentId);
                pstmt.executeUpdate();

                // Determine which table has the equipment ID and update the respective row in the table
                updateSpecificEquipmentDetails(con, equipmentId);

                System.out.println("Equipment updated successfully.");
            } else {
                System.out.println("No equipment found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSpecificEquipmentDetails(Connection con, long equipmentId) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Check ExaminationEquipment table
            String checkExaminationSql = "SELECT EXAMINATION_TYPE FROM EXAMINATION_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkExaminationSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String currentExaminationType = rs.getString("EXAMINATION_TYPE");
                System.out.print("Enter new examination type (leave blank to keep current: " + currentExaminationType + "): ");
                String newExaminationType = scanner.nextLine();
                if (newExaminationType.isEmpty()) newExaminationType = currentExaminationType;

                String updateExaminationSql = "UPDATE EXAMINATION_EQUIPMENT SET EXAMINATION_TYPE = ? WHERE ID = ?";
                pstmt = con.prepareStatement(updateExaminationSql);
                pstmt.setString(1, newExaminationType);
                pstmt.setLong(2, equipmentId);
                pstmt.executeUpdate();
                return;
            }

            // Check TreatmentEquipment table
            String checkTreatmentSql = "SELECT TREATMENT_METHOD FROM TREATMENT_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkTreatmentSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String currentTreatmentMethod = rs.getString("TREATMENT_METHOD");
                System.out.print("Enter new treatment method (leave blank to keep current: " + currentTreatmentMethod + "): ");
                String newTreatmentMethod = scanner.nextLine();
                if (newTreatmentMethod.isEmpty()) newTreatmentMethod = currentTreatmentMethod;

                String updateTreatmentSql = "UPDATE TREATMENT_EQUIPMENT SET TREATMENT_METHOD = ? WHERE ID = ?";
                pstmt = con.prepareStatement(updateTreatmentSql);
                pstmt.setString(1, newTreatmentMethod);
                pstmt.setLong(2, equipmentId);
                pstmt.executeUpdate();
                return;
            }

            // Check OtherEquipment table
            String checkOtherSql = "SELECT USAGE FROM OTHER_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkOtherSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String currentUsage = rs.getString("USAGE");
                System.out.print("Enter new usage (leave blank to keep current: " + currentUsage + "): ");
                String newUsage = scanner.nextLine();
                if (newUsage.isEmpty()) newUsage = currentUsage;

                String updateOtherSql = "UPDATE OTHER_EQUIPMENT SET USAGE = ? WHERE ID = ?";
                pstmt = con.prepareStatement(updateOtherSql);
                pstmt.setString(1, newUsage);
                pstmt.setLong(2, equipmentId);
                pstmt.executeUpdate();
                return;
            }

            System.out.println("No specific equipment details found for the given equipment ID.");

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }

    @Override
    public void deleteEquipmentByName(Connection con) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the equipment to delete: ");
        String equipmentName = scanner.nextLine();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String selectSql = "SELECT ID FROM EQUIPMENT WHERE NAME = ?";
            pstmt = con.prepareStatement(selectSql);
            pstmt.setString(1, equipmentName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long equipmentId = rs.getLong("ID");

                deleteSpecificEquipmentDetails(con, equipmentId);

                String deleteSql = "DELETE FROM EQUIPMENT WHERE ID = ?";
                pstmt = con.prepareStatement(deleteSql);
                pstmt.setLong(1, equipmentId);
                pstmt.executeUpdate();

                System.out.println("Equipment deleted successfully.");
            } else {
                System.out.println("No equipment found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteSpecificEquipmentDetails(Connection con, long equipmentId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Check ExaminationEquipment table
            String checkExaminationSql = "SELECT ID FROM EXAMINATION_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkExaminationSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String deleteExaminationSql = "DELETE FROM EXAMINATION_EQUIPMENT WHERE ID = ?";
                pstmt = con.prepareStatement(deleteExaminationSql);
                pstmt.setLong(1, equipmentId);
                pstmt.executeUpdate();
                return;
            }

            // Check TreatmentEquipment table
            String checkTreatmentSql = "SELECT ID FROM TREATMENT_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkTreatmentSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String deleteTreatmentSql = "DELETE FROM TREATMENT_EQUIPMENT WHERE ID = ?";
                pstmt = con.prepareStatement(deleteTreatmentSql);
                pstmt.setLong(1, equipmentId);
                pstmt.executeUpdate();
                return;
            }

            // Check OtherEquipment table
            String checkOtherSql = "SELECT ID FROM OTHER_EQUIPMENT WHERE ID = ?";
            pstmt = con.prepareStatement(checkOtherSql);
            pstmt.setLong(1, equipmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String deleteOtherSql = "DELETE FROM OTHER_EQUIPMENT WHERE ID = ?";
                pstmt = con.prepareStatement(deleteOtherSql);
                pstmt.setLong(1, equipmentId);
                pstmt.executeUpdate();
                return;
            }

            System.out.println("No specific equipment details found for the given equipment ID.");

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }

}
