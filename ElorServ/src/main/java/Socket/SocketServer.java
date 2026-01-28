package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import Modelo.Metodos;

@Component
public class SocketServer {

	@Autowired
	private Metodos metodos;
	
	public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(6767)) {

            while (true) {
                Socket socket = serverSocket.accept();
                new HiloServidor(socket, metodos).start(); 
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
