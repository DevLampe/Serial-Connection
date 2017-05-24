/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communicator;
import gnu.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
/**
 *
 * @author mathi
 */
public class Communicator extends javax.swing.JFrame implements SerialPortEventListener {
    //for containing the ports that will be found
    private Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";
    File file;
    Path path;
    JFileChooser chooser;
    int teller = 0;
    Calendar timer1, timer2;
    public Communicator() {
        initComponents();
        
    }
     public boolean initIOStream()
    {
        //return value for whether opening the streams is successful or not
        boolean successful = false;

        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            //writeData("StartStartS");

            successful = true;
            Status2TXT.setText("Succes");
            return successful;
        }
        catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            return successful;
        }
    }
     public Calendar timer(){
         Calendar cal = Calendar.getInstance();
         return cal;
     }
     
     public void initListener()
    {
        try
        {
            serialPort.addEventListener((SerialPortEventListener) this);
            serialPort.notifyOnDataAvailable(true);
            
            Status3TXT.setText("Succes");
        }
        catch (TooManyListenersException e)
        {
            logText = "Too many listeners. (" + e.toString() + ")";
            Status3TXT.setText("Too many listeners");
        }
    }
     
     public void writeData(String src)
    {
        try
        {
            byte[] message = src.getBytes(StandardCharsets.UTF_8);
            SerialReadTXT.append(Arrays.toString(message) + "\n");
            String encoded = Base64.getEncoder().encodeToString(message);
            SerialReadTXT.append(encoded + "\n");
            byte[] decoded = Base64.getDecoder().decode(encoded);
            jProgressBar1.setMaximum(decoded.length);
            for (int i = 0; i<decoded.length;){
                SerialReadTXT.append("Starting to write\n");
                output.write(decoded[i]);
                SerialReadTXT.append("Written \n");
                SerialReadTXT.append(i + "\n");
                i = i+1;
                jProgressBar1.setValue(i);
                TimeUnit.MILLISECONDS.sleep(50);
                jPanel1.updateUI();
            }
        }
        catch (Exception e)
        {
            logText = "Failed to write data. (" + e.toString() + ")";
            SerialReadTXT.append("Failed to write \n");
        } 
    }
     
     public void choosefile(){
         chooser = new JFileChooser(); 
    chooser.setCurrentDirectory(new java.io.File("."));
    chooser.setDialogTitle("Select File");
    chooser.setAcceptAllFileFilterUsed(true);
    //    
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
        file = chooser.getSelectedFile();
        StatusTXT.setText(file.toString());
        path = file.toPath();
        Status2TXT.setText(path.toString());
        filereader(path);
      }
    else {
      System.out.println("No Selection ");
      }
     }
     
     public void filereader(Path file){
        //Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                writeData(line);
            }
        } 
        catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
     }
     
     public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                byte singleData = (byte)input.read();
                timer2 = timer();
                if (singleData != NEW_LINE_ASCII)
                {
                    logText = new String(new byte[] {singleData});
                    SerialReadTXT.append(logText);
                }
                else
                {
                    SerialReadTXT.append(teller + "\n");
                    teller= 1+teller;
                }
                
            }
            catch (Exception e)
            {
                logText = "Failed to read data. (" + e.toString() + ")";
                StatusTXT.setText("Failed to read");
            }
        }
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
        ConnectBTN = new javax.swing.JButton();
        PORTS = new javax.swing.JComboBox<>();
        DiscoverCOMBTN = new javax.swing.JButton();
        DisconnectCOMBTN = new javax.swing.JButton();
        StatusTXT = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        SerialReadTXT = new javax.swing.JTextArea();
        Status2TXT = new javax.swing.JTextField();
        Status3TXT = new javax.swing.JTextField();
        SendBTN = new javax.swing.JButton();
        SendTXT = new javax.swing.JTextField();
        ChooseFileBTN = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ConnectBTN.setText("Connect");
        ConnectBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectBTNActionPerformed(evt);
            }
        });

        DiscoverCOMBTN.setText("Discover COM");
        DiscoverCOMBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DiscoverCOMBTNActionPerformed(evt);
            }
        });

        DisconnectCOMBTN.setText("Disconnect");
        DisconnectCOMBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DisconnectCOMBTNActionPerformed(evt);
            }
        });

        SerialReadTXT.setColumns(20);
        SerialReadTXT.setRows(5);
        jScrollPane1.setViewportView(SerialReadTXT);

        SendBTN.setText("Send Data");
        SendBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendBTNActionPerformed(evt);
            }
        });

        ChooseFileBTN.setText("Choose File");
        ChooseFileBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseFileBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(DiscoverCOMBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ConnectBTN)
                        .addComponent(DisconnectCOMBTN)
                        .addComponent(SendBTN)
                        .addComponent(SendTXT))
                    .addComponent(ChooseFileBTN))
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Status3TXT)
                        .addComponent(PORTS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(StatusTXT, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                        .addComponent(Status2TXT))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PORTS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DiscoverCOMBTN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ConnectBTN)
                    .addComponent(StatusTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DisconnectCOMBTN)
                    .addComponent(Status2TXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Status3TXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SendBTN))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(71, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChooseFileBTN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(SendTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(166, 166, 166))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
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

    private void ConnectBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectBTNActionPerformed
        String selectedPort = (String)PORTS.getSelectedItem();
        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

        CommPort commPort = null;

        try
        {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("Tracker", TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
           // serialPort.setBaudBase(115200);
            //logging
            logText = selectedPort + " opened successfully.";
            StatusTXT.setText("Connection");
        }
        catch (PortInUseException e)
        {
            logText = selectedPort + " is in use. (" + e.toString() + ")";
            StatusTXT.setText("PORT in Use");
        }
        catch (Exception e)
        {
            logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
            StatusTXT.setText("Failed to open");
        }
         
        initIOStream();
        initListener();
    }//GEN-LAST:event_ConnectBTNActionPerformed

    private void DiscoverCOMBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DiscoverCOMBTNActionPerformed
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                PORTS.addItem(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
    }//GEN-LAST:event_DiscoverCOMBTNActionPerformed

    private void DisconnectCOMBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisconnectCOMBTNActionPerformed
 //close the serial port
        try
        {
            //writeData("CloseCloseC");

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();

            logText = "Disconnected.";
            StatusTXT.setText("Closed");
            Status2TXT.setText("");
            Status3TXT.setText("");
        }
        catch (Exception e)
        {
            logText = "Failed to close " + serialPort.getName()
                              + "(" + e.toString() + ")";
            StatusTXT.setText("Failed to close");
        }
    }//GEN-LAST:event_DisconnectCOMBTNActionPerformed

    private void SendBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendBTNActionPerformed
        writeData(SendTXT.getText());
        SendTXT.setText("");
    }//GEN-LAST:event_SendBTNActionPerformed

    private void ChooseFileBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseFileBTNActionPerformed
       choosefile();
    }//GEN-LAST:event_ChooseFileBTNActionPerformed

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
            java.util.logging.Logger.getLogger(Communicator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Communicator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Communicator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Communicator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Communicator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ChooseFileBTN;
    private javax.swing.JButton ConnectBTN;
    private javax.swing.JButton DisconnectCOMBTN;
    private javax.swing.JButton DiscoverCOMBTN;
    private javax.swing.JComboBox<String> PORTS;
    private javax.swing.JButton SendBTN;
    private javax.swing.JTextField SendTXT;
    private javax.swing.JTextArea SerialReadTXT;
    private javax.swing.JTextField Status2TXT;
    private javax.swing.JTextField Status3TXT;
    private javax.swing.JTextField StatusTXT;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
