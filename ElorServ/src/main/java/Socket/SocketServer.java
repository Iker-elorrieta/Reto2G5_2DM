package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	public static void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(6767)) {
            System.out.println("Servidor soket eskuchando en 6767" );

            while (true) {
                Socket socket = serverSocket.accept();
                new HiloServidor(socket).start(); 
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
