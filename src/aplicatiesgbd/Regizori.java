
package aplicatiesgbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sorin
 */
public class Regizori extends javax.swing.JFrame {

    private static final String parola = "Sorelul123(c++)";
    private static final String user = "root";
    private static final String dataConn = "jdbc:mysql://localhost:3306/mydb";
    
    Connection sqlConn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int q ,i,id,deleteItem,columnCount;
    
    
   
    public Regizori() {
        initComponents();
    }
public void upDateDB() throws SQLException, ClassNotFoundException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
            pst = sqlConn.prepareStatement("Select * from regizori");
            
            rs = pst.executeQuery();
            ResultSetMetaData stData = rs.getMetaData();
            
            q = stData.getColumnCount();
            DefaultTableModel RecordTable;
            RecordTable = (DefaultTableModel)TableRegizori.getModel();
            RecordTable.setRowCount(0);
            
            while(rs.next()){
                Vector columnData = new Vector();
                
                for(i=1; i <= q; i++)
                {
                    columnData.add(rs.getInt("id"));
                    columnData.add(rs.getString("NumeRegizor"));
                    columnData.add(rs.getString("Biografie"));
                  
                }
                RecordTable.addRow(columnData);
                
            }
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
                
    }
    
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
    
    
    
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblNumeRegizor = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtNumeRegizor = new javax.swing.JTextField();
        lblBiografie = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaBiografie = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableRegizori = new javax.swing.JTable();
        btnAdauga = new javax.swing.JButton();
        btnActualizeaza = new javax.swing.JButton();
        btnPrinteaza = new javax.swing.JButton();
        btnReseteaza = new javax.swing.JButton();
        btnSterge = new javax.swing.JButton();
        btnTest1 = new javax.swing.JButton();
        btnTest2 = new javax.swing.JButton();
        btnIesire = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        miMeniu = new javax.swing.JMenuItem();
        miInapoi = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 204));
        jLabel1.setText("  REGIZORI");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblId.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblId.setText("ID:");

        lblNumeRegizor.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNumeRegizor.setText("NumeRegizor:");

        txtId.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        txtNumeRegizor.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtNumeRegizor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeRegizorActionPerformed(evt);
            }
        });

        lblBiografie.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblBiografie.setText("Biografie");

        txtAreaBiografie.setColumns(20);
        txtAreaBiografie.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtAreaBiografie.setRows(5);
        jScrollPane2.setViewportView(txtAreaBiografie);

        TableRegizori.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        TableRegizori.setModel(new javax.swing.table.DefaultTableModel(
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
                "id", "NumeRegizor", "Biografie"
            }
        ));
        jScrollPane1.setViewportView(TableRegizori);

        btnAdauga.setText("Insereaza");
        btnAdauga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdaugaActionPerformed(evt);
            }
        });

        btnActualizeaza.setText("Modifica");
        btnActualizeaza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizeazaActionPerformed(evt);
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

        btnIesire.setText("Iesire");
        btnIesire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIesireActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnIesire, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSterge, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnActualizeaza, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAdauga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPrinteaza, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReseteaza, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTest1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTest2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblNumeRegizor)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNumeRegizor))
                                    .addComponent(jScrollPane2)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(237, 237, 237)
                                .addComponent(lblBiografie)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(lblBiografie)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(btnAdauga, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblId)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNumeRegizor)
                                    .addComponent(txtNumeRegizor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                        .addComponent(btnIesire, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jMenu1.setText("File");

        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        jMenu1.add(miExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Navigheaza");

        miMeniu.setText("Meniu");
        miMeniu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMeniuActionPerformed(evt);
            }
        });
        jMenu2.add(miMeniu);

        miInapoi.setText("Inapoi");
        miInapoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miInapoiActionPerformed(evt);
            }
        });
        jMenu2.add(miInapoi);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumeRegizorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeRegizorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeRegizorActionPerformed

    private void btnAdaugaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdaugaActionPerformed
                try{
                        Class.forName("com.mysql.cj.jdbc.Driver");
            
                        sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
            
                        pst = sqlConn.prepareStatement("insert into regizori(id,NumeRegizor,Biografie) value (?,?,?)");
                        pst.setString(1,txtId.getText());
                        pst.setString(2,txtNumeRegizor.getText());
                        pst.setString(3,txtAreaBiografie.getText());
                        pst.executeUpdate();
            
                        JOptionPane.showMessageDialog(this,"Record Added");
                        upDateDB();
            
                    } catch (ClassNotFoundException ex) { 
            Logger.getLogger(Regizori.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Regizori.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_btnAdaugaActionPerformed

    private void btnActualizeazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizeazaActionPerformed

                try{
                        Class.forName("com.mysql.cj.jdbc.Driver");
            
                        sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
            
                        pst = sqlConn.prepareStatement("UPDATE regizori set NumeRegizor=?,Biografie=? WHERE id=?");
                        pst.setString(1,txtNumeRegizor.getText());
                        pst.setString(2,txtAreaBiografie.getText());
                        pst.setString(3,txtId.getText());
            
                        pst.executeUpdate();
            
                        JOptionPane.showMessageDialog(this,"Record Updated");
                        upDateDB();
            
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }//GEN-LAST:event_btnActualizeazaActionPerformed

    private void btnPrinteazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrinteazaActionPerformed
                try{
                        upDateDB();
                    }catch (ClassNotFoundException ex) {
                        Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }//GEN-LAST:event_btnPrinteazaActionPerformed

    private void btnReseteazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReseteazaActionPerformed
                txtId.setText("");
                txtNumeRegizor.setText("");
                txtAreaBiografie.setText("");
               
    }//GEN-LAST:event_btnReseteazaActionPerformed

    private void btnStergeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStergeActionPerformed
                DefaultTableModel RecordTable = (DefaultTableModel)TableRegizori.getModel();
                int SelectedRows = TableRegizori.getSelectedRow();
        
                try{
                        id = Integer.parseInt(RecordTable.getValueAt(SelectedRows, 0).toString());
            
                        deleteItem = JOptionPane.showConfirmDialog(null, "Sigur doriti sa faceti aceasta stergere ?","Warning",JOptionPane.YES_NO_OPTION);
                        if(deleteItem == JOptionPane.YES_OPTION)
                        {
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                sqlConn = DriverManager.getConnection(dataConn,user,parola);
                                pst = sqlConn.prepareStatement("delete from filme where id =?");
                                pst.setInt(1, id);
                                pst.executeUpdate();
                                JOptionPane.showMessageDialog(this,"Record updated");
                                upDateDB();
                
                                txtId.setText("");
                                txtNumeRegizor.setText("");
                                txtAreaBiografie.setText("");

                            }
                    }catch (ClassNotFoundException ex) {
                        Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }//GEN-LAST:event_btnStergeActionPerformed
      JFrame frame;
    private void btnIesireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIesireActionPerformed
                frame = new JFrame("Exit");
                if(JOptionPane.showConfirmDialog(frame, "Sunteti sigur ca doriti sa inchideti aplicatia?","MySQL",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                        System.exit(0);
                    }
    }//GEN-LAST:event_btnIesireActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        dispose();
    }//GEN-LAST:event_miExitActionPerformed

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

    private void btnTest1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest1ActionPerformed
        try {
            getNullable();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Regizori.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Regizori.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTest1ActionPerformed

    private void btnTest2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest2ActionPerformed
        try {
            normalizare_existenta();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Regizori.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Regizori.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTest2ActionPerformed

   
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
            java.util.logging.Logger.getLogger(Regizori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Regizori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Regizori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Regizori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Regizori().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableRegizori;
    private javax.swing.JButton btnActualizeaza;
    private javax.swing.JButton btnAdauga;
    private javax.swing.JButton btnIesire;
    private javax.swing.JButton btnPrinteaza;
    private javax.swing.JButton btnReseteaza;
    private javax.swing.JButton btnSterge;
    private javax.swing.JButton btnTest1;
    private javax.swing.JButton btnTest2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBiografie;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNumeRegizor;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miInapoi;
    private javax.swing.JMenuItem miMeniu;
    private javax.swing.JTextArea txtAreaBiografie;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNumeRegizor;
    // End of variables declaration//GEN-END:variables
}
