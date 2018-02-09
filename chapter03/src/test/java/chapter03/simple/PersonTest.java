package chapter03.simple;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PersonTest {
  SessionFactory factory;

  @BeforeClass
  public void setup(){
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .configure()
        .build();
    factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
  }

  @Test
  public void testSavePerson() {
    try(Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();
      session.save(new Person("J. C. Smell"));
      tx.commit();
    }
  }

}