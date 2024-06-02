package service;

import java.sql.Connection;

public interface PatientService {
    public void addPatient(Connection con);
    public void getPatientByName(Connection con);
    public void updatePatientByName(Connection con);
    public void deletePatientByName(Connection con);
}
