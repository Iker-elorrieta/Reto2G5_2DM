package Modelo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


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
    public ArrayList<Users> obtenerAlumnos(Users user){
    	try {
    		System.out.println("Solicitando alumnos para el profesor con ID: " + user.getId());
    		oos.writeObject("OBTENER_ALUMNOS");
    		System.out.println("Enviando ID de profesor: " + user.getId());
    		oos.writeObject(user.getId());
    		oos.flush();
    		String json = (String) ois.readObject();
    		
    		Type listType = new TypeToken<ArrayList<Users>>(){}.getType();
    		ArrayList<Users> listaAlumnos = gson.fromJson(json, listType);

    		return listaAlumnos;
            
    	}catch (Exception e) {
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

