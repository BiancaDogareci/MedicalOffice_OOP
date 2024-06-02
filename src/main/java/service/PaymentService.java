package service;

import java.sql.Connection;
import java.sql.SQLException;

public interface PaymentService {
    public long addPayment(Connection con);
    public void deletePayment(Connection con, long paymentId) throws SQLException;
    public void editPayment(Connection con, long appointmentId);
    public void showPayments(Connection con);
}
