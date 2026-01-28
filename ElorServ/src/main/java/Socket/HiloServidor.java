package Socket;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Modelo.Metodos;
import Modelo.Users;

public class HiloServidor extends Thread {

    private Metodos metodos;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public HiloServidor(Socket socket, Metodos metodos) {
        this.socket = socket;
        this.metodos = metodos;
    }

    @Override
    public void run() {
        try {

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object obj = ois.readObject();

                if (!(obj instanceof String)) {
                    break;
                }

                String comando = (String) obj;

                switch (comando) {
                    case "LOGIN":
                        String username = (String) ois.readObject();
                        String password = (String) ois.readObject();

                        Users u = metodos.loginCliente(username, password);
                        oos.writeObject(metodos.crearJson(u));
                        oos.flush();
                        break;

                    case "CONSEGUIR_ALUMNOS":
                        String userJson = (String) ois.readObject();
                        oos.writeObject(metodos.obtenerAlumnos(userJson));
                        oos.flush();
                        break;

                    case "CONSEGUIR_HORARIOS":
                        Integer userId = (Integer) ois.read();
                        oos.writeObject(metodos.obtenerHorariosProfesor(userId));
                        oos.flush();
                        break;

                    case "CONSEGUIR_PROFESORES":
                        oos.writeObject(metodos.obtenerProfesores());
                        oos.flush();
                        break;

                    default:
                        break;
                }
            }

        } catch (EOFException e) {
            System.out.println("Cliense serro la koneksion pe");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
