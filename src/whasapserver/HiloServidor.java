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
public void run(){
        try {
			ServerSocket servidor = new ServerSocket(5211);
	        while(true) {
	        	Socket conecta = servidor.accept();
	        	String ip;
	        	InetAddress dir;
	        	dir = conecta.getInetAddress();
	        	ip = dir.getHostAddress();
	        	DataInputStream recibe = new DataInputStream(conecta.getInputStream());
	        	String texto;
	        	texto=recibe.readUTF(); 
                        String[] partes = texto.split(",");
                        mensajeria.addRow(new Object[]{"Se ha enviado desde la ip: " + ip + " ,el mensaje < "});
                        mensajeria.addRow(new Object[]{texto + " > a: " + partes[1]} );                       
                        try {
				Socket conecta2 = new Socket(partes[1],5211);
				DataOutputStream manda = new DataOutputStream(conecta2.getOutputStream());
				manda.writeUTF(partes[0]);
				manda.close();
                                conecta2.close();
                                
                                Socket conecta3 = new Socket(ip,5211);
				DataOutputStream manda2 = new DataOutputStream(conecta3.getOutputStream());
				manda2.writeUTF(partes[0]);
				manda2.close();
                                conecta3.close();
			} catch (UnknownHostException e0) {
				// TODO Auto-generated catch block
				e0.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
                     
	        	conecta.close();
	        	
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
    

}