package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Logger;

public class SessionUtil {
  private static final SessionUtil instance = new SessionUtil();
  private final SessionFactory factory;
  private static final String CONFIG_NAME = "/configuration.properties";
  Logger logger = Logger.getLogger(this.getClass().toString());

  public SessionUtil() {
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .configure()
        .build();
    factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
  }

  public static SessionUtil getInstance() {
    return instance;
  }

  public static Session getSession() {
    return getInstance().factory.openSession();
  }
}
