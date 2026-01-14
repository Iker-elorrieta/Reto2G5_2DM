package Socket;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


import Modelo.Metodos;
import Modelo.Users;

public class HiloServidor extends Thread {

    private Metodos metodos = new Metodos();
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public HiloServidor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            while (estaConectado()) {
                Object obj = ois.readObject(); // BLOKEANTE

                if (obj == null) break;

                
               // Map<String, Object> mensaje = (Map<String, Object>) obj;

                String comando = (String) obj;

                switch (comando) {
                    case "LOGIN":
                    	
                        String username =(String)ois.readObject();
                        String password =(String) ois.readObject();

                        // hacemos login
                        Users u = metodos.login(username, password);

                        Map<String, Object> respuesta = new HashMap<>();
                        if (u != null) {
                            respuesta.put("estado", "OK");
                            respuesta.put("id", u.getId());
                            respuesta.put("username", u.getUsername());
                        } else {
                            respuesta.put("estado", "ERROR");
                            respuesta.put("mensaje", "login inválido");
                        }

                        oos.writeObject(respuesta);
                        oos.flush();
                        break;

                   

                    default:
                        System.out.println("Comando desconocido: " + comando);
                }
            }
            System.out.println("Ya no estas conectado");

        } catch (EOFException e) {
            System.out.println("Cliente cerró la conexión");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
                if (ois != null) ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (oos != null) oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private boolean estaConectado() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }
}
