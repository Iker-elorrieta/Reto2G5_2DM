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
        System.out.println("=== INICIANDO SOCKET SERVER ===");
        System.out.println("Metodos inyectado: " + (metodos != null ? "SÍ ✓" : "NO ✗"));
        
        try (ServerSocket serverSocket = new ServerSocket(6767)) {
            System.out.println("Servidor escuchando en puerto 6767");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + socket.getInetAddress());
                
                if (metodos == null) {
                    System.err.println("ERROR CRÍTICO: metodos es null!");
                } else {
                    System.out.println("Creando HiloServidor con metodos válido");
                    new HiloServidor(socket, metodos).start();
                }
            }

        } catch (IOException e) {
            System.err.println("Error en el servidor:");
            e.printStackTrace();
        }
    }
}