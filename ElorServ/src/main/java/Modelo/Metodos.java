package Modelo;

import java.security.MessageDigest;
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
	private HibernateUtil hibernateUtil = new HibernateUtil();

	public Metodos() {
		sessionFactory = hibernateUtil.getSessionFactory();
		this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
	}
	
	public String crearJson(Object objeto) {
		return gson.toJson(objeto);
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

			String hql = "FROM Users u JOIN FETCH u.tipos WHERE u.username = :username AND u.password = :password";
			Query<Users> query = session.createQuery(hql, Users.class);
			query.setParameter("username", username);
			query.setParameter("password", password);

			usuarioLog = query.uniqueResult();

			if (usuarioLog != null) {
				usuarioLog.getTipos().getId();
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

	
	public String obtenerAlumnos(String profesorJson) {
		ArrayList<Users> listaAlumnos = new ArrayList<>();
		Session session = sessionFactory.openSession();
		String json = null;

		try {
			Users profesor = gson.fromJson(profesorJson, Users.class);
			String hql = "select distinct r.usersByAlumnoId " + "from Users profesor "
					+ "join profesor.reunionesesForProfesorId r " + "where profesor = :profesor";
			Query<Users> query = session.createQuery(hql, Users.class);
			query.setParameter("profesor", profesor);
			listaAlumnos.addAll(query.list());

			json = crearJson(listaAlumnos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return json;
	}

}
