package service;

import java.sql.Connection;

public interface AppointmentService {
    public void addAppointment(Connection con);
    public void getAppointmentsByDoctor(Connection con);
    public void getAppointmentsByPatient(Connection con);
    public void deleteAppointmentWithPayment(Connection con);
    public void editAppointment(Connection con);
}
