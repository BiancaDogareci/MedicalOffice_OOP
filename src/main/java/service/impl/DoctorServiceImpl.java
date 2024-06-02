package service.impl;

import service.DoctorService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DoctorServiceImpl implements DoctorService {

    // CREATE
    @Override
    public void addDoctor(Connection con){
        PreparedStatement pstmt = null;
        try {
            Scanner scanner = new Scanner(System.in);

            // Let user give input
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter specialization: ");
            String specialization = scanner.nextLine();
            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine();

            String insertSql = "INSERT INTO DOCTOR (FIRST_NAME, LAST_NAME, SPECIALIZATION, PHONE_NUMBER) VALUES (?, ?, ?, ?)";
            pstmt = con.prepareStatement(insertSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, specialization);
            pstmt.setString(4, phoneNumber);

            // Execute insert statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Show all doctors
    @Override
    public void getDoctors(Connection con) {
        try {
            String selectSql = "SELECT * FROM DOCTOR";
            PreparedStatement pstmt = con.prepareStatement(selectSql);
            ResultSet rs = pstmt.executeQuery();

            // Print all doctors
            System.out.println("Doctors List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("ID"));
                System.out.println("First Name: " + rs.getString("FIRST_NAME"));
                System.out.println("Last Name: " + rs.getString("LAST_NAME"));
                System.out.println("Specialization: " + rs.getString("SPECIALIZATION"));
                System.out.println("Phone Number: " + rs.getString("PHONE_NUMBER"));
                System.out.println("-----------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    @Override
    public void getDoctorByName(Connection con) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Scanner scanner = new Scanner(System.in);
            // Let user give input
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            String selectSql = "SELECT * FROM DOCTOR WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            pstmt = con.prepareStatement(selectSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("ID");
                String specialization = rs.getString("SPECIALIZATION");
                String phoneNumber = rs.getString("PHONE_NUMBER");
                System.out.println("Doctor ID: " + id + ", First Name: " + firstName + ", Last Name: " + lastName +
                        ", Specialization: " + specialization + ", Phone Number: " + phoneNumber);
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

    // UPDATE
    @Override
    public void updateDoctorByName(Connection con) {
        PreparedStatement pstmt = null;
        try {
            Scanner scanner = new Scanner(System.in);
            // Let user give input (old data)
            System.out.print("Enter first name: ");
            String oldFirstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String oldLastName = scanner.nextLine();

            // Prompt the user for input (new data for the doctor)
            System.out.print("Enter new first name: ");
            String newFirstName = scanner.nextLine();
            System.out.print("Enter new last name: ");
            String newLastName = scanner.nextLine();
            System.out.print("Enter new specialization: ");
            String newSpecialization = scanner.nextLine();
            System.out.print("Enter new phone number: ");
            String newPhoneNumber = scanner.nextLine();

            String updateSql = "UPDATE DOCTOR SET FIRST_NAME = ?, LAST_NAME = ?, SPECIALIZATION = ?, PHONE_NUMBER = ? WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            pstmt = con.prepareStatement(updateSql);
            pstmt.setString(1, newFirstName);
            pstmt.setString(2, newLastName);
            pstmt.setString(3, newSpecialization);
            pstmt.setString(4, newPhoneNumber);
            pstmt.setString(5, oldFirstName);
            pstmt.setString(6, oldLastName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Doctor updated successfully.");
            } else {
                System.out.println("No doctor found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // DELETE
    @Override
    public void deleteDoctorByName(Connection con) {
        PreparedStatement pstmt = null;
        try {
            Scanner scanner = new Scanner(System.in);
            // Let user give input
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            String deleteSql = "DELETE FROM DOCTOR WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            pstmt = con.prepareStatement(deleteSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Doctor deleted successfully.");
            } else {
                System.out.println("No doctor found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
