/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.java.cosc111.samples.presenter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Russel
 */
public class DesktopViewer extends javax.swing.JFrame {

    /**
     * Creates new form DesktopViewer
     */
    public DesktopViewer() {
        initComponents();        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPreview = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPreview, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(DesktopViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DesktopViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DesktopViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DesktopViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        Scanner sin = new Scanner(System.in);
        System.out.print("Host: ");
        String host = sin.nextLine();
        DesktopViewer view = new DesktopViewer();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                view.setVisible(true);
            }
        });
        new Thread(new Receiver(view.jPreview)).start();

    }

    private static class Receiver implements Runnable{
        private static final int BUFF_SIZE = 1025;
        private static final int DEFAULT_PORT = 1024;
        private static final String BROADCAST_ADDR = "224.0.1.0";
        private ByteArrayOutputStream data = new ByteArrayOutputStream();
        private InetAddress oHost;
        private MulticastSocket socket;
        private JLabel target;
        
        public Receiver(JLabel target_){
            target = target_;
        }
        
        @Override
        public void run(){
            try {
                oHost = InetAddress.getByName(BROADCAST_ADDR);
                socket = new MulticastSocket(DEFAULT_PORT);
                socket.joinGroup(oHost);
                while(true){
                    listen();
                }                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        private void listen(){
            try {                
//                System.out.println("Listening...");
                DatagramPacket dMsg = new DatagramPacket(new byte[BUFF_SIZE],BUFF_SIZE);
                socket.receive(dMsg);
                int dataSize = byteToInt(dMsg.getData()[1]) + 
                                byteToInt(dMsg.getData()[2]) + 
                                byteToInt(dMsg.getData()[3]) + 
                                byteToInt(dMsg.getData()[4]);
                int index = byteToInt(dMsg.getData()[0]);
                if(index == 0){
                    try{
                        BufferedImage img =ImageIO.read(new ByteArrayInputStream(data.toByteArray()));
                        data = new ByteArrayOutputStream();
                        if(img!=null){
                            target.setIcon(new ImageIcon(img));
                            //target.repaint();      
                            System.out.println("update");
                        }
                    }catch(IOException ex){
                        System.err.println(ex.getMessage());
                    }
                }else{
                    data.write(dMsg.getData(), 5, dataSize);
//                    System.out.println(index+ ": " + dataSize);
                }
//                System.out.println("Host: " + index  + ": "  
//                                            + dataSize);
             } catch (IOException ex) {
                    ex.printStackTrace();
            }                
        }
        
        private int byteToInt(byte b){
            int n = (int)b;
            if(n<0){
                n += 256;
            }
            return n;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jPreview;
    // End of variables declaration//GEN-END:variables
}
