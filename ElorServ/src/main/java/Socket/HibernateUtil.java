package Socket;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {
    private final SessionFactory sessionFactory;

    public HibernateUtil(Environment env) {
        this.sessionFactory = buildSessionFactory(env);
    }

    private static SessionFactory buildSessionFactory(Environment env) {
        try {
            // fallback values
            String defaultUrl = "jdbc:mysql://localhost:3306/eduelorrieta";
            String defaultUser = "root";
            String defaultPass = "";

            // coger envs o fallback
            String url = env.getProperty("db_url", defaultUrl);
            String user = env.getProperty("db_user", defaultUser);
            String password = env.getProperty("db_password", defaultPass);

            Properties props = new Properties();
            props.put("hibernate.connection.url", url);
            props.put("hibernate.connection.username", user);
            props.put("hibernate.connection.password", password);

            return new Configuration()
                    .configure() // hibernate.cfg.xml
                    .addProperties(props)
                    .buildSessionFactory(new StandardServiceRegistryBuilder()
                            .configure("hibernate.cfg.xml").build());

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
