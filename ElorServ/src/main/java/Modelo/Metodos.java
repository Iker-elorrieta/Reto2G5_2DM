package Modelo;

import java.security.MessageDigest;

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
		this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}

	public Users loginCliente(String usuario, String contrasenaHasheada) {
		Session session = null;
		Users usuarioLog = null;

		try {
			session = sessionFactory.openSession();

			String hql = "FROM Users u WHERE u.username = :username AND u.tipos.id = 3";
			Query<Users> query = session.createQuery(hql, Users.class);
			query.setParameter("username", usuario);

			usuarioLog = query.uniqueResult();

			if (usuarioLog != null) {
				
				try {
					MessageDigest md = MessageDigest.getInstance("SHA");
					byte[] dataBytes = usuarioLog.getPassword().getBytes();
					md.update(dataBytes);
					byte[] resumen = md.digest();
					String passwordCifradaBBDD = new String(resumen);

					// Comparación
					if (!passwordCifradaBBDD.equals(contrasenaHasheada)) {
						usuarioLog = null; // contraseña incorrecta
					}

				} catch (Exception e) {
					e.printStackTrace();
					usuarioLog = null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return usuarioLog;
	}


	public Users loginWeb(String username, String password) {
	    Session session = null;
	    Users usuarioLog = null;

	    try {
	        session = sessionFactory.openSession();

	        String hql = "FROM Users u WHERE u.username = :username AND u.password = :password";
	        Query<Users> query = session.createQuery(hql, Users.class);
	        query.setParameter("username", username);
	        query.setParameter("password", password);

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


	public String crearJson(Object objeto) {
		return gson.toJson(objeto);
	}

}
