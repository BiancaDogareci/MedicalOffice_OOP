package service;

import java.sql.Connection;

public interface MedicalRecordService {
    public void addMedicalRecord(Connection con);
    public void getMedicalRecordsOfAPatient(Connection con);
    public void updateMedicalRecord(Connection con);
    public void deleteMedicalRecord(Connection con);
}
