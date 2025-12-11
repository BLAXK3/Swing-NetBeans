package com.github.blaxk3.fruit.shop;

/**
 *
 * @author BLAXK
 */
public class ShowDataDialog extends javax.swing.JDialog {

    private final Database db;
    private final Main main;
    private int ans;
    
    /**
     * Creates new form ShowDataDialog
     */
    public ShowDataDialog(Database db, Main main) {
        this.db = db;
        this.main = main;
        initComponents();
        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dispose();
            }
        });
        setModal(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPartPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        delTableButton = new javax.swing.JButton();
        clearDataTableButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Show Order Details");
        setName("showDataDialog"); // NOI18N

        mainPartPanel.setBackground(new java.awt.Color(255, 255, 255));

        dataTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "order_number", "ref_number", "product_name", "quantity", "total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane.setViewportView(dataTable);

        delTableButton.setBackground(new java.awt.Color(255, 102, 51));
        delTableButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        delTableButton.setForeground(new java.awt.Color(0, 0, 0));
        delTableButton.setText("Delete Table");
        delTableButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        delTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delTableButtonActionPerformed(evt);
            }
        });

        clearDataTableButton.setBackground(new java.awt.Color(255, 255, 51));
        clearDataTableButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        clearDataTableButton.setForeground(new java.awt.Color(0, 0, 0));
        clearDataTableButton.setText("Clear Data");
        clearDataTableButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        clearDataTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearDataTableButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPartPanelLayout = new javax.swing.GroupLayout(mainPartPanel);
        mainPartPanel.setLayout(mainPartPanelLayout);
        mainPartPanelLayout.setHorizontalGroup(
            mainPartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPartPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(clearDataTableButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(delTableButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105))
        );
        mainPartPanelLayout.setVerticalGroup(
            mainPartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPartPanelLayout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(mainPartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delTableButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearDataTableButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void delTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delTableButtonActionPerformed
       if (main.callOptionPaneConfirmDialog("Are you sure?", "Confirm") == javax.swing.JOptionPane.YES_OPTION) {
            db.deleteTabel();
            main.getShowButton().setEnabled(false);
            this.dispose();
       }
    }//GEN-LAST:event_delTableButtonActionPerformed

    private void clearDataTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearDataTableButtonActionPerformed
        if (main.callOptionPaneConfirmDialog("Are you sure?", "Confirm") == javax.swing.JOptionPane.YES_OPTION) {
            db.clearTable();
            db.showOrderDetails();
       }
    }//GEN-LAST:event_clearDataTableButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearDataTableButton;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton delTableButton;
    private javax.swing.JPanel mainPartPanel;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

    protected javax.swing.JTable getDataTable() {
        return dataTable;
    }
}
