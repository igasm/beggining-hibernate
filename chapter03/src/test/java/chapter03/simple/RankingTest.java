package chapter03.simple;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RankingTest {

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
  public void testSaveRanking(){
    try (Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();

      Person person = helperClass.savePerson(session, "J. C. Smell");
      Person observer = helperClass.savePerson(session, "Drew Lombardo");
      Skill skill = saveSkill(session, "Java");
    }
  }

}
