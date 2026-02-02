package Socket;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {
    private final SessionFactory sessionFactory;

    public HibernateUtil(Environment env) {
        this.sessionFactory = buildSessionFactory(env);
    }

    private SessionFactory buildSessionFactory(Environment env) {
        try {
            System.out.println("=== CONSTRUYENDO SESSION FACTORY ===");

            // Leer variables de entorno
            String url = env.getProperty("datasource.url");
            String user = env.getProperty("datasource.user");
            String password = env.getProperty("datasource.password");

            System.out.println("URL: " + url);
            System.out.println("User: " + user);

            // Crear el registro de servicios con las propiedades
            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            registryBuilder.configure("hibernate.cfg.xml"); // Carga el cfg.xml
            
            // Sobrescribir propiedades de conexión
            registryBuilder.applySetting("hibernate.connection.url", url);
            registryBuilder.applySetting("hibernate.connection.username", user);
            registryBuilder.applySetting("hibernate.connection.password", password);
            
            StandardServiceRegistry registry = registryBuilder.build();
            
            System.out.println("Registry construido");

            // Crear el SessionFactory desde el registry
            MetadataSources sources = new MetadataSources(registry);
            Metadata metadata = sources.getMetadataBuilder().build();
            
            SessionFactory sf = metadata.getSessionFactoryBuilder().build();
            
            System.out.println("SessionFactory creado exitosamente");
            
            return sf;

        } catch (Throwable ex) {
            System.err.println("Error creando SessionFactory:");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}