import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import model.Appointment;
import service.impl.*;
import service.*;
import javax.print.Doc;

public class Main {
    public static void main(String[] args) {

        // The connection with the database
        DbFunctions db = new DbFunctions();
        Connection connection = db.connect_to_db("MedicalOfficeDb", "postgres", "Bibi2003");

        // Scanner object from the Scanner class
        Scanner scanner = new Scanner(System.in);


        while(true){
            // Menu
            System.out.println("1. Appointment");
            System.out.println("2. Doctor");
            System.out.println("3. Patient");
            System.out.println("4. Medical record");
            System.out.println("5. Equipment");
            System.out.println("6. Show amounts earned");
            System.out.println("7. QUIT");

            System.out.println("  Choose an option (number between 1-7): ");
            int option = scanner.nextInt();

            while(option < 1 || option > 7){
                System.out.println("  Choose a valid option (number between 1-7): ");
                option = scanner.nextInt();
            }

            if (option == 1) {
                while(true) {
                    // Functionalities for Appointments
                    System.out.println("1. Add an appointment");
                    System.out.println("2. Show all appointments from a specific doctor");
                    System.out.println("3. Show all appointments from a specific patient");
                    System.out.println("4. Edit an appointment");
                    System.out.println("5. Delete an appointment");
                    System.out.println("6. Back to menu!");
                    System.out.println("  Choose a valid option (number between 1-6): ");
                    int choice = scanner.nextInt();
                    while (choice < 1 || choice > 6) {
                        System.out.println("  Choose a valid option (number between 1-6): ");
                        choice = scanner.nextInt();
                    }

                    AppointmentServiceImpl appointmentServiceImpl = new AppointmentServiceImpl();
                    if (choice == 1) {
                        // Add an appointment
                        appointmentServiceImpl.addAppointment(connection);
                    } else if (choice == 2) {
                        // Show all appointments from a specific doctor
                        appointmentServiceImpl.getAppointmentsByDoctor(connection);
                    } else if (choice == 3) {
                        // Show all appointments from a specific patient
                        appointmentServiceImpl.getAppointmentsByPatient(connection);
                    } else if (choice == 4) {
                        // Edit an appointment
                        appointmentServiceImpl.editAppointment(connection);
                    } else if (choice == 5) {
                        // Delete an appointment
                        appointmentServiceImpl.deleteAppointmentWithPayment(connection);
                    } else {
                        break;
                    }
                }
            }

            if (option == 2) {
                while(true){
                    // Functionalities for Doctors
                    System.out.println("1. Add a doctor");
                    System.out.println("2. Show all doctors");
                    System.out.println("3. Show a doctor");
                    System.out.println("4. Edit a doctor");
                    System.out.println("5. Delete a doctor");
                    System.out.println("6. Back to menu!");
                    System.out.println("  Choose a valid option (number between 1-6): ");
                    int choice = scanner.nextInt();
                    while (choice < 1 || choice > 6) {
                        System.out.println("  Choose a valid option (number between 1-6): ");
                        choice = scanner.nextInt();
                    }

                    DoctorServiceImpl doctorServiceImpl = new DoctorServiceImpl();
                    if (choice == 1) {
                        // Add a doctor
                        doctorServiceImpl.addDoctor(connection);
                    } else if (choice == 2) {
                        // Show all doctors
                        doctorServiceImpl.getDoctors(connection);
                    } else if (choice == 3) {
                        // Show a doctor
                        doctorServiceImpl.getDoctorByName(connection);
                    } else if (choice == 4) {
                        // Edit a doctor
                        doctorServiceImpl.updateDoctorByName(connection);
                    } else if (choice == 5){
                        // Delete a doctor
                        doctorServiceImpl.deleteDoctorByName(connection);
                    } else{
                        break;
                    }
                }
            }

            if (option == 3) {
                while(true){
                    // Functionalities for Patient
                    System.out.println("1. Add a patient");
                    System.out.println("2. Show a patient");
                    System.out.println("3. Edit a patient");
                    System.out.println("4. Delete a patient");
                    System.out.println("5. Back to menu!");
                    System.out.println("  Choose a valid option (number between 1-5): ");
                    int choice = scanner.nextInt();
                    while (choice < 1 || choice > 5) {
                        System.out.println("  Choose a valid option (number between 1-5): ");
                        choice = scanner.nextInt();
                    }

                    PatientServiceImpl patientServiceImpl = new PatientServiceImpl();
                    if (choice == 1) {
                        // Add a patient
                        patientServiceImpl.addPatient(connection);
                    } else if (choice == 2) {
                        // Show a patient
                        patientServiceImpl.getPatientByName(connection);
                    } else if (choice == 3) {
                        // Edit a patient
                        patientServiceImpl.updatePatientByName(connection);
                    } else if (choice == 4){
                        // Delete a patient
                        patientServiceImpl.deletePatientByName(connection);
                    } else{
                        break;
                    }
                }
            }

            if (option == 4){
                while(true) {
                    // Functionalities for Medical Record
                    System.out.println("1. Add a medical record");
                    System.out.println("2. Show all medical records of a patient");
                    System.out.println("3. Edit a medical record");
                    System.out.println("4. Delete a medical record");
                    System.out.println("5. Back to menu!");
                    System.out.println("  Choose a valid option (number between 1-5): ");
                    int choice = scanner.nextInt();
                    while (choice < 1 || choice > 5) {
                        System.out.println("  Choose a valid option (number between 1-5): ");
                        choice = scanner.nextInt();
                    }

                    MedicalRecordServiceImpl medicalRecordServiceImpl = new MedicalRecordServiceImpl();
                    if (choice == 1) {
                        // Add a medical record
                        medicalRecordServiceImpl.addMedicalRecord(connection);
                    } else if (choice == 2) {
                        // Show all medical records of a patient
                        medicalRecordServiceImpl.getMedicalRecordsOfAPatient(connection);
                    } else if (choice == 3) {
                        // Edit a medical record
                        medicalRecordServiceImpl.updateMedicalRecord(connection);
                    } else if (choice == 4){
                        // Delete a medical record
                        medicalRecordServiceImpl.deleteMedicalRecord(connection);
                    } else{
                        break;
                    }
                }
            }

            if (option == 5){
                while(true) {
                    // Functionalities for Equipment
                    System.out.println("1. Add equipment");
                    System.out.println("2. Show all equipment");
                    System.out.println("3. Show specific equipment details");
                    System.out.println("4. Edit equipment");
                    System.out.println("5. Delete equipment");
                    System.out.println("6. Back to menu!");
                    System.out.println("  Choose a valid option (number between 1-6): ");
                    int choice = scanner.nextInt();
                    while (choice < 1 || choice > 6) {
                        System.out.println("  Choose a valid option (number between 1-6): ");
                        choice = scanner.nextInt();
                    }

                    EquipmentServiceImpl equipmentServiceImpl = new EquipmentServiceImpl();
                    if (choice == 1) {
                        // Add equipment
                        equipmentServiceImpl.addEquipment(connection);
                    } else if (choice == 2) {
                        // Show all equipment
                        equipmentServiceImpl.showAllEquipmentNames(connection);
                    } else if (choice == 3) {
                        // Show specific equipment details
                        equipmentServiceImpl.showEquipmentDetailsByName(connection);
                    } else if (choice == 4) {
                        // Edit equipment
                        equipmentServiceImpl.editEquipmentByName(connection);
                    } else if (choice == 5) {
                        // Delete equipment
                        equipmentServiceImpl.deleteEquipmentByName(connection);
                    } else {
                        break;
                    }
                }
            }

            if(option == 6){
                PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();
                paymentServiceImpl.showPayments(connection);
            }

            if(option == 7){
                break;
            }

            System.out.println();
            System.out.println();
            System.out.println("Loading menu...");
            System.out.println();
            System.out.println();
        }

    }
}