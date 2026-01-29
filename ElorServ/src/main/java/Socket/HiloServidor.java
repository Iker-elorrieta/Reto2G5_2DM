package Socket;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Modelo.Metodos;
import Modelo.Tipos;
import Modelo.Users;

public class HiloServidor extends Thread {

    private Metodos metodos;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public HiloServidor(Socket socket, Metodos metodos) {
        this.socket = socket;
        this.metodos = metodos;
        System.out.println("HiloServidor creado. Metodos: " + (metodos != null ? "OK ✓" : "NULL ✗"));
    }

    @Override
    public void run() {
        try {
            System.out.println("Iniciando comunicación con cliente...");
            
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            System.out.println("ObjectOutputStream creado");
            
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("ObjectInputStream creado");
            
            System.out.println("Esperando comandos...");

            while (true) {
                Object obj = ois.readObject();

                if (!(obj instanceof String)) {
                    System.out.println("Objeto recibido no es String, terminando...");
                    break;
                }

                String comando = (String) obj;
                System.out.println(">>> Comando recibido: " + comando);

                switch (comando) {
                    case "LOGIN":
                        String username = (String) ois.readObject();
                        String password = (String) ois.readObject();
                        
                        System.out.println("LOGIN - Usuario: " + username);
                        System.out.println("Metodos antes de llamar loginCliente: " + (metodos != null ? "OK" : "NULL"));

                        Users u = metodos.loginCliente(username, password);
                        String jsonResponse = metodos.crearJson(u);
                        
                        System.out.println("LOGIN - Resultado: " + (u != null ? "Usuario encontrado" : "Usuario no encontrado"));
                        
                        oos.writeObject(jsonResponse);
                        oos.flush();
                        System.out.println("Respuesta enviada al cliente");
                        break;

                    case "CONSEGUIR_ALUMNOS":
                        String userJson = (String) ois.readObject();
                        System.out.println("CONSEGUIR_ALUMNOS");
                        oos.writeObject(metodos.obtenerAlumnos(userJson));
                        oos.flush();
                        break;

                    case "CONSEGUIR_HORARIOS":
                        Integer userId = (Integer) ois.readObject();
                        System.out.println("CONSEGUIR_HORARIOS - userId: " + userId);
                        
                        // 1. Averiguamos qué rol tiene el usuario (Profesor o Alumno)
                        Tipos tipoUsuario = metodos.obtenerTipoPorUserId(userId);
                        String respuestaHorarios = "[]"; // Por defecto vacío
                        
                        if (tipoUsuario != null) {
                            String rol = tipoUsuario.getName(); // Asumo que devuelve "profesor" o "alumno"
                            
                            // Comprobamos si es profesor (o irakaslea en euskera)
                            if ("profesor".equalsIgnoreCase(rol) || "irakaslea".equalsIgnoreCase(rol)) {
                                System.out.println("Es Profesor, obteniendo horarios de docencia...");
                                respuestaHorarios = metodos.obtenerHorariosProfesor(userId);
                            
                            // Comprobamos si es alumno (o ikaslea en euskera)
                            } else if ("alumno".equalsIgnoreCase(rol) || "ikaslea".equalsIgnoreCase(rol)) {
                                System.out.println("Es Alumno, obteniendo horarios de matriculación...");
                                respuestaHorarios = metodos.obtenerHorariosAlumno(userId);
                            }
                        }

                        oos.writeObject(respuestaHorarios);
                        oos.flush();
                        break;

                    case "CONSEGUIR_PROFESORES":
                        System.out.println("CONSEGUIR_PROFESORES");
                        oos.writeObject(metodos.obtenerProfesores());
                        oos.flush();
                        break;

                    default:
                        System.out.println("Comando desconocido: " + comando);
                        break;
                }
            }

        } catch (EOFException e) {
            System.out.println("Cliente cerró la conexión");
        } catch (Exception e) {
            System.err.println("ERROR en HiloServidor:");
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (socket != null && !socket.isClosed()) socket.close();
                System.out.println("Conexión cerrada");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}