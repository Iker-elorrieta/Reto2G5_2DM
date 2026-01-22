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

	

	// Java
	public String obtenerAlumnosPorProfesor(Integer profesorId) {
	    ArrayList<Users> listaAlumnos = new ArrayList<>();
	    String alumnosJson = "";
	    if (profesorId == null) return alumnosJson;

	    Session session = null;
	    try {
	        session = sessionFactory.openSession();

	        String hql = "select distinct alumno " +
	                "from Reuniones r " +
	                "join r.usersByAlumnoId alumno " +
	                "where r.usersByProfesorId.id = :idProfesor";

	        Query<Users> query = session.createQuery(hql, Users.class);
	        query.setParameter("idProfesor", profesorId);
	        listaAlumnos.addAll(query.list());

	        alumnosJson = crearJson(listaAlumnos);

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (session != null) session.close();
	    }

	    return alumnosJson;
	}


}
