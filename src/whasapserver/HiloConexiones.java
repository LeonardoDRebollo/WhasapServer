package whasapserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author djclu
 */
public class HiloConexiones extends Thread {

    private DefaultTableModel Conexion;
    private DefaultTableModel mensajeria;

    public HiloConexiones(DefaultTableModel conectadoModel, DefaultTableModel mensajeModel) {
        this.Conexion = conectadoModel;
        this.mensajeria = mensajeModel;
    }

    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(5212);
            while (true) {
                Socket conexion = servidor.accept();
                String ip;
                InetAddress dir;
                dir = conexion.getInetAddress();
                ip = dir.getHostAddress();
                DataInputStream recibe = new DataInputStream(conexion.getInputStream());
                String texto;
                texto = recibe.readUTF();
                String[] partes = texto.split(",");
                String desconectado = "";
                if (partes.length > 1) {
                    desconectado = partes[1].trim();
                    System.out.println(desconectado);
                }
                if (!desconectado.equals("se ha desconectado")) {
                    Conexion.addRow(new Object[]{texto, ip});
                    mensajeria.addRow(new Object[]{texto + " ha entrado al chat"});

                    try {
                        for (int fila1 = 0; fila1 < Conexion.getRowCount(); fila1++) {
        Object dato1 = Conexion.getValueAt(fila1, 1);
        
        for (int fila2 = 0; fila2 < Conexion.getRowCount(); fila2++) {
         Socket conexion1 = new Socket(dato1.toString(), 5213);
         DataOutputStream envia1 = new DataOutputStream(conexion1.getOutputStream());
                Object dato3 = Conexion.getValueAt(fila1, 0);
                Object dato2 = Conexion.getValueAt(fila2, 1);
                String envio = dato3.toString() + "," + dato2.toString();
                envia1.writeUTF(envio);   
                envia1.close();
                conexion1.close();
                }
              }
            } catch (IOException e) {
                e.printStackTrace();
            }                              
                } else {
                    mensajeria.addRow(new Object[]{partes[0] + " se salió del chat"});
                }
                if (desconectado.equals("se ha desconectado")) {
                                       
         for (int fila = 0; fila < Conexion.getRowCount(); fila++) {
         Object us = Conexion.getValueAt(fila, 1);
             try (Socket conexion6 = new Socket(us.toString(), 5213)) {
                 DataOutputStream envia6 = new DataOutputStream(conexion6.getOutputStream());
                 envia6.writeUTF(texto);
                 envia6.close();
                 conexion6.close();
             }        
    }                
                    for (int fila = 0; fila < Conexion.getRowCount(); fila++) {
                        Object valorColumnaDos = Conexion.getValueAt(fila, 1);
                        if (valorColumnaDos != null && valorColumnaDos.toString().equals(ip)) {
                            Conexion.removeRow(fila);
                            break;
                        }
                    }
                }
                conexion.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}