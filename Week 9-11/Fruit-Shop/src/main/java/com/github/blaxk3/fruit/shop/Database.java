package com.github.blaxk3.fruit.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {

    private final Main main;
    private final ShowDataDialog dialog;
    private final String dbName = "fruit_shop"; // default 
    private final String tableName = "order_details"; // default 
    private final String user = "Your-User";
    private final String password = "Your-Password";
    private final String port = "1433"; // default 
    private final boolean encrypt = false; // default 
    private final String serverUrl = "jdbc:sqlserver://localhost:"+ port +";encrypt=" + encrypt;
    private final String dbUrl = "jdbc:sqlserver://localhost:"+ port +";databaseName=" + dbName + ";encrypt=" + encrypt;
    
    
    /**
     * Constructor for Database class.
     * If status == 'a', it will insert receipt data.
     * Otherwise, it will show previously saved order details.
     *
     * @param main   Main class reference for accessing UI callbacks
     * @param status 'a' = add receipt, otherwise show data
     */
    public Database(Main main, char status) {
        this.main = main;
        dialog = new ShowDataDialog(this, main);
        if (status == 'a') {
            addReceiptData(main.getRefNumGen(), main.getReceiptData(),
                    Integer.toString(main.getTotal().get()));
        } else {
            showOrderDetails();
        }
    }

    /**
     * Connects to SQL Server, creates database and table if do not exist.
     *
     * @return Connection object connected to the database
     */
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
                            + "order_number INT IDENTITY(1,1) PRIMARY KEY, "
                            + "ref_number VARCHAR(20), "
                            + "product_name VARCHAR(50), "
                            + "quantity INT, "
                            + "total_price INT"
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
    
    /**
     * Inserts receipt (order) data into the database.
     *
     * @param refNumber  Reference number of the order
     * @param order      A map containing product name → quantity
     * @param total      Total price as string
     */
    private void addReceiptData(String refNumber, java.util.Map<String, Integer> order, String total) {

        String sql = "INSERT INTO " + tableName
                + " (ref_number, product_name, quantity, total_price) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = connectDatabase(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int totalPrice = Integer.parseInt(total);

            for (java.util.Map.Entry<String, Integer> entry : order.entrySet()) {
                ps.setString(1, refNumber);
                ps.setString(2, entry.getKey());
                ps.setInt(3, entry.getValue());
                ps.setInt(4, totalPrice);
                ps.addBatch();
            }
            ps.executeBatch();
            main.callOptionPane("The database has been updated successfully", "Info", 1);

        } catch (SQLException e) {
            main.callOptionPane(e.toString(), "Error", 0);
            System.exit(0);
        }
    }

    /**
     * Loads and displays all order details inside ShowDataDialog.
     * Used when status != 'a' in constructor.
     */
    public void showOrderDetails() {
        String sql = "SELECT * FROM " + tableName;

        try (Connection conn = connectDatabase(); 
                PreparedStatement ps = conn.prepareStatement(sql); 
                ResultSet rs = ps.executeQuery()) {

            
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) dialog.getDataTable().getModel();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("order_number"),
                    rs.getString("ref_number"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getInt("total_price")
                });
            }

            dialog.setVisible(true);

        } catch (SQLException e) {
            main.callOptionPane(e.toString(), "Error", 0);
            System.exit(0);
        }
    }

    /**
     * Clears all data from the table using TRUNCATE TABLE.
     * This will remove all rows and reset the identity counter.
     *
     * @apiNote TRUNCATE cannot be executed if the table is referenced
     *          by a FOREIGN KEY or currently in use.
     */
    protected void clearTable() {
        String sql = "TRUNCATE TABLE " + tableName;

        try (Connection conn = connectDatabase(); 
                Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            main.callOptionPane("All data has been deleted!", "Info", 1);

        } catch (SQLException e) {
            main.callOptionPane(e.toString(), "Error", 0);
            System.exit(0);
        }
    }

    /**
     * Deletes the entire table from the database.
     *
     * @implNote This permanently removes the table structure.
     * @throws SQLException if the table does not exist or database connection fails.
     */
    protected void deleteTabel() {
        String sql = "DROP TABLE " + tableName;

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password); 
                Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            main.callOptionPane("Table deleted successfully", "Info", 1);

        } catch (SQLException e) {
            main.callOptionPane(e.toString(), "Error", 0);
            System.exit(0);
        }
    }
}
