package service.impl;

import model.Payment;
import service.PaymentService;
import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class PaymentServiceImpl implements PaymentService {

    @Override
    public long addPayment(Connection con) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter payment type: ");
        String type = scanner.nextLine();

        System.out.print("Enter payment amount: ");
        Integer amount = scanner.nextInt();

        try {
            // Insert payment details into the PAYMENT table
            String insertSql = "INSERT INTO PAYMENT (TYPE, AMOUNT) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, type);
            pstmt.setDouble(2, amount);
            pstmt.executeUpdate();

            // Retrieve the generated payment ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void editPayment(Connection con, long appointmentId) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Get the existing payment details
            String selectPaymentSql = "SELECT * FROM PAYMENT WHERE ID = ?";
            PreparedStatement pstmt = con.prepareStatement(selectPaymentSql);
            pstmt.setLong(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No payment found with the given ID.");
                return;
            }

            String currentType = rs.getString("TYPE");
            int currentAmount = rs.getInt("AMOUNT");

            // Get updated payment details from the user
            System.out.print("Enter new payment type (leave blank to keep current): ");
            String newType = scanner.nextLine().trim();
            if (newType.isEmpty()) {
                newType = currentType; // Keep old value
            }

            System.out.print("Enter new payment amount (leave blank to keep current): ");
            String newAmountStr = scanner.nextLine().trim();
            int newAmount;
            if (newAmountStr.isEmpty()) {
                newAmount = currentAmount; // Keep old value
            } else {
                newAmount = Integer.parseInt(newAmountStr);
            }

            // Update payment in the database
            String updateSql = "UPDATE PAYMENT SET TYPE = ?, AMOUNT = ? WHERE ID = ?";
            pstmt = con.prepareStatement(updateSql);
            pstmt.setString(1, newType);
            pstmt.setInt(2, newAmount);
            pstmt.setLong(3, appointmentId);
            pstmt.executeUpdate();

            System.out.println("Payment updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showPayments(Connection con) {
        String selectPaymentSql = "SELECT * FROM PAYMENT";
        try (PreparedStatement pstmt = con.prepareStatement(selectPaymentSql);
             ResultSet rs = pstmt.executeQuery()) {

            Queue<Payment> paymentQueue = new LinkedList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                int amount = rs.getInt("amount");
                Payment payment = new Payment(type, amount);
                paymentQueue.offer(payment);
            }

            System.out.println("Payment amounts:");
            while (!paymentQueue.isEmpty()) {
                Payment payment = paymentQueue.poll();
                System.out.println("Type: " + payment.getType() + ", Amount: " + payment.getAmount());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePayment(Connection con, long paymentId) throws SQLException {
        String deletePaymentSql = "DELETE FROM PAYMENT WHERE ID = ?";
        PreparedStatement pstmt = con.prepareStatement(deletePaymentSql);
        pstmt.setLong(1, paymentId);
        pstmt.executeUpdate();
        System.out.println("Payment deleted successfully.");
    }
}
