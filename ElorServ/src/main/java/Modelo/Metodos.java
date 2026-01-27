package Modelo;

import java.security.MessageDigest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Socket.HibernateUtil;

public class Metodos {
	private SessionFactory sessionFactory;
	private Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();;
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

			String hql = "FROM Users u WHERE u.username = :username AND (u.tipos.name = 'profesor' OR u.tipos.nameEu = 'irakaslea')";
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

	public String obtenerStats() {
		Session session = sessionFactory.openSession();
		String json = null;
		try {
			String totalAlumnosStr = "select count(u) from Users u where u.tipos.name = 'alumno' or u.tipos.nameEu = 'ikaslea'";
			String totalProfesoresStr = "select count(u) from Users u where u.tipos.name = 'profesor' or u.tipos.nameEu = 'irakaslea'";
			String totalReunionesHoyStr = "select count(r) from Reuniones r where date(r.fecha) = :today";

			// Queries
			Query<Long> queryAlumnos = session.createQuery(totalAlumnosStr, Long.class);
			Query<Long> queryProfesores = session.createQuery(totalProfesoresStr, Long.class);

			Date today = new Date(System.currentTimeMillis());
			Query<Long> queryReuniones = session.createQuery(totalReunionesHoyStr, Long.class).setParameter("today",
					today);

			// Results
			Long totalAlumnos = queryAlumnos.uniqueResult();
			Long totalProfesores = queryProfesores.uniqueResult();
			Long totalReunionesHoy = queryReuniones.uniqueResult();

			// Map and JSON
			Map<String, Long> stats = new HashMap<>();
			stats.put("totalAlumnos", totalAlumnos);
			stats.put("totalProfesores", totalProfesores);
			stats.put("totalReunionesHoy", totalReunionesHoy);

			json = crearJson(stats);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return json;
	}

	public String obtenerHorarios(Integer id) {
		Session session = sessionFactory.openSession();
		String json = null;

		String hqlUsuario = "FROM Users u JOIN FETCH u.tipos WHERE u =" + id;
		Query<Users> queryUsuario = session.createQuery(hqlUsuario, Users.class);
		Users usuario = queryUsuario.uniqueResult();

		System.out.println("=============================");
		System.out.println(usuario.getTipos().getName());

		if (usuario.getTipos().getName().equals("profesor") || usuario.getTipos().getNameEu().equals("irakaslea")) {
			try {
				String hql = "FROM Horarios h JOIN FETCH h.modulos m JOIN FETCH m.ciclos c WHERE h.users.id = :userId";
				Query<Horarios> query = session.createQuery(hql, Horarios.class);
				query.setParameter("userId", usuario.getId());
				List<Horarios> horarios = query.list();
				json = crearJson(horarios);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();

			}

		} else if (usuario.getTipos().getName().equals("alumno") || usuario.getTipos().getNameEu().equals("ikaslea")) {

			String hql = "select distinct h " + "from Horarios h " + "join fetch h.modulos m "
					+ "join fetch m.ciclos c " + "where exists ( " + "   select 1 " + "   from Matriculaciones matr "
					+ "   where matr.users = " + id + "     and matr.ciclos = c "
					+ "     and matr.curso = m.curso " + ") " + "order by h.dia, h.hora";

			Query<Horarios> query = session.createQuery(hql, Horarios.class);

			List<Horarios> horarios = query.list();
			json = crearJson(horarios);

		}

		return json;
	}

	public String obtenerProfesores() {

		Session session = sessionFactory.openSession();

		String hqlUsuario = "FROM Users u JOIN FETCH u.tipos t WHERE t.name = :nombreTipo OR t.nameEu = :nombreTipoEu";
		Query<Users> queryUsuario = session.createQuery(hqlUsuario, Users.class);
		queryUsuario.setParameter("nombreTipo", "profesor");
		queryUsuario.setParameter("nombreTipoEu", "irakaslea");
		List<Users> usuario = queryUsuario.list();

		return crearJson(usuario);
	}

	public String obtenerUsers() {
		Session session = sessionFactory.openSession();
		try {
			String hql = "FROM Users u JOIN FETCH u.tipos ORDER BY u.id ASC";
			Query<Users> query = session.createQuery(hql, Users.class);
			List<Users> usuarios = query.list();
			String json = crearJson(usuarios);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
	}

	public boolean createUser(Users user) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.persist(user);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			return false;
		} finally {
			session.close();
		}

	}

	public boolean updateUser(Integer id, Users user) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			Users existingUser = session.get(Users.class, id);
			if (existingUser == null) {
				return false;
			}
			// Update all fields
			existingUser.setTipos(user.getTipos());
			existingUser.setEmail(user.getEmail());
			existingUser.setUsername(user.getUsername());
			existingUser.setPassword(user.getPassword());
			existingUser.setNombre(user.getNombre());
			existingUser.setApellidos(user.getApellidos());
			existingUser.setDni(user.getDni());
			existingUser.setDireccion(user.getDireccion());
			existingUser.setTelefono1(user.getTelefono1());
			existingUser.setTelefono2(user.getTelefono2());
			existingUser.setArgazkiaUrl(user.getArgazkiaUrl());
			existingUser.setCreatedAt(user.getCreatedAt());
			existingUser.setUpdatedAt(user.getUpdatedAt());
			existingUser.setMatriculacioneses(user.getMatriculacioneses());
			existingUser.setReunionesesForAlumnoId(user.getReunionesesForAlumnoId());
			existingUser.setHorarioses(user.getHorarioses());
			existingUser.setReunionesesForProfesorId(user.getReunionesesForProfesorId());

			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			return false;
		} finally {
			session.close();
		}
	}

	public boolean deleteUser(Integer id) {
	    Session session = sessionFactory.openSession();
	    try {
	        session.beginTransaction();
	        Users user = session.get(Users.class, id);
	        if (user != null) {
	            session.remove(user); // Removes the entity
	            session.getTransaction().commit(); // Commit the transaction
	            return true;
	        } else {
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        if (session.getTransaction() != null) {
	            session.getTransaction().rollback();
	        }
	        return false;
	    } finally {
	        session.close();
	    }
	}

	public String obtenerReuniones(Integer id) {
		Session session = sessionFactory.openSession();
		String json = null;

		try {
			String hql = "FROM Reuniones r JOIN FETCH r.usersByAlumnoId JOIN FETCH r.usersByProfesorId WHERE r.usersByProfesorId = "+id +" OR r.usersByAlumnoId ="+id+" ORDER BY r.fecha ASC";
			Query<Reuniones> query = session.createQuery(hql, Reuniones.class);
			List<Reuniones> reuniones = query.list();
			json = crearJson(reuniones);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return json;
	}


}
