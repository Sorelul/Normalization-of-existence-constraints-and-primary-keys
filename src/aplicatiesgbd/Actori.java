/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aplicatiesgbd;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sorin
 */
public class Actori extends javax.swing.JFrame {

    private static final String parola = "Sorelul123(c++)";
    private static final String user = "root";
    private static final String dataConn = "jdbc:mysql://localhost:3306/mydb";
    
    Connection sqlConn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int q ,i,id,deleteItem,columnCount;
    
    

    public Actori() {
        initComponents();
    }

    public void upDateDB() throws SQLException, ClassNotFoundException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
            pst = sqlConn.prepareStatement("Select * from actori");
            
            rs = pst.executeQuery();
            ResultSetMetaData stData = rs.getMetaData();
            
            q = stData.getColumnCount();
            DefaultTableModel RecordTable;
            RecordTable = (DefaultTableModel)jTable1.getModel();
            RecordTable.setRowCount(0);
            
            while(rs.next()){
                Vector columnData = new Vector();
                
                for(i=1; i <= q; i++)
                {
                    columnData.add(rs.getInt("id"));
                    columnData.add(rs.getString("NumeActor"));
                    columnData.add(rs.getString("BiografieActor"));
                  
                }
                RecordTable.addRow(columnData);
                
            }
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
                
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblNumeActor = new javax.swing.JLabel();
        lblBiografieActor = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtNumeActor = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaBiografie = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnActualizeaza = new javax.swing.JButton();
        btnAdauga = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 300), new java.awt.Dimension(0, 300), new java.awt.Dimension(32767, 300));
        btnPrinteaza = new javax.swing.JButton();
        btnReseteaza = new javax.swing.JButton();
        btnSterge = new javax.swing.JButton();
        btnIesire = new javax.swing.JButton();
        btnTest1 = new javax.swing.JButton();
        btnTest2 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        miExit1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        miMeniu = new javax.swing.JMenuItem();
        miInapoi = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setText("    Actori");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(248, 248, 248))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        lblId.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblId.setText("ID:");

        lblNumeActor.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNumeActor.setText("NumeActor");

        lblBiografieActor.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblBiografieActor.setText("BiografieActor");

        txtAreaBiografie.setColumns(20);
        txtAreaBiografie.setRows(5);
        jScrollPane1.setViewportView(txtAreaBiografie);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "id", "NumeActor", "BiografieActor"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        btnActualizeaza.setText("Modifica");
        btnActualizeaza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizeazaActionPerformed(evt);
            }
        });

        btnAdauga.setText("Insereaza");
        btnAdauga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdaugaActionPerformed(evt);
            }
        });

        btnPrinteaza.setText("Vizualizeaza");
        btnPrinteaza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrinteazaActionPerformed(evt);
            }
        });

        btnReseteaza.setText("Reseteaza");
        btnReseteaza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReseteazaActionPerformed(evt);
            }
        });

        btnSterge.setText("Sterge");
        btnSterge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStergeActionPerformed(evt);
            }
        });

        btnIesire.setText("Iesire");
        btnIesire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIesireActionPerformed(evt);
            }
        });

        btnTest1.setText("Test1");
        btnTest1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTest1ActionPerformed(evt);
            }
        });

        btnTest2.setText("Test2");
        btnTest2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTest2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnIesire, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSterge, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnActualizeaza, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAdauga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPrinteaza, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(btnReseteaza, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTest1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTest2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(lblNumeActor, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumeActor, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(178, 178, 178)
                                    .addComponent(lblBiografieActor))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(559, 559, 559))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(btnAdauga, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnActualizeaza, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPrinteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnReseteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSterge, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTest1)
                                .addGap(2, 2, 2)
                                .addComponent(btnTest2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnIesire, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNumeActor, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblId)
                                        .addComponent(lblNumeActor)
                                        .addComponent(txtId)))
                                .addGap(18, 18, 18)
                                .addComponent(lblBiografieActor)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jMenu2.setText("File");

        miExit1.setText("Exit");
        miExit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExit1ActionPerformed(evt);
            }
        });
        jMenu2.add(miExit1);

        jMenuBar2.add(jMenu2);

        jMenu3.setText("Navigheaza");

        miMeniu.setText("Meniu");
        miMeniu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMeniuActionPerformed(evt);
            }
        });
        jMenu3.add(miMeniu);

        miInapoi.setText("Inapoi");
        miInapoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miInapoiActionPerformed(evt);
            }
        });
        jMenu3.add(miInapoi);

        jMenuBar2.add(jMenu3);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 690, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
     JFrame frame;
    private void btnIesireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIesireActionPerformed
      frame = new JFrame("Exit");
       if(JOptionPane.showConfirmDialog(frame, "Sunteti sigur ca doriti sa inchideti aplicatia?","MySQL",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
         System.exit(0);
    }
    }//GEN-LAST:event_btnIesireActionPerformed

    private void btnStergeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStergeActionPerformed
   DefaultTableModel RecordTable = (DefaultTableModel)jTable1.getModel();  
   int SelectedRows = jTable1.getSelectedRow();

       
               id = Integer.parseInt(RecordTable.getValueAt(SelectedRows, 0).toString());

           deleteItem = JOptionPane.showConfirmDialog(null, "Sigur doriti sa faceti aceasta stergere ?","Warning",JOptionPane.YES_NO_OPTION);
           if(deleteItem == JOptionPane.YES_OPTION)
           {
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
       
              sqlConn = DriverManager.getConnection(dataConn,user,parola);
                pst = sqlConn.prepareStatement("delete from actori where id =?");
                pst.setInt(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this,"Record updated");
                upDateDB();

                txtId.setText("");
                txtNumeActor.setText("");
                txtAreaBiografie.setText("");
         
            } catch (ClassNotFoundException ex) {
           java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       } catch (SQLException ex) {
           java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       }
        
    }//GEN-LAST:event_btnStergeActionPerformed
    }
    
    private void btnReseteazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReseteazaActionPerformed
        txtId.setText("");
        txtNumeActor.setText("");
        txtAreaBiografie.setText("");

    }//GEN-LAST:event_btnReseteazaActionPerformed

    private void btnPrinteazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrinteazaActionPerformed
        try {
            upDateDB();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPrinteazaActionPerformed

    private void btnAdaugaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdaugaActionPerformed
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);

            pst = sqlConn.prepareStatement("insert into actori(id,NumeActor,BiografieActor) value (?,?,?)");
            pst.setString(1,txtId.getText());
            pst.setString(2,txtNumeActor.getText());
            pst.setString(3,txtAreaBiografie.getText());
           

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,"Record Added");
            upDateDB();

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_btnAdaugaActionPerformed

    private void btnActualizeazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizeazaActionPerformed

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);

            pst = sqlConn.prepareStatement("UPDATE actori set NumeActor=?,BiografieActor=? WHERE id=?");
            pst.setString(1,txtNumeActor.getText());
            pst.setString(2,txtAreaBiografie.getText());
            pst.setString(3,txtId.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,"Record Updated");
            upDateDB();

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        
    }//GEN-LAST:event_btnActualizeazaActionPerformed

    private void miExit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExit1ActionPerformed
        dispose();
    }//GEN-LAST:event_miExit1ActionPerformed

    private void miMeniuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMeniuActionPerformed
        Meniu fr = new Meniu();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMeniuActionPerformed

    private void miInapoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miInapoiActionPerformed
        MyDB fr = new MyDB();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miInapoiActionPerformed

     public void getNullable()throws ClassNotFoundException, SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);


            pst = sqlConn.prepareStatement("SELECT TABLE_NAME, COLUMN_NAME FROM information_schema.COLUMNS WHERE IS_NULLABLE = 'yes' AND TABLE_SCHEMA = 'mydb'");
            rs = pst.executeQuery();
            
            ResultSetMetaData stData = rs.getMetaData();
            columnCount = stData.getColumnCount();
            
           System.out.println("------------------------------------------------------------------------------------------------");
           System.out.println("Urmatoarele coloane din tablele date nu au constrangerea de not null!");
           while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue + " " + stData.getColumnName(i));
                }
            System.out.println("");
            }
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private void btnTest1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest1ActionPerformed
        try {
            getNullable();
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTest1ActionPerformed

    
    public void normalizare_existenta() throws ClassNotFoundException, SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);


            pst = sqlConn.prepareStatement("SELECT TABLE_NAME, COLUMN_NAME FROM information_schema.COLUMNS WHERE IS_NULLABLE = 'yes' AND TABLE_SCHEMA = 'mydb'");
            rs = pst.executeQuery();
            
            ResultSetMetaData stData = rs.getMetaData();
            columnCount = stData.getColumnCount();
            
           System.out.println("------------------------------------------------------------------------------------------------");
           System.out.println("Urmatoarele coloane din tablele date nu au constrangerea de not null!");
           int contor = 0;
           Vector vectorasColoane = new Vector();
           Vector vectorasTabele = new Vector();
           while (rs.next()) {
                for (int i = 1; i <= columnCount; i+=1) {
                    if (i > 1) 
                        System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + stData.getColumnName(i));
                    contor++;
                    if(contor%2==0){
                        vectorasColoane.add(columnValue+" ");
                    }
                    else
                        vectorasTabele.add(columnValue+" ");
 
                }
            System.out.println("");
            }
           //JOptionPane.showMessageDialog(this, vectorasColoane.toString(),"Nume coloane",JOptionPane.PLAIN_MESSAGE);
           //JOptionPane.showMessageDialog(this, vectorasTabele.toString(),"Nume Tabele",JOptionPane.PLAIN_MESSAGE);
           
           int lungimeVectori = vectorasColoane.size();
           String numeColoana;
           String numeTabela;
           String tipColoana;
           
           Object[] options = {"Da,vă rog",
                    "Nu, mulțumesc"};
           int optiuneaAleasa;
           
           if(lungimeVectori>0){
           
            for(i=0;i<lungimeVectori;i++){
                numeTabela = vectorasTabele.get(i).toString();

                numeColoana = vectorasColoane.get(i).toString();
                
                optiuneaAleasa = JOptionPane.showOptionDialog(this,
                "Coloana "+numeColoana+" din tabela "+numeTabela+" nu are restricția de NULL.\n Doriți să normalizați această coloană?",
                "A fost găsită o coloană propusă spre normalizare existențială",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     
                options,  
                options[0]); 
                
                if(optiuneaAleasa == 0)
                {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
                    
                    pst = sqlConn.prepareStatement("SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?;");
                    pst.setString(1,numeTabela);
                    pst.setString(2,numeColoana); 
                    rs = pst.executeQuery();
                    rs.next();
                    tipColoana = rs.getString(1);
                    
                    if(!tipColoana.equals("varchar")){
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
                        
                        pst = sqlConn.prepareStatement("UPDATE "+numeTabela+" SET "+numeColoana+" = 0 WHERE "+numeColoana+" IS NULL OR "+numeColoana+"='';");
                        pst.executeUpdate();
                    }
                    else
                    {   Class.forName("com.mysql.cj.jdbc.Driver");
                        sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);                    
                        pst = sqlConn.prepareStatement("UPDATE "+numeTabela+" set "+numeColoana+"='adaugat autmoat' WHERE "+numeColoana+" IS NULL OR "+numeColoana+"='';");
                        pst.executeUpdate();
                    }
                    
                    if(!tipColoana.equals("varchar")){
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
                        
                        pst = sqlConn.prepareStatement("ALTER TABLE "+numeTabela+" MODIFY "+numeColoana+" "+tipColoana+" NOT NULL;");
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(this,"Normalizare aplicata!");
                    }
                    else
                    {   Class.forName("com.mysql.cj.jdbc.Driver");
                        sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
                        
                        pst = sqlConn.prepareStatement("ALTER TABLE "+numeTabela+" MODIFY "+numeColoana+" "+tipColoana+"(255) NOT NULL;");
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(this,"Normalizare aplicata!");
                    }
                    
                    
                }
               
             }
           
           }
           
           upDateDB();
          
           
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
       
    }
    
    
    
   
    
    private void btnTest2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest2ActionPerformed
     try {
            normalizare_existenta();
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FereastraPrinciapala.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(FereastraPrinciapala.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTest2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Actori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Actori().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizeaza;
    private javax.swing.JButton btnAdauga;
    private javax.swing.JButton btnIesire;
    private javax.swing.JButton btnPrinteaza;
    private javax.swing.JButton btnReseteaza;
    private javax.swing.JButton btnSterge;
    private javax.swing.JButton btnTest1;
    private javax.swing.JButton btnTest2;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBiografieActor;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNumeActor;
    private javax.swing.JMenuItem miExit1;
    private javax.swing.JMenuItem miInapoi;
    private javax.swing.JMenuItem miMeniu;
    private javax.swing.JTextArea txtAreaBiografie;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNumeActor;
    // End of variables declaration//GEN-END:variables
}
