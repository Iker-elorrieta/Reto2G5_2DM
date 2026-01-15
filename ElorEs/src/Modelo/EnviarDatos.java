package Modelo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;

public class EnviarDatos {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
	private Gson gson = new Gson();


    public EnviarDatos(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
        
    }

    public String login(String username, String password) {
        try {
            oos.writeObject("LOGIN");
            oos.writeObject(username);
            oos.writeObject(password);
            oos.flush();

            return (String) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public <T> T leerJson(String json, Class<T> clase) {
    	try {
            return gson.fromJson(json, clase);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }	}
}

