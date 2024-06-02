package service.impl;

import service.PatientService;
import java.sql.*;
import java.util.Scanner;

public class PatientServiceImpl implements PatientService{

    // CREATE
    @Override
    public void addPatient(Connection con) {
        Scanner scanner = new Scanner(System.in);

        // Let user give input
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter birth date (YYYY-MM-DD): ");
        String birthDate = scanner.nextLine();

        System.out.print("Enter registration date (YYYY-MM-DD): ");
        String registrationDate = scanner.nextLine();

        System.out.print("Enter sex: ");
        String sex = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        try {
            String selectDoctorsSql = "SELECT ID, FIRST_NAME, LAST_NAME FROM DOCTOR WHERE SPECIALIZATION = 'family doctor'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectDoctorsSql);

            System.out.println("Family doctors:");
            while (rs.next()) {
                long doctorId = rs.getLong("ID");
                String doctorFirstName = rs.getString("FIRST_NAME");
                String doctorLastName = rs.getString("LAST_NAME");
                System.out.println("ID: " + doctorId + ", Name: " + doctorFirstName + " " + doctorLastName);
            }

            System.out.print("Enter first name of the family doctor: ");
            String doctorFirstName = scanner.nextLine();

            System.out.print("Enter last name of the family doctor: ");
            String doctorLastName = scanner.nextLine();

            String selectDoctorIdSql = "SELECT ID FROM DOCTOR WHERE FIRST_NAME = ? AND LAST_NAME = ? AND SPECIALIZATION = 'family doctor'";
            PreparedStatement pstmt = con.prepareStatement(selectDoctorIdSql);
            pstmt.setString(1, doctorFirstName);
            pstmt.setString(2, doctorLastName);
            ResultSet doctorResultSet = pstmt.executeQuery();

            long doctorId = -1;
            if (doctorResultSet.next()) {
                doctorId = doctorResultSet.getLong("ID");
            } else {
                System.out.println("No family doctor found with the given name.");
                return;
            }

            // Insert patient details into the PATIENT table
            String insertSql = "INSERT INTO PATIENT (FIRST_NAME, LAST_NAME, BIRTH_DATE, REGISTRATION_DATE, SEX, PHONE_NUMBER, DOCTOR_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(insertSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setDate(3, java.sql.Date.valueOf(birthDate));
            pstmt.setDate(4, java.sql.Date.valueOf(registrationDate));
            pstmt.setString(5, sex);
            pstmt.setString(6, phoneNumber);
            pstmt.setLong(7, doctorId);
            pstmt.executeUpdate();
            System.out.println("Patient added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    @Override
    public void getPatientByName(Connection con) {
        Scanner scanner = new Scanner(System.in);

        // Let user give input
        System.out.print("Enter first name of the patient: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name of the patient: ");
        String lastName = scanner.nextLine();

        try {
            String selectSql = "SELECT * FROM PATIENT WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Patient Details:");
                System.out.println("First Name: " + rs.getString("FIRST_NAME"));
                System.out.println("Last Name: " + rs.getString("LAST_NAME"));
                System.out.println("Birth Date: " + rs.getDate("BIRTH_DATE"));
                System.out.println("Registration Date: " + rs.getDate("REGISTRATION_DATE"));
                System.out.println("Sex: " + rs.getString("SEX"));
                System.out.println("Phone Number: " + rs.getString("PHONE_NUMBER"));
                long doctorId = rs.getLong("DOCTOR_ID");

                // Retrieve the doctors details using the DOCTOR_ID
                String selectDoctorSql = "SELECT FIRST_NAME, LAST_NAME FROM DOCTOR WHERE ID = ?";
                pstmt = con.prepareStatement(selectDoctorSql);
                pstmt.setLong(1, doctorId);
                ResultSet doctorResultSet = pstmt.executeQuery();

                if (doctorResultSet.next()) {
                    System.out.println("Doctor's First Name: " + doctorResultSet.getString("FIRST_NAME"));
                    System.out.println("Doctor's Last Name: " + doctorResultSet.getString("LAST_NAME"));
                } else {
                    System.out.println("No doctor found with the given ID.");
                }
            } else {
                System.out.println("No patient found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    @Override
    public void updatePatientByName(Connection con) {
        Scanner scanner = new Scanner(System.in);

        // Let user give input
        System.out.print("Enter first name of the patient to update: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name of the patient to update: ");
        String lastName = scanner.nextLine();

        try {
            // Retrieve the patient to update
            String selectSql = "SELECT * FROM PATIENT WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.print("Enter new first name: ");
                String newFirstName = scanner.nextLine();

                System.out.print("Enter new last name: ");
                String newLastName = scanner.nextLine();

                System.out.print("Enter new birth date (YYYY-MM-DD): ");
                String newBirthDate = scanner.nextLine();

                System.out.print("Enter new registration date (YYYY-MM-DD): ");
                String newRegistrationDate = scanner.nextLine();

                System.out.print("Enter new sex: ");
                String newSex = scanner.nextLine();

                System.out.print("Enter new phone number: ");
                String newPhoneNumber = scanner.nextLine();

                // Display list of family doctors
                String selectDoctorsSql = "SELECT ID, FIRST_NAME, LAST_NAME FROM DOCTOR WHERE SPECIALIZATION = 'family doctor'";
                Statement stmt = con.createStatement();
                ResultSet doctorsRs = stmt.executeQuery(selectDoctorsSql);

                System.out.println("Family doctors:");
                while (doctorsRs.next()) {
                    long doctorId = doctorsRs.getLong("ID");
                    String doctorFirstName = doctorsRs.getString("FIRST_NAME");
                    String doctorLastName = doctorsRs.getString("LAST_NAME");
                    System.out.println("ID: " + doctorId + ", Name: " + doctorFirstName + " " + doctorLastName);
                }

                // Get doctor details from user input
                System.out.print("Enter first name of the new family doctor: ");
                String doctorFirstName = scanner.nextLine();

                System.out.print("Enter last name of the new family doctor: ");
                String doctorLastName = scanner.nextLine();

                // Retrieve the doctor ID based on the provided names
                String selectDoctorIdSql = "SELECT ID FROM DOCTOR WHERE FIRST_NAME = ? AND LAST_NAME = ? AND SPECIALIZATION = 'family doctor'";
                pstmt = con.prepareStatement(selectDoctorIdSql);
                pstmt.setString(1, doctorFirstName);
                pstmt.setString(2, doctorLastName);
                ResultSet doctorResultSet = pstmt.executeQuery();

                long doctorId = -1;
                if (doctorResultSet.next()) {
                    doctorId = doctorResultSet.getLong("ID");
                } else {
                    System.out.println("No family doctor found with the given name.");
                    return;
                }

                // Update patient details
                String updateSql = "UPDATE PATIENT SET FIRST_NAME = ?, LAST_NAME = ?, BIRTH_DATE = ?, REGISTRATION_DATE = ?, SEX = ?, PHONE_NUMBER = ?, DOCTOR_ID = ? WHERE FIRST_NAME = ? AND LAST_NAME = ?";
                pstmt = con.prepareStatement(updateSql);
                pstmt.setString(1, newFirstName);
                pstmt.setString(2, newLastName);
                pstmt.setDate(3, java.sql.Date.valueOf(newBirthDate));
                pstmt.setDate(4, java.sql.Date.valueOf(newRegistrationDate));
                pstmt.setString(5, newSex);
                pstmt.setString(6, newPhoneNumber);
                pstmt.setLong(7, doctorId);
                pstmt.setString(8, firstName);
                pstmt.setString(9, lastName);
                pstmt.executeUpdate();
                System.out.println("Patient updated successfully.");
            } else {
                System.out.println("No patient found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    @Override
    public void deletePatientByName(Connection con) {
        Scanner scanner = new Scanner(System.in);

        // Let user give input
        System.out.print("Enter first name of the patient to delete: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name of the patient to delete: ");
        String lastName = scanner.nextLine();

        try {
            // Delete patient from the PATIENT table
            String deleteSql = "DELETE FROM PATIENT WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteSql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Patient deleted successfully.");
            } else {
                System.out.println("No patient found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
