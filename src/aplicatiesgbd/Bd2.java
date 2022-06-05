
package aplicatiesgbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sorin
 */
public class Bd2 extends javax.swing.JFrame {

    private static final String parola = "Sorelul123(c++)";
    private static final String user = "root";
    private static final String dataConn = "jdbc:mysql://localhost:3306/mydb2_gresit";
    
    Connection sqlConn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int q ,i,id,deleteItem,columnCount;
    
    public Bd2() throws SQLException, ClassNotFoundException {
        initComponents();
        normalizare_chei_primare();
    }
    
    public void normalizare_chei_primare() throws SQLException, ClassNotFoundException{
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        sqlConn = (Connection) DriverManager.getConnection(dataConn,user,parola);
        pst = sqlConn.prepareStatement("SELECT TABLE_NAME,COLUMN_NAME FROM INFORMATION_SCHEMA.key_column_usage WHERE TABLE_SCHEMA = 'mydb2_gresit' AND CONSTRAINT_NAME='PRIMARY'");
        rs = pst.executeQuery();
            
        ResultSetMetaData stData = rs.getMetaData();
        columnCount = stData.getColumnCount();
        
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("Urmatoarele coloane din tablele au chei primare!");

        int contor = 0;
        Vector vectorasColoane = new Vector();
        Vector vectorasTabele = new Vector();
        Vector vectorasTipData = new Vector();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i+=1) {
                //if (i > 1) 
                    //System.out.print(",  ");
                String columnValue = rs.getString(i);
                //System.out.print(columnValue + " " + stData.getColumnName(i));
                contor++;
                if(contor%2==0){
                    vectorasColoane.add(columnValue+" ");
                }
                else
                    vectorasTabele.add(columnValue+" ");
 
            }
        //System.out.println("");
        }
        int lungimeVectori = vectorasColoane.size();
        String numeColoana;
        String numeTabela;
        String tipColoana;

        if(lungimeVectori>0){
           
            for(i=0;i<lungimeVectori;i++){
                
                numeTabela = vectorasTabele.get(i).toString();
                numeColoana = vectorasColoane.get(i).toString();
                pst = sqlConn.prepareStatement("SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ? AND TABLE_SCHEMA = 'mydb2_gresit';");
                pst.setString(1,numeTabela);
                pst.setString(2,numeColoana); 
                rs = pst.executeQuery();
                rs.next();
                tipColoana = rs.getString(1);
                vectorasTipData.add(tipColoana);
            }
        }
        
        for(i=0;i<lungimeVectori;i++){
            System.out.println(vectorasTabele.get(i).toString()+" "+vectorasColoane.get(i).toString()+" "+vectorasTipData.get(i).toString());
        }
        
        //---------------------------------------------------------------------------------Memorare Date------------------------------------------------------------------------------------
        
        Vector coloane = new Vector();
        Vector tabele = new Vector();
        Vector raspunsuri = new Vector();
        pst = sqlConn.prepareStatement("SELECT COLUMN_NAME,TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'mydb2_gresit';");
        
        rs = pst.executeQuery();
        
        ResultSetMetaData rsmd = rs.getMetaData();
        columnCount = rsmd.getColumnCount();
        contor=0;
        while(rs.next()){
            for(i=1;i<=columnCount;i++){
                contor++;
                if(contor%2 != 0){
                    coloane.add(rs.getString(i));
                    
                }
                else
                {
                    tabele.add(rs.getString(i));
                }
            }
                
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<String>(tabele);
        Vector tabele_fara_duplicate = new Vector();
        tabele_fara_duplicate.addAll(hashSet);
        //System.out.println(tabele_fara_duplicate.size());
        
        Vector tabele_fara_cheie = new Vector();
        
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("Urmatoarele tabele nu au cheie primara!");
        
        for(i=0;i<tabele_fara_duplicate.size();i++){    
            pst = sqlConn.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.key_column_usage WHERE TABLE_SCHEMA = 'mydb2_gresit' AND CONSTRAINT_NAME='PRIMARY' AND TABLE_NAME=?");
            pst.setString(1, tabele_fara_duplicate.get(i).toString());
            rs = pst.executeQuery();
            
            if(rs.next()==false)
            {   tabele_fara_cheie.add(tabele_fara_duplicate.get(i).toString());
                System.out.println("Tabela "+tabele_fara_duplicate.get(i).toString()+" nu are cheie primara!");
            }
            
        }
        
        String alfa;
        for(i=0;i<tabele_fara_cheie.size();i++){
            alfa = tabele_fara_cheie.get(i).toString();
            pst = sqlConn.prepareStatement("ALTER TABLE "+alfa+" ADD COLUMN PK"+alfa+" INT NOT NULL AUTO_INCREMENT PRIMARY KEY");
            pst.executeUpdate();
        }

        int nr_chei;
        String nume_tabela;
        String tip_cheie="";
        Vector TIP_CHEI = new Vector();
       // System.out.println(tabele_fara_duplicate.size());
       
       
       
        for(i=0;i<tabele_fara_duplicate.size();i++){
            nume_tabela = tabele_fara_duplicate.get(i).toString();
            pst = sqlConn.prepareStatement("SELECT COUNT(COLUMN_NAME) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'mydb2_gresit' AND  TABLE_NAME = ?  AND COLUMN_KEY = 'PRI';");
            pst.setString(1, nume_tabela);
            rs = pst.executeQuery();
            rs.next();
            nr_chei = rs.getInt(1);
            System.out.println("Tabela "+nume_tabela+" are "+nr_chei+" chei primare!\n");
            
            pst = sqlConn.prepareCall("SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'mydb2_gresit' AND  TABLE_NAME = ?  AND COLUMN_KEY = 'PRI';");
            pst.setString(1, nume_tabela);
            rs = pst.executeQuery();
            
            if(nr_chei == 1){
                rs.next();
                tip_cheie=rs.getString(1).toString();
            }
            else if(nr_chei >1){
                ResultSetMetaData mtdt3 = rs.getMetaData();
                columnCount = mtdt3.getColumnCount();
                
                while(rs.next()){
                    for(int j=1;j<columnCount;j++){
                        TIP_CHEI.add(rs.getString(j).toString());
                    }
                }
            }
            
            if(nr_chei == 1)
            {
                if(!tip_cheie.equals("int"))
                {
                    pst= sqlConn.prepareStatement("ALTER TABLE "+nume_tabela+" DROP PRIMARY KEY");
                    pst.executeUpdate();
                    
                     pst = sqlConn.prepareStatement("ALTER TABLE "+nume_tabela+" ADD COLUMN PK"+nume_tabela+" INT NOT NULL AUTO_INCREMENT PRIMARY KEY");
                     pst.executeUpdate();
                     
                     
                     
                }
            }
                  
            
        }
        

        
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


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
            java.util.logging.Logger.getLogger(Bd2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bd2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bd2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bd2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Bd2().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Bd2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Bd2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
