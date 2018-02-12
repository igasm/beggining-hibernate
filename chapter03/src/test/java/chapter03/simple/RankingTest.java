package chapter03.simple;

import chapter03.HelperClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class RankingTest {

  SessionFactory factory;
  HelperClass helperClass;

  @BeforeMethod
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

      Person subject = helperClass.savePerson(session, "J. C. Smell");
      Person observer = helperClass.savePerson(session, "Drew Lombardo");
      Skill skill = helperClass.saveSkill(session, "Java");

      Ranking ranking = new Ranking();
      ranking.setSubject(subject);
      ranking.setObserver(observer);
      ranking.setSkill(skill);
      ranking.setRanking(8);
      session.save(ranking);

      tx.commit();
    }
  }

  @Test
  public void testRankings(){
    populateRankingData();

    try(Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();

      org.hibernate.query.Query<Ranking> query = session.createQuery("from Ranking r "
           +  "where r.subject.name=:name "
           +  "and r.skill.name=:skill", Ranking.class);
      query.setParameter("name", "J. C. Smell");
      query.setParameter("skill", "Java");

      IntSummaryStatistics stats = query.list()
          .stream()
          .collect(Collectors.summarizingInt(Ranking::getRanking));

      query.list().forEach(System.out::println);

      long count = stats.getCount();
      int average = (int) stats.getAverage();

      tx.commit();
      session.close();
      assertEquals(count, 3);
      assertEquals(average, 7);
    }

  }

  @Test
  public void changeRanking(){
    populateRankingData();

    try(Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();
      org.hibernate.query.Query<Ranking> query = session.createQuery("from Ranking r " +
          "where r.subject.name=:subject and " +
          "r.observer.name=:observer and " +
          "r.skill.name=:skill", Ranking.class);
      query.setParameter("subject", "J. C. Smell");
      query.setParameter("observer", "Gene Showrama");
      query.setParameter("skill", "Java");

      Ranking ranking = query.uniqueResult();
      assertNotNull(ranking, "Could not find matching ranking");
      ranking.setRanking(9);
      tx.commit();

    }
    assertEquals(getAverage("J. C. Smell", "Java"), 8);
  }

  @Test
  public void removeRanking(){
    populateRankingData();

    try(Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();
      org.hibernate.query.Query<Ranking> query = session.createQuery("from Ranking r " +
          "where r.subject.name=:subject and " +
          "r.observer.name=:observer and " +
          "r.skill.name=:skill", Ranking.class);
      query.setParameter("subject", "J. C. Smell");
      query.setParameter("observer", "Gene Showrama");
      query.setParameter("skill", "Java");

      Ranking ranking = query.uniqueResult();
      assertNotNull(ranking, "Could not find matching ranking");
      session.delete(ranking);
      tx.commit();

    }
    assertEquals(getAverage("J. C. Smell", "Java"), 7);
  }

  private int getAverage(String subject, String skill) {
    int avg;
    try(Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();

      org.hibernate.query.Query<Ranking> query = session.createQuery("from Ranking r "
          +  "where r.subject.name=:name "
          +  "and r.skill.name=:skill", Ranking.class);
      query.setParameter("name", "J. C. Smell");
      query.setParameter("skill", "Java");

      IntSummaryStatistics stats = query.list()
          .stream()
          .collect(Collectors.summarizingInt(Ranking::getRanking));

      query.list().forEach(System.out::println);

      avg = (int) stats.getAverage();

      tx.commit();
      session.close();
    }
    return avg;
  }

  private void populateRankingData() {
    try(Session session = factory.openSession()){
      Transaction tx = session.beginTransaction();
      createData(session, "J. C. Smell", "Gene Showrama", "Java", 6);
      createData(session, "J. C. Smell", "Scottball Most", "Java", 7);
      createData(session, "J. C. Smell", "Drew Lombardo", "Java", 8);
      tx.commit();
    }
  }

  private void createData(Session session, String subjectName, String observerName, String skillName, int rank) {
    Person subject = helperClass.savePerson(session, subjectName);
    Person observer = helperClass.savePerson(session, observerName);
    Skill skill = helperClass.saveSkill(session, skillName);

    Ranking ranking = new Ranking();
    ranking.setSubject(subject);
    ranking.setObserver(observer);
    ranking.setSkill(skill);
    ranking.setRanking(rank);
    session.save(ranking);
  }


}
