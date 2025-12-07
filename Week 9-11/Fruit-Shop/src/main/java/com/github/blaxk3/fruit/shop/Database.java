package com.github.blaxk3.fruit.shop;
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
    public Database(Main main, char status) {
        this.main = main;
        if (status == 'a') {
//            addStudentGrade(main.studentData());
        }
        else {
            showStudentData();
        }
    }

    public Connection connectDatabase() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=Your-database-name;encrypt=false";
        String user = "";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            
            try(ResultSet rs = conn.getMetaData().getTables(null, null, "order_details", null)) {

                if (!rs.next()) {
                    String sql =
                        "CREATE TABLE order_details ("
                        + "order_id INT PRIMARY KEY, "
                        + "order_name VARCHAR(20), "
                        + "order_quantity INT, "
                        + "order_price INT, "
                        + "total_price  INT"
                        + ");";

                    try(Statement stmt = conn.createStatement()) {
                        stmt.executeUpdate(sql);
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
