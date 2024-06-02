package service;

import java.sql.Connection;

public interface DoctorService {
    public void addDoctor(Connection con);
    public void getDoctors(Connection con);
    public void getDoctorByName(Connection con);
    public void updateDoctorByName(Connection con);
    public void deleteDoctorByName(Connection con);
}
