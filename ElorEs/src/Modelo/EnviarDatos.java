package Modelo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EnviarDatos {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public EnviarDatos(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
    }

    public Users login(String username, String password) {
        try {
            oos.writeObject("LOGIN");
            oos.writeObject(username);
            oos.writeObject(password);
            oos.flush();

            return (Users) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

