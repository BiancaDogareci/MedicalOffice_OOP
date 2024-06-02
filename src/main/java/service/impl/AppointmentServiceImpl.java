package service.impl;

import model.Appointment;
import model.Payment;
import service.AppointmentService;
import java.sql.*;
import java.util.Scanner;

public class AppointmentServiceImpl implements AppointmentService {
    @Override
    public void addAppointment(Connection con) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Get appointment details from user input
            System.out.print("Enter appointment date (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            System.out.print("Enter appointment time (HH:MM): ");
            String time = scanner.nextLine();

            System.out.print("Enter first name of the patient: ");
            String patientFirstName = scanner.nextLine();

            System.out.print("Enter last name of the patient: ");
            String patientLastName = scanner.nextLine();

            System.out.print("Enter first name of the doctor: ");
            String doctorFirstName = scanner.nextLine();

            System.out.print("Enter last name of the doctor: ");
            String doctorLastName = scanner.nextLine();

            // Retrieve the patient ID based on the names
            long patientId = getPatientId(con, patientFirstName, patientLastName);
            if (patientId == -1) {
                System.out.println("No patient found with the given name.");
                return;
            }

            // Retrieve the doctor ID based on the names
            long doctorId = getDoctorId(con, doctorFirstName, doctorLastName);
            if (doctorId == -1) {
                System.out.println("No doctor found with the given name.");
                return;
            }

            // Call addPayment function and get the payment ID
            PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();
            long paymentId = paymentServiceImpl.addPayment(con);
            if (paymentId == -1) {
                System.out.println("Failed to add payment.");
                return;
            }

            // Insert appointment details into the APPOINTMENT table
            String insertSql = "INSERT INTO APPOINTMENT (DATE, TIME, PATIENT_ID, DOCTOR_ID, PAYMENT_ID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertSql);
            pstmt.setDate(1, java.sql.Date.valueOf(date));
            pstmt.setString(2, time);
            pstmt.setLong(3, patientId);
            pstmt.setLong(4, doctorId);
            pstmt.setLong(5, paymentId);
            pstmt.executeUpdate();

            System.out.println("Appointment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAppointmentsByDoctor(Connection con) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first name of the doctor: ");
        String doctorFirstName = scanner.nextLine();

        System.out.print("Enter last name of the doctor: ");
        String doctorLastName = scanner.nextLine();

        try {
            // Retrieve the doctor ID based on the provided names
            long doctorId = getDoctorId(con, doctorFirstName, doctorLastName);
            if (doctorId == -1) {
                System.out.println("No doctor found with the given name.");
                return;
            }

            // Retrieve the appointments for the doctor
            String selectAppointmentsSql = "SELECT * FROM APPOINTMENT WHERE DOCTOR_ID = ?";
            PreparedStatement pstmt = con.prepareStatement(selectAppointmentsSql);
            pstmt.setLong(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            // Display the appointments
            System.out.println("Appointments for Dr. " + doctorFirstName + " " + doctorLastName + ":");
            while (rs.next()) {
                System.out.println("Appointment ID: " + rs.getLong("ID"));
                System.out.println("Date: " + rs.getDate("DATE"));
                System.out.println("Time: " + rs.getString("TIME"));
                System.out.println("-------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAppointmentsByPatient(Connection con) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first name of the patient: ");
        String patientFirstName = scanner.nextLine();

        System.out.print("Enter last name of the patient: ");
        String patientLastName = scanner.nextLine();

        try {
            // Retrieve the patient ID based on the names
            long patientId = getPatientId(con, patientFirstName, patientLastName);
            if (patientId == -1) {
                System.out.println("No patient found with the given name.");
                return;
            }

            // Retrieve the appointments for the patient
            String selectAppointmentsSql = "SELECT * FROM APPOINTMENT WHERE PATIENT_ID = ?";
            PreparedStatement pstmt = con.prepareStatement(selectAppointmentsSql);
            pstmt.setLong(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            // Display the appointments
            System.out.println("Appointments for patient " + patientFirstName + " " + patientLastName + ":");
            while (rs.next()) {
                System.out.println("Appointment ID: " + rs.getLong("ID"));
                System.out.println("Date: " + rs.getDate("DATE"));
                System.out.println("Time: " + rs.getString("TIME"));
                System.out.println("-------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAppointmentWithPayment(Connection con) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the appointment ID: ");
        long appointmentId = scanner.nextLong();

        try {
            // Check if the appointment exists
            if (!doesAppointmentExist(con, appointmentId)) {
                System.out.println("No appointment found with the given ID.");
                return;
            }

            // Delete the appointment
            String deleteAppointmentSql = "DELETE FROM APPOINTMENT WHERE ID = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteAppointmentSql);
            pstmt.setLong(1, appointmentId);
            pstmt.executeUpdate();

            // Delete the payment (using appointment ID as payment ID)
            PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();
            paymentServiceImpl.deletePayment(con, appointmentId);

            System.out.println("Appointment and its associated payment deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editAppointment(Connection con) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the appointment ID: ");
        long appointmentId = scanner.nextLong();
        scanner.nextLine();

        try {
            // Check if the appointment exists
            String selectAppointmentSql = "SELECT * FROM APPOINTMENT WHERE ID = ?";
            PreparedStatement pstmt = con.prepareStatement(selectAppointmentSql);
            pstmt.setLong(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No appointment found with the given ID.");
                return;
            }

            // Get existing appointment details
            Date currentDate = rs.getDate("DATE");
            String currentTime = rs.getString("TIME");

            // Get updated appointment details from the user
            System.out.print("Enter new date (YYYY-MM-DD, leave blank to keep current): ");
            String newDate = scanner.nextLine().trim();
            if (newDate.isEmpty()) {
                newDate = currentDate.toString(); // Keep old value
            }

            System.out.print("Enter new time (HH:MM:SS, leave blank to keep current): ");
            String newTime = scanner.nextLine().trim();
            if (newTime.isEmpty()) {
                newTime = currentTime; // Keep old value
            }

            // Update appointment in the database
            String updateSql = "UPDATE APPOINTMENT SET DATE = ?, TIME = ? WHERE ID = ?";
            pstmt = con.prepareStatement(updateSql);
            pstmt.setDate(1, Date.valueOf(newDate));
            pstmt.setString(2, newTime);
            pstmt.setLong(3, appointmentId);
            pstmt.executeUpdate();

            // Edit the payment associated with this appointment
            PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();
            paymentServiceImpl.editPayment(con, appointmentId);

            System.out.println("Appointment updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Function to check if an appointment exists
    private boolean doesAppointmentExist(Connection con, long appointmentId) throws SQLException {
        String checkAppointmentSql = "SELECT 1 FROM APPOINTMENT WHERE ID = ?";
        PreparedStatement pstmt = con.prepareStatement(checkAppointmentSql);
        pstmt.setLong(1, appointmentId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    // Functions to get the id's of a Patient and Doctor
    private long getPatientId(Connection con, String firstName, String lastName) throws SQLException {
        String selectPatientIdSql = "SELECT ID FROM PATIENT WHERE FIRST_NAME = ? AND LAST_NAME = ?";
        PreparedStatement pstmt = con.prepareStatement(selectPatientIdSql);
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getLong("ID");
        } else {
            return -1;
        }
    }

    private long getDoctorId(Connection con, String firstName, String lastName) throws SQLException {
        String selectDoctorIdSql = "SELECT ID FROM DOCTOR WHERE FIRST_NAME = ? AND LAST_NAME = ?";
        PreparedStatement pstmt = con.prepareStatement(selectDoctorIdSql);
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getLong("ID");
        } else {
            return -1;
        }
    }
}
