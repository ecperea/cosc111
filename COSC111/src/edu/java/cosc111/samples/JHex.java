package edu.java.cosc111.samples;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author RLVillacarlos
 */
public class JHex extends javax.swing.JFrame {
    private static final int BUFF_SIZE = 2 << 20 ;
    private static final int DEFAULT_WIDTH = 32;
    private static final int ASCII_DOT = 46;
    private static final int ASCII_SPACE = 32;
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    
    private int curViewWidth = DEFAULT_WIDTH;
    private int curColHex  = 0; 
    private int curColChar = 0; 
    /**
     * Creates new form JHex
     */
    public JHex() {
        initComponents();
        init();
    }
    
    private void init(){
        jtaASCII.setColumns(curViewWidth);
        jtaHex.setColumns(curViewWidth);
        jtaASCII.setText("");
        jtaHex.setText("");       
        curColHex  = 0; 
        curColChar = 0;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaASCII = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaHex = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JHex");

        jSplitPane1.setDividerLocation(200);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jtaASCII.setEditable(false);
        jtaASCII.setColumns(20);
        jtaASCII.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jtaASCII.setRows(5);
        jScrollPane1.setViewportView(jtaASCII);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.add(filler5, java.awt.BorderLayout.WEST);

        jLabel1.setText("ASCII View");
        jPanel3.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jtaHex.setEditable(false);
        jtaHex.setColumns(20);
        jtaHex.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jtaHex.setRows(5);
        jScrollPane2.setViewportView(jtaHex);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Hex View");
        jPanel4.add(jLabel2, java.awt.BorderLayout.CENTER);
        jPanel4.add(filler6, java.awt.BorderLayout.WEST);

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setRightComponent(jPanel2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);
        getContentPane().add(filler3, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(filler1, java.awt.BorderLayout.WEST);
        getContentPane().add(filler2, java.awt.BorderLayout.EAST);
        getContentPane().add(filler4, java.awt.BorderLayout.PAGE_END);

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Open");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        openFile();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(JHex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JHex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JHex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JHex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JHex().setVisible(true);
            }
        });
    }
    
    
    
    private void openFile(){
        JFileChooser jFileChoose = new JFileChooser();
        
        if(jFileChoose.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            Path p = jFileChoose.getSelectedFile().toPath();
            this.setTitle("JHex - Reading " + p.toString());
            try(InputStream in = new BufferedInputStream(
                                Files.newInputStream(p,StandardOpenOption.READ),BUFF_SIZE)){
                
                byte[] b = new byte[BUFF_SIZE];
                int readBytes = 0;
                init();
                while((readBytes = in.read(b))!=-1){
                    if(readBytes<b.length){
                        byte[] b_ = new byte[readBytes];
                        System.arraycopy(b, 0, b_, 0, readBytes);
                        addCharacterValues(b_);
                        addHexValues(b_);
                    }else{                        
                        addCharacterValues(b);
                        addHexValues(b);
                    }
                }
                jtaASCII.setCaretPosition(0);
                jtaHex.setCaretPosition(0);
                this.setTitle("JHex - " + p.toString());
            }catch(IOException ex){                
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error Reading File",JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }
    
    private void addHexValues(byte[] dataBytes){  
        StringBuilder s = new StringBuilder(dataBytes.length*3);
        
        for(int i =0;i<dataBytes.length;i++){  
            s.append(byteToHex(dataBytes[i]));
            curColHex = (curColHex + 1) % curViewWidth;            
            if(curColHex==0){
                s.append("\n");
            }else{
                s.append(" ");                
            }
        }
        jtaHex.append(s.toString());
    }
    
    //Modified version of http://stackoverflow.com/a/9855338/4042112   
    public static String byteToHex(byte b) {
        char[] hexChars = new char[2];
        int v = b & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
        return new String(hexChars);
    }
    
    private void addCharacterValues(byte[] dataBytes) throws UnsupportedEncodingException{   
        StringBuilder s = new StringBuilder(dataBytes.length);        
        byte[] bytes = new byte[dataBytes.length];
        
        for(int i =0;i<dataBytes.length;i++){
            int n = (int)dataBytes[i] & 0xFF;
            
            if(n <ASCII_SPACE){ //If character is non-printable
                bytes[i] = ASCII_DOT;
            }else{
                bytes[i] = dataBytes[i];
            }
        }
        String toAppend = new String(bytes,StandardCharsets.US_ASCII);
        int pos = 0;
        int len = toAppend.length();
        while(pos<len){
            int lenToRead = curViewWidth - curColChar;            
            int posEnd = pos + lenToRead;
            
            if(posEnd>len){
                lenToRead = toAppend.length() - pos;
                posEnd = pos + lenToRead;
            }
            
            s.append(toAppend.substring(pos, posEnd));
            curColChar = (curColChar + lenToRead)  % curViewWidth;                
            pos += lenToRead;
            if(curColChar==0){
                s.append("\n");
            }
        }
        jtaASCII.append(s.toString());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jtaASCII;
    private javax.swing.JTextArea jtaHex;
    // End of variables declaration//GEN-END:variables
}
