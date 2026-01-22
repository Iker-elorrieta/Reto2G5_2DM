package Modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketCliente {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SocketCliente() {
        try {
            socket = new Socket("localhost", 6767);

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // MUY IMPORTANTE pe
            in  = new ObjectInputStream(socket.getInputStream());

            System.out.println("Konektado al servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void cerrarConexion() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

