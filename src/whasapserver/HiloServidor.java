package whasapserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author djclu
 */
public class HiloServidor extends Thread {

    private DefaultTableModel Conexion;
    private DefaultTableModel mensajeria;

    public HiloServidor(DefaultTableModel conexion, DefaultTableModel mensajerias) {
        this.Conexion = conexion;
        this.mensajeria = mensajerias;
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(5211);
            while (true) {
                Socket conexion = servidor.accept();
                String ip;
                InetAddress dir;
                dir = conexion.getInetAddress();
                ip = dir.getHostAddress();
                DataInputStream recibe = new DataInputStream(conexion.getInputStream());
                String texto;
                texto = recibe.readUTF();
                
                mensajeria.addRow(new Object[]{"Se ha enviado desde la ip: " + ip + " ,el mensaje < "});
                mensajeria.addRow(new Object[]{texto + " >"} );
                int columna = 1;
                try {
                    int filas = Conexion.getRowCount();
                    for (int fila = 0; fila < filas; fila++) {
                        Object dato = Conexion.getValueAt(fila, columna);
                        Socket conexion2 = new Socket(dato.toString(), 5211);
                        DataOutputStream envia2 = new DataOutputStream(conexion2.getOutputStream());
                        envia2.writeUTF(texto);
                        System.out.println("Mensaje enviado a: " + dato.toString());
                        envia2.close();
                        conexion2.close();
                    }
                } catch (UnknownHostException e0) {
                    // TODO Auto-generated catch block
                    e0.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                conexion.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}