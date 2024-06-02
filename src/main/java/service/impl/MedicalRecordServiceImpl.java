package service.impl;

import model.Doctor;
import model.MedicalRecord;
import model.Patient;
import service.MedicalRecordService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MedicalRecordServiceImpl implements MedicalRecordService {

    //CREATE
    @Override
    public void addMedicalRecord(Connection con){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter diagnosis: ");
        String diagnosis = scanner.nextLine();

        System.out.print("Enter prescribed medication: ");
        String prescribedMedication = scanner.nextLine();

        System.out.print("Enter treatment period: ");
        String treatmentPeriod = scanner.nextLine();

        System.out.print("Enter first name of the patient: ");
        String patientFirstName = scanner.nextLine();

        System.out.print("Enter last name of the patient: ");
        String patientLastName = scanner.nextLine();

        try {
            String selectPatientIdSql = "SELECT ID FROM PATIENT WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            PreparedStatement pstmt = con.prepareStatement(selectPatientIdSql);
            pstmt.setString(1, patientFirstName);
            pstmt.setString(2, patientLastName);
            ResultSet rs = pstmt.executeQuery();

            long patientId = -1;
            if (rs.next()) {
                patientId = rs.getLong("ID");
            } else {
                System.out.println("No patient found with the given name.");
                return;
            }

            String insertSql = "INSERT INTO MEDICAL_RECORD (DATE, DIAGNOSIS, PRESCRIBED_MEDICATION, TREATMENT_PERIOD, PATIENT_ID) VALUES (?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(insertSql);
            pstmt.setDate(1, java.sql.Date.valueOf(date));
            pstmt.setString(2, diagnosis);
            pstmt.setString(3, prescribedMedication);
            pstmt.setString(4, treatmentPeriod);
            pstmt.setLong(5, patientId);
            pstmt.executeUpdate();
            System.out.println("Medical record added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    @Override
    public void getMedicalRecordsOfAPatient(Connection con){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first name of the patient: ");
        String patientFirstName = scanner.nextLine();

        System.out.print("Enter last name of the patient: ");
        String patientLastName = scanner.nextLine();

        try {
            String selectPatientIdSql = "SELECT * FROM PATIENT WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            PreparedStatement pstmt = con.prepareStatement(selectPatientIdSql);
            pstmt.setString(1, patientFirstName);
            pstmt.setString(2, patientLastName);
            ResultSet rs = pstmt.executeQuery();

            long patientId = -1;
            Patient patient = null;
            if (rs.next()) {
                patientId = rs.getLong("ID");
                long doctorId = rs.getLong("DOCTOR_ID");
                Doctor doctor = getDoctorById(con, doctorId);

                patient = new Patient(
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getDate("BIRTH_DATE"),
                        rs.getDate("REGISTRATION_DATE"),
                        rs.getString("SEX"),
                        rs.getString("PHONE_NUMBER"),
                        doctor,
                        new ArrayList<>(), // medicalRecords
                        new ArrayList<>() // appointments
                );
            } else {
                System.out.println("No patient found with the given name.");
                return;
            }

            // Retrieve the medical record for the patient
            String selectMedicalRecordSql = "SELECT * FROM MEDICAL_RECORD WHERE PATIENT_ID = ?";
            pstmt = con.prepareStatement(selectMedicalRecordSql);
            pstmt.setLong(1, patientId);
            rs = pstmt.executeQuery();

            List<MedicalRecord> medicalRecords = new ArrayList<>();
            while (rs.next()) {
                MedicalRecord medicalRecord = new MedicalRecord(
                        rs.getDate("DATE"),
                        rs.getString("DIAGNOSIS"),
                        rs.getString("PRESCRIBED_MEDICATION"),
                        rs.getString("TREATMENT_PERIOD"),
                        patient
                );
                medicalRecords.add(medicalRecord);
            }

            // Sort the medicalRecords list based on the date
            medicalRecords.sort(Comparator.comparing(MedicalRecord::getDate));

            for (MedicalRecord medicalRecord : medicalRecords) {
                System.out.println(medicalRecord.toString());
                System.out.println("-------------");
            }

            // easier but without collections (List)
//            while (rs.next()) {
//                System.out.println("Medical Record:");
//                System.out.println("ID: " + rs.getString("ID"));
//                System.out.println("Date: " + rs.getDate("DATE"));
//                System.out.println("Diagnosis: " + rs.getString("DIAGNOSIS"));
//                System.out.println("Prescribed Medication: " + rs.getString("PRESCRIBED_MEDICATION"));
//                System.out.println("Treatment Period: " + rs.getString("TREATMENT_PERIOD"));
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    @Override
    public void updateMedicalRecord(Connection con){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first name of the patient: ");
        String patientFirstName = scanner.nextLine();

        System.out.print("Enter last name of the patient: ");
        String patientLastName = scanner.nextLine();

        try {
            String selectPatientIdSql = "SELECT ID FROM PATIENT WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            PreparedStatement pstmt = con.prepareStatement(selectPatientIdSql);
            pstmt.setString(1, patientFirstName);
            pstmt.setString(2, patientLastName);
            ResultSet rs = pstmt.executeQuery();

            long patientId = -1;
            if (rs.next()) {
                patientId = rs.getLong("ID");
            } else {
                System.out.println("No patient found with the given name.");
                return;
            }

            String selectMedicalRecordSql = "SELECT * FROM MEDICAL_RECORD WHERE PATIENT_ID = ?";
            pstmt = con.prepareStatement(selectMedicalRecordSql);
            pstmt.setLong(1, patientId);
            rs = pstmt.executeQuery();

            System.out.println("Medical Records for Patient:");
            while (rs.next()) {
                System.out.println("Medical Record ID: " + rs.getLong("ID"));
                System.out.println("Date: " + rs.getDate("DATE"));
                System.out.println("Diagnosis: " + rs.getString("DIAGNOSIS"));
                System.out.println("Prescribed Medication: " + rs.getString("PRESCRIBED_MEDICATION"));
                System.out.println("Treatment Period: " + rs.getString("TREATMENT_PERIOD"));
                System.out.println("-------------");
            }

            System.out.print("Enter the ID of the medical record you want to update: ");
            long medicalRecordId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Enter new date (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            System.out.print("Enter new diagnosis: ");
            String diagnosis = scanner.nextLine();

            System.out.print("Enter new prescribed medication: ");
            String prescribedMedication = scanner.nextLine();

            System.out.print("Enter new treatment period: ");
            String treatmentPeriod = scanner.nextLine();

            String updateSql = "UPDATE MEDICAL_RECORD SET DATE = ?, DIAGNOSIS = ?, PRESCRIBED_MEDICATION = ?, TREATMENT_PERIOD = ? WHERE ID = ?";
            pstmt = con.prepareStatement(updateSql);
            pstmt.setDate(1, java.sql.Date.valueOf(date));
            pstmt.setString(2, diagnosis);
            pstmt.setString(3, prescribedMedication);
            pstmt.setString(4, treatmentPeriod);
            pstmt.setLong(5, medicalRecordId);
            pstmt.executeUpdate();
            System.out.println("Medical record updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    @Override
    public void deleteMedicalRecord(Connection con){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first name of the patient: ");
        String patientFirstName = scanner.nextLine();

        System.out.print("Enter last name of the patient: ");
        String patientLastName = scanner.nextLine();

        try {
            String selectPatientIdSql = "SELECT ID FROM PATIENT WHERE FIRST_NAME = ? AND LAST_NAME = ?";
            PreparedStatement pstmt = con.prepareStatement(selectPatientIdSql);
            pstmt.setString(1, patientFirstName);
            pstmt.setString(2, patientLastName);
            ResultSet rs = pstmt.executeQuery();

            long patientId = -1;
            if (rs.next()) {
                patientId = rs.getLong("ID");
            } else {
                System.out.println("No patient found with the given name.");
                return;
            }

            String selectMedicalRecordSql = "SELECT * FROM MEDICAL_RECORD WHERE PATIENT_ID = ?";
            pstmt = con.prepareStatement(selectMedicalRecordSql);
            pstmt.setLong(1, patientId);
            rs = pstmt.executeQuery();

            System.out.println("Medical Records for Patient:");
            while (rs.next()) {
                System.out.println("Medical Record ID: " + rs.getLong("ID"));
                System.out.println("Date: " + rs.getDate("DATE"));
                System.out.println("Diagnosis: " + rs.getString("DIAGNOSIS"));
                System.out.println("Prescribed Medication: " + rs.getString("PRESCRIBED_MEDICATION"));
                System.out.println("Treatment Period: " + rs.getString("TREATMENT_PERIOD"));
                System.out.println("-------------");
            }

            System.out.print("Enter the ID of the medical record you want to delete: ");
            long medicalRecordId = scanner.nextLong();

            String deleteSql = "DELETE FROM MEDICAL_RECORD WHERE ID = ?";
            pstmt = con.prepareStatement(deleteSql);
            pstmt.setLong(1, medicalRecordId);
            pstmt.executeUpdate();
            System.out.println("Medical record deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Doctor getDoctorById(Connection con, long doctorId) throws SQLException {
        String selectDoctorSql = "SELECT * FROM DOCTOR WHERE ID = ?";
        PreparedStatement pstmt = con.prepareStatement(selectDoctorSql);
        pstmt.setLong(1, doctorId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return new Doctor(
                    rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME"),
                    rs.getString("SPECIALIZATION"),
                    rs.getString("PHONE_NUMBER")
                    //new ArrayList<>(), //patients
                    //new ArrayList<>() // appointments
            );
        } else {
            throw new SQLException("Doctor not found with ID: " + doctorId);
        }
    }
}
