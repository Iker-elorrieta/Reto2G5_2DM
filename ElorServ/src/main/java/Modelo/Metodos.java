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

			// Solo usuarios con tipo_id = 3
			String hql = "FROM Users u WHERE u.username = :username AND u.tipos.id = 3";
			Query<Users> query = session.createQuery(hql, Users.class);
			query.setParameter("username", usuario);

			usuarioLog = query.uniqueResult();

			if (usuarioLog != null) {
				// --- Hash de la contraseña plana de la BBDD con el mismo estilo que el cliente
				// ---
				try {
					MessageDigest md = MessageDigest.getInstance("SHA");
					byte[] dataBytes = usuarioLog.getPassword().getBytes();
					md.update(dataBytes);
					byte[] resumen = md.digest();
					String passwordCifradaBBDD = new String(resumen);

					// --- SYSO para debug ---
					System.out.println("Contraseña plana BBDD: " + usuarioLog.getPassword());
					System.out.println("Hash generado en servidor: " + passwordCifradaBBDD);
					System.out.println("Hash recibido del cliente: " + contrasenaHasheada);
					// -----------------------------------

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

	public void cerrarSessionFactory() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
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
