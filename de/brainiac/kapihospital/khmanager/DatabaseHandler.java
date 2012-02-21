/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khmanager;

import de.brainiac.kapihospital.khvalues.Disease;
import de.brainiac.kapihospital.khvalues.Patient;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {
    private String _dbName;
    private String _username;
    private String _password;
    private Connection _conn = null;
    private PreparedStatement _patientInsert, _patientUpdate, _patientDelete;
    private Statement _sqlStatement;
    private ResultSet _patientID, _allPatients;

    public DatabaseHandler(String dbName, String username, String password) {
        _dbName = "jdbc:mysql://localhost/" + dbName;
        _username = username;
        _password = password;
        
        //Test if DB Connection is possible
        if (connect()) {
            disconnect();
        }
    }

    private boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            _conn = DriverManager.getConnection(_dbName, _username, _password);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean disconnect() {
        try {
            if (_conn != null) {
                _conn.close();
            }
            if (_sqlStatement != null) {
                _sqlStatement.close();
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int insertPatient(Patient actualPatient) {
        int newPatientID = -1;
        connect();
        try {
            //InsertPatientValues
            _patientInsert = _conn.prepareStatement("INSERT INTO tblPatienten VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            _patientInsert.setString(1, actualPatient.getName());
            Disease[] actualDiseases = actualPatient.getDiseases();
            for (int x = 0; x < actualDiseases.length; x++) {
                _patientInsert.setInt(2+x, actualDiseases[x].getId());
            }
            for (int x = actualDiseases.length; x < 6; x++) {
                _patientInsert.setNull(2+x, Types.INTEGER);
            }
            for (int x = 0; x < actualDiseases.length; x++) {
                _patientInsert.setBoolean(8+x, actualDiseases[x].isTreated());
            }
            for (int x = actualDiseases.length; x < 6; x++) {
                _patientInsert.setNull(8+x, Types.BOOLEAN);
            }
            _patientInsert.setDouble(14, actualPatient.getMinPrice());
            _patientInsert.setDouble(15, actualPatient.getMaxPrice());
            _patientInsert.setNull(16, Types.TIMESTAMP);
            _patientInsert.setNull(17, Types.TIMESTAMP);
            _patientInsert.executeUpdate();
            _patientInsert.close();
            
            //get the ID;
            _sqlStatement = _conn.createStatement();
            // Result set get the result of the SQL query
            _patientID = _sqlStatement.executeQuery("SELECT LAST_INSERT_ID()");
            _patientID.next();
            newPatientID = _patientID.getInt(1);            
            _patientID.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        disconnect();
        return newPatientID;
    }

    public List<Patient> getPatients() {
        List<Patient> newPatientList = new ArrayList<Patient>();
        connect();
        try {
            _sqlStatement = _conn.createStatement();
            _allPatients = _sqlStatement.executeQuery("SELECT * FROM tblPatienten WHERE cashed is NULL");
            while (_allPatients.next()) {
                int id = _allPatients.getInt("patID");
                String name = _allPatients.getString("patName");
                int countedDiseases = 0;
                for (int x = 3; x < 9; x++) {
                    if (_allPatients.getInt(x) != 0) {
                        countedDiseases++;
                    }
                }
                int[] diseases = new int[countedDiseases];
                boolean[] treatedDiseases = new boolean[countedDiseases];
                for (int x = 3; x < 3+countedDiseases; x++) {
                    diseases[x-3] = _allPatients.getInt(x);
                    treatedDiseases[x-3] = _allPatients.getBoolean(x+6);
                }
                Double minPrice = _allPatients.getDouble("minPrice");
                Double maxPrice = _allPatients.getDouble("maxPrice");
                newPatientList.add(new Patient(id, name, diseases, treatedDiseases, minPrice, maxPrice));
            }
            _allPatients.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        disconnect();
        return newPatientList;
    }

    public boolean deletePatient(Patient actualPatient) {
        connect();
        try {
            _patientDelete = _conn.prepareStatement("DELETE FROM tblPatienten WHERE patID = ?");
            _patientDelete.setInt(1, actualPatient.getID());
            return _patientDelete.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        disconnect();
        return false;
    }
}