
package aplicatiesgbd;

import java.awt.Component;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author sorin
 */
public class FereastraPrinciapala extends javax.swing.JFrame{
    //======================================================================Initializari=======================================================================        
    private static final String parola = "Sorelul123(c++)";
    private static final String user = "root";
    private static final String dataConn = "jdbc:mysql://localhost:3306/mydb";
    
    Connection sqlConn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int q ,i,id,deleteItem,columnCount;
            
    public FereastraPrinciapala() {
        initComponents();
    }

    //======================================================================Functii=====================================================================================

    public void upDateDB() throws SQLException, ClassNotFoundException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
            pst = sqlConn.prepareStatement("Select * from filme");
            
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
                    columnData.add(rs.getString("Titlu"));
                    columnData.add(rs.getString("An"));
                    columnData.add(rs.getString("Certificare"));
                    columnData.add(rs.getInt("LungimeFilm"));
                    columnData.add(rs.getFloat("Rating_IMDB"));
                    columnData.add(rs.getString("Descriere"));
                    columnData.add(rs.getInt("Voturi"));
                }
                RecordTable.addRow(columnData);
                
            }
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
                
    }
    
    
    public void getConstrangeri() throws ClassNotFoundException, SQLException{
          try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
            
            pst = sqlConn.prepareStatement("SELECT TABLE_NAME, constraint_name FROM information_schema.TABLE_CONSTRAINTS WHERE constraint_schema = 'mydb' and table_name='Actori'");
            rs = pst.executeQuery();
            
            ResultSetMetaData stData = rs.getMetaData();
            columnCount = stData.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue + " ");
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
    

   //======================================================================Butoane=====================================================================================
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnActualizeaza = new javax.swing.JButton();
        btnPrinteaza = new javax.swing.JButton();
        btnAdauga = new javax.swing.JButton();
        btnIesire = new javax.swing.JButton();
        lblAn = new javax.swing.JLabel();
        lblCertificare = new javax.swing.JLabel();
        lblTitlu = new javax.swing.JLabel();
        lblLungimeFilm = new javax.swing.JLabel();
        lblVoturi = new javax.swing.JLabel();
        lblDescriere = new javax.swing.JLabel();
        lblRating = new javax.swing.JLabel();
        txtAn = new javax.swing.JTextField();
        txtTitlu = new javax.swing.JTextField();
        txtLungimeFilm = new javax.swing.JTextField();
        txtRating = new javax.swing.JTextField();
        txtVoturi = new javax.swing.JTextField();
        txtCertificare = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescriere = new javax.swing.JTextArea();
        txtId = new javax.swing.JTextField();
        lblId = new javax.swing.JLabel();
        btnReseteaza = new javax.swing.JButton();
        btnSterge = new javax.swing.JButton();
        btnTest2 = new javax.swing.JButton();
        btnTest3 = new javax.swing.JButton();
        btnTest1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        miMeniu = new javax.swing.JMenuItem();
        miInapoi = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel1FocusGained(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Titlu", "An", "Certificare", "LungimeFilm", "Rating_IMDB", "Descriere", "Voturi"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

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

        btnAdauga.setText("Insereaza");
        btnAdauga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdaugaActionPerformed(evt);
            }
        });

        btnIesire.setText("Iesire");
        btnIesire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIesireActionPerformed(evt);
            }
        });

        lblAn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblAn.setText("An:");

        lblCertificare.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblCertificare.setText("Certificare:");

        lblTitlu.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblTitlu.setText("Titlu:");

        lblLungimeFilm.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblLungimeFilm.setText("LungimeFilm:");

        lblVoturi.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblVoturi.setText("Voturi:");

        lblDescriere.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblDescriere.setText("Descriere:");

        lblRating.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblRating.setText("Rating IMDB:");

        txtDescriere.setColumns(20);
        txtDescriere.setRows(5);
        jScrollPane2.setViewportView(txtDescriere);

        lblId.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblId.setText("ID:");

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

        btnTest2.setText("test2");
        btnTest2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTest2ActionPerformed(evt);
            }
        });

        btnTest3.setText("test3");
        btnTest3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTest3ActionPerformed(evt);
            }
        });

        btnTest1.setText("test1");
        btnTest1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTest1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCertificare)
                            .addComponent(lblAn)
                            .addComponent(lblLungimeFilm)
                            .addComponent(lblTitlu))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtAn, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLungimeFilm, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTitlu, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCertificare, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblRating)
                                    .addComponent(lblVoturi))
                                .addGap(30, 30, 30))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblId)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRating, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(txtVoturi, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(txtId))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblDescriere)
                        .addGap(256, 256, 256))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAdauga, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnActualizeaza, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrinteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReseteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSterge, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnTest1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(158, 158, 158))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnTest2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnTest3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addComponent(btnIesire, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnTest1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIesire, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnAdauga, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnActualizeaza, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnPrinteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnReseteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSterge, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnTest2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTest3)))
                        .addGap(18, 18, 18)
                        .addComponent(lblDescriere)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTitlu)
                            .addComponent(txtTitlu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCertificare)
                            .addComponent(txtCertificare, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblLungimeFilm)
                            .addComponent(txtLungimeFilm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRating, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRating))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblVoturi)
                            .addComponent(txtVoturi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblId))))
                .addGap(0, 25, Short.MAX_VALUE))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdaugaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdaugaActionPerformed
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
           
            

            pst = sqlConn.prepareStatement("insert into filme(id,Titlu,An,Certificare,LungimeFilm,Rating_IMDB,Descriere,Voturi) value (?,?,?,?,?,?,?,?)");
            pst.setString(1,txtId.getText());  
            pst.setString(2,txtTitlu.getText()); 
            pst.setString(3,txtAn.getText()); 
            pst.setString(4,txtCertificare.getText()); 
            pst.setString(5,txtLungimeFilm.getText()); 
            pst.setString(6,txtRating.getText()); 
            pst.setString(7,txtDescriere.getText());
            pst.setString(8,txtVoturi.getText());
            
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(this,"Record Added");
            upDateDB();
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnAdaugaActionPerformed

    private void btnActualizeazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizeazaActionPerformed
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
           
            

            pst = sqlConn.prepareStatement("UPDATE filme set Titlu=?,An=?,Certificare=?,LungimeFilm=?,Rating_IMDB=?,Descriere=?,Voturi=? WHERE id=?");
            pst.setString(1,txtTitlu.getText()); 
            pst.setString(2,txtAn.getText()); 
            pst.setString(3,txtCertificare.getText()); 
            pst.setString(4,txtLungimeFilm.getText()); 
            pst.setString(5,txtRating.getText()); 
            pst.setString(6,txtDescriere.getText());
            pst.setString(7,txtVoturi.getText());
            pst.setString(8,txtId.getText());  
            
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(this,"Record Updated");
            upDateDB();
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnActualizeazaActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

            DefaultTableModel RecordTable;
            
            RecordTable = (DefaultTableModel)jTable1.getModel();
            int SelectedRows = jTable1.getSelectedRow();
            
            txtId.setText(RecordTable.getValueAt(SelectedRows, 0).toString());
            txtTitlu.setText(RecordTable.getValueAt(SelectedRows, 1).toString());
            txtAn.setText(RecordTable.getValueAt(SelectedRows, 2).toString());
            txtCertificare.setText(RecordTable.getValueAt(SelectedRows, 3).toString());
            txtLungimeFilm.setText(RecordTable.getValueAt(SelectedRows, 4).toString());
            txtRating.setText(RecordTable.getValueAt(SelectedRows, 5).toString());
            txtDescriere.setText(RecordTable.getValueAt(SelectedRows, 6).toString());
            txtVoturi.setText(RecordTable.getValueAt(SelectedRows,7).toString());
    }//GEN-LAST:event_jTable1MouseClicked
    
    private void btnStergeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStergeActionPerformed
        DefaultTableModel RecordTable = (DefaultTableModel)jTable1.getModel();
        int SelectedRows = jTable1.getSelectedRow();
        
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
            txtTitlu.setText("");
            txtAn.setText("");
            txtCertificare.setText("");
            txtLungimeFilm.setText("");
            txtRating.setText("");
            txtDescriere.setText("");
            txtVoturi.setText("");
            }
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnStergeActionPerformed
    private JFrame frame;
    private void btnIesireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIesireActionPerformed
                frame = new JFrame("Exit");
        if(JOptionPane.showConfirmDialog(frame, "Sunteti sigur ca doriti sa inchideti aplicatia?","MySQL",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_btnIesireActionPerformed

    private void btnReseteazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReseteazaActionPerformed
        txtId.setText("");
        txtTitlu.setText("");
        txtAn.setText("");
        txtCertificare.setText("");
        txtLungimeFilm.setText("");
        txtRating.setText("");
        txtDescriere.setText("");
        txtVoturi.setText("");
    }//GEN-LAST:event_btnReseteazaActionPerformed

    private void jPanel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusGained
       
        
    }//GEN-LAST:event_jPanel1FocusGained

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered

    }//GEN-LAST:event_jTable1MouseEntered

    private void btnPrinteazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrinteazaActionPerformed
                try{
            upDateDB();
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPrinteazaActionPerformed

    private void btnTest2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest2ActionPerformed
//        try {
//            normalizare_chei_primare();
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_btnTest2ActionPerformed

    private void btnTest3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest3ActionPerformed
        try {
            normalizare_existenta();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTest3ActionPerformed

    private void btnTest1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTest1ActionPerformed
        try {
            getNullable();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FereastraPrinciapala.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTest1ActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        dispose();
    }//GEN-LAST:event_miExitActionPerformed

    private void miInapoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miInapoiActionPerformed
        MyDB fr = new MyDB();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miInapoiActionPerformed

    private void miMeniuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMeniuActionPerformed
        Meniu fr = new Meniu();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMeniuActionPerformed

    //======================================================================Main=====================================================================================
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FereastraPrinciapala.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FereastraPrinciapala.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FereastraPrinciapala.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FereastraPrinciapala.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FereastraPrinciapala().setVisible(true);
                
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
    private javax.swing.JButton btnTest3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAn;
    private javax.swing.JLabel lblCertificare;
    private javax.swing.JLabel lblDescriere;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLungimeFilm;
    private javax.swing.JLabel lblRating;
    private javax.swing.JLabel lblTitlu;
    private javax.swing.JLabel lblVoturi;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miInapoi;
    private javax.swing.JMenuItem miMeniu;
    private javax.swing.JTextField txtAn;
    private javax.swing.JTextField txtCertificare;
    private javax.swing.JTextArea txtDescriere;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLungimeFilm;
    private javax.swing.JTextField txtRating;
    private javax.swing.JTextField txtTitlu;
    private javax.swing.JTextField txtVoturi;
    // End of variables declaration//GEN-END:variables

}
