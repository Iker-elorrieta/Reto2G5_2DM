package Modelo;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Socket.HibernateUtil;

public class Metodos {
	private SessionFactory sessionFactory;
	private Gson gson = new Gson();

	public Metodos() {
		sessionFactory = HibernateUtil.getSessionFactory();
		this.gson = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.create();
	}

	public Users login(String usuario, String contrasena) {
		Session session = null;
		Users usuarioLog = null;
		
		try {
			session = sessionFactory.openSession();
			// Usar parámetros para evitar SQL Injection
			Query<Users> query = session.createQuery("FROM Users WHERE username = :username AND password = :password",
					Users.class);
			query.setParameter("username", usuario);
			query.setParameter("password", contrasena);

			usuarioLog = query.uniqueResult();
			
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return usuarioLog;
	}

	public void cerrarSessionFactory() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	public ArrayList<Users> getAllUsers() {
		Session session = null;
		ArrayList<Users> usuarios = new ArrayList<>();

		try {
			session = sessionFactory.openSession();
			Query<Users> query = session.createQuery("FROM Users", Users.class);
			usuarios = (ArrayList<Users>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return usuarios;
	}
	
	public String crearJson(Object objeto) {
		return gson.toJson(objeto);
	}
	
	
}
