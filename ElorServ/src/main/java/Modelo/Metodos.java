package Modelo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import Socket.HibernateUtil;

public class Metodos {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Users login(String usuario, String contrasena) {
        Session session = null;
        Users usuarioLog = null;
        
        try {
            session = sessionFactory.openSession();
            // Usar parámetros para evitar SQL Injection
            Query<Users> query = session.createQuery(
                "FROM Users WHERE username = :username AND password = :password", 
                Users.class
            );
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
}
