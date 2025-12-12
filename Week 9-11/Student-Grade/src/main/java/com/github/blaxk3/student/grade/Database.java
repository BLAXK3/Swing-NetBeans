package com.github.blaxk3.student.grade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author BLAXK
 */

public class Database {

    private final Main main;
    private final String dbName = "students"; // default 
    private final String tableName = "student_grade"; // default 
    private final String user = "Your-User";
    private final String password = "Your-Password";
    private final String port = "1433"; // default 
    private final boolean encrypt = false; // default 
    private final String serverUrl = "jdbc:sqlserver://localhost:"+ port +";encrypt=" + encrypt;
    private final String dbUrl = "jdbc:sqlserver://localhost:"+ port +";databaseName=" + dbName + ";encrypt=" + encrypt;
    
    public Database(Main main, char status) {
        this.main = main;
        if (status == 'a') {
            addStudentGrade(main.studentData());
        }
        else {
            showStudentData();
        }
    }

    private Connection connectDatabase() {
        Connection conn;

        try {
            conn = DriverManager.getConnection(serverUrl, user, password);

            try (PreparedStatement ps = conn.prepareStatement("SELECT name FROM sys.databases WHERE name = ?")) {
                ps.setString(1, dbName);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        try (Statement st = conn.createStatement()) {
                            st.executeUpdate("CREATE DATABASE " + dbName);
                        }
                    }
                }
            }
            conn.close();
            conn = DriverManager.getConnection(dbUrl, user, password);

            try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {

                if (!rs.next()) {
                    String createTable
                            = "CREATE TABLE " + tableName + "("
                            + "std_number INT IDENTITY(1,1) PRIMARY KEY, "
                            + "std_id VARCHAR(20) NOT NULL, "
                            + "first_name VARCHAR(20), "
                            + "surname VARCHAR(20), "
                            + "grade VARCHAR(2)"
                            + ");";

                    try (Statement stmt = conn.createStatement()) {
                        stmt.executeUpdate(createTable);
                    }
                }
                return conn;
            }
        } catch (SQLException e) {
            main.callOptionPane(e.toString(), "Error", 0);
            System.exit(0);
            return null;
        }
    }
    
    public void addStudentGrade(String[] data) {
            String sql = 
                "MERGE student_grade AS target " +
                "USING (SELECT ? AS std_id, ? AS first_name, ? AS surname, ? AS grade) AS src " +
                "ON target.std_id = src.std_id " +
                "WHEN MATCHED THEN " +
                "    UPDATE SET grade = src.grade " +
                "WHEN NOT MATCHED THEN " +
                "    INSERT (std_id, first_name, surname, grade) " +
                "    VALUES (src.std_id, src.first_name, src.surname, src.grade);";

            try(Connection conn = connectDatabase();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, data[0]); // std_id
                ps.setString(2, data[1]); // first_name
                ps.setString(3, data[2]); // surname
                ps.setString(4, data[3]); // grade
                ps.executeUpdate();
                main.callOptionPane("The database has been updated successfully" , "Info", 1);
            }
            catch (SQLException e) {
                main.callOptionPane(e.toString(), "Error", 0);
                System.exit(0);
            }
    }

    public void showStudentData() {
        String sql = "SELECT * FROM student_grade";

        try (Connection conn = connectDatabase();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            ShowDataDialog dialog = new ShowDataDialog();
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) dialog.getDataTable().getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("std_number"),
                    rs.getString("std_id"),
                    rs.getString("first_name"),
                    rs.getString("surname"),
                    rs.getString("grade")
                });
            }
            dialog.setVisible(true);
        } catch (SQLException e) {
            main.callOptionPane(e.toString(), "Error", 0);
            System.exit(0);
        }
    }
}
