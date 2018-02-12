package chapter03.simple;

import chapter03.HelperClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PersonTest {

  SessionFactory factory;
  HelperClass helperClass;

  @BeforeClass
  public void setup(){
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .configure()
        .build();
    factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    helperClass = new HelperClass();
  }

  @Test
  public void testSavePerson() {
    try(Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();
      session.save(new Person("J. C. Smell"));
      tx.commit();
    }
  }

  @Test(dependsOnMethods = "testSavePerson")
  public void testFindingPerson() {
    try(Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();
      Person person = helperClass.findPerson(session, "J. C. Smell");
      assertEquals(person.getName(), "J. C. Smell");
      tx.commit();
    }
  }

}