/**
 *
 * @author Russel
 */

package edu.java.cosc111.samples.net;

import edu.java.cosc111.samples.lib.UpdatableBinaryMinHeapPQ;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class DesktopViewer extends javax.swing.JFrame {
    private static final int MAX_AGE = 1000;  
    private final Thread reciever;
    
    /**
     * Creates new form DesktopViewer
     * @param host
     * @throws java.net.UnknownHostException
     */
    public DesktopViewer(String host) throws UnknownHostException {
        initComponents();        
        reciever = new Thread(new Receiver(host));
//        presenter = new Thread(new Presenter());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jPreview = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DesktopViewer");

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(jPreview);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        setBounds(0, 0, 416, 338);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     * @throws java.net.UnknownHostException
     */
    public static void main(String args[]) throws UnknownHostException {
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
        //</editor-fold>
        
        Scanner sin = new Scanner(System.in);
        System.out.print("Host: ");
        String host = sin.nextLine();
        DesktopViewer view = new DesktopViewer(host);
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                view.setVisible(true);
                view.reciever.start();
//                view.presenter.start();
            }
        });        
    }
    private class Receiver implements Runnable{       
        private static final int DATA_SIZE = 32768;
        private static final int BUFF_SIZE = DATA_SIZE + FrameBlock.HEADER_SIZE;
        private static final int DEFAULT_PORT = 1024;
        private static final String BROADCAST_ADDR = "224.0.1.0";
        
        private InetAddress oHost;
        private DatagramSocket socket;
        private final Map<Integer,Map<Integer,FrameBlock>> frames = new HashMap<>();
        private final UpdatableBinaryMinHeapPQ<Integer,Long> frameAge = new UpdatableBinaryMinHeapPQ<>();
        private final ByteArrayOutputStream buffImg = new ByteArrayOutputStream();        
        private final InetAddress host;
        

        public Receiver(String host_) throws UnknownHostException{
            host = InetAddress.getByName(host_);
        }
                
        
        @Override
        public void run(){
            try {
                oHost = InetAddress.getByName(BROADCAST_ADDR);
                socket = new MulticastSocket(DEFAULT_PORT);
//                socket.setBroadcast(true);
//                socket.joinGroup(oHost);
                while(true){
                    try {                
                        DatagramPacket dMsg = new DatagramPacket(new byte[BUFF_SIZE],BUFF_SIZE);
                        socket.receive(dMsg);
                        FrameBlock b = new FrameBlock(dMsg.getData()); 
                        int frame = b.frame;
                        if(frames.containsKey(frame)){
                            frameAge.updatePriority(frame, System.currentTimeMillis());
                            Map<Integer,FrameBlock> block = frames.get(frame);
                            block.put(b.index, b);
                        }else{
                            frameAge.insert(frame,System.currentTimeMillis());
                            HashMap<Integer,FrameBlock> block = new HashMap<>();
                            block.put(b.index, b);
                            frames.put(b.frame,block);
                        }                        
//                        if(dMsg.getAddress().equals(host)){
                            cleanUp();
                            createImage(frame);                
//                        }                
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }                }                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            finally{
//                try {
//                    socket.leaveGroup(oHost);
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
            }
        }
        
        public void cleanUp() {
            while(frameAge.size()>0){
//                System.out.println(age);
                int min = frameAge.getMin();
                if(System.currentTimeMillis() - frameAge.getPriority(min)<=MAX_AGE){
                    break;
                }
                frameAge.removeMin();
                frames.remove(min);
            }            
        }
        
        public void createImage(int frame){
            Map<Integer,FrameBlock> block = frames.get(frame);
            if(block.containsKey(0) && block.size()-1 == block.get(0).size){                    
                for(int i =1;i<block.size();i++){
                    FrameBlock d = block.get(i);
                    try {
                        buffImg.write(d.data);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                try {
//                    try {
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(buffImg.toByteArray()));                        
                    if(img!=null){
                        jPreview.setIcon(new ImageIcon(img));
//                            images.offer(img, MAX_WAIT, TimeUnit.MILLISECONDS);
                    }                        
//                    } catch (InterruptedException ex) {}                                            
                    buffImg.reset();
                    frames.remove(frame); 
                    frameAge.updatePriority(frame, Long.MIN_VALUE);
                    frameAge.removeMin();
//                        age.remove(frame);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
//    private class Presenter implements Runnable{
//        @Override
//        public void run() {
//            while(true){
//                try {
//                    BufferedImage img = images.poll(MAX_WAIT, TimeUnit.MILLISECONDS);
//                    if(img!=null){
//                        jPreview.setIcon(new ImageIcon(img));
//                    }
//                } catch (InterruptedException ex) {}                
//            }
//        }
//        
//    }
    
    private static class FrameBlock implements Comparable<FrameBlock>{
        private static final int HEADER_SIZE = 12;
        private static final int FRAME_START = 0;
        private static final int INDEX_START = 4;
        private static final int SIZE_START = 8;
        
        private final Integer frame;
        private final Integer index;
        private final Integer size;
        private final byte[]  data;

        public FrameBlock(byte[] raw) {
            if(raw!=null && raw.length>0){
                frame = bytesToInt(raw, FRAME_START);
                index = bytesToInt(raw, INDEX_START);
                size = bytesToInt(raw, SIZE_START);
                data = new byte[size];
                System.arraycopy(raw, HEADER_SIZE, data, 0, size);
            }else{
                throw new IllegalArgumentException();
            }
        }
        
        private int bytesToInt(byte[] b, int start){
            int n = ((b[start] & 0xff)<<24) + 
                    ((b[start + 1] & 0xff)<<16) + 
                    ((b[start + 2] & 0xff)<<8) +
                     (b[start + 3] & 0xff);
            return n;
        }
        
        @Override
        public int compareTo(FrameBlock o) {
            return this.index.compareTo(o.index);
        }        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jPreview;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
