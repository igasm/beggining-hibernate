package chapter03;

import chapter03.simple.Person;
import chapter03.simple.Ranking;
import chapter03.simple.Skill;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.SessionUtil;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HibernateRankingService implements RankingService {

  HelperClass helperClass = new HelperClass();

  @Override
  public void addRanking(String subjectName, String observerName, String skillName, int rankValue) {
    try (Session session = SessionUtil.getSession()){
      Transaction tx = session.beginTransaction();
      addRanking(session, subjectName, observerName, skillName, rankValue);
      tx.commit();
    }
  }

  @Override
  public int getRankingFor(String subjectName, String skillName) {
    try (Session session = SessionUtil.getSession()) {
      Transaction tx = session.beginTransaction();
      int average = getRankingFor(session, subjectName, skillName);
      tx.commit();
      return average;
    }
  }

  @Override
  public void updateRanking(String subjectName, String observerName, String skillName, int rankValue) {
    try(Session session = SessionUtil.getSession()){
      Transaction tx = session.beginTransaction();
      Ranking ranking = findRanking(session, subjectName, observerName, skillName);
      if(ranking == null){
        addRanking(subjectName, observerName, skillName, rankValue);
      }else{
        ranking.setRanking(rankValue);
      }
      tx.commit();
    }
  }

  @Override
  public void removeRanking(String subjectName, String observerName, String skillName) {
    try(Session session = SessionUtil.getSession()){
      Transaction tx = session.beginTransaction();
      Ranking ranking = findRanking(session, subjectName, observerName, skillName);
      if(ranking!=null) {
        session.delete(ranking);
      }
      tx.commit();
    }
  }

  @Override
  public Map<String, Integer> findRankingsFor(String subjectName) {
    Map<String, Integer> result;
    Session session = SessionUtil.getSession();
    Transaction tx = session.beginTransaction();
    result = findRankingsFor(session, subjectName);
    tx.commit();
    session.close();
    return result;
  }

  @Override
  public Person findBestPersonFor(String skillName) {
    Person person = null;
    try(Session session = SessionUtil.getSession()) {
      Transaction tx = session.beginTransaction();
      person = findBestPersonFor(session, skillName);
      tx.commit();
    }
    return person;
  }

  private Person findBestPersonFor(Session session, String skillName) {
    Query<Object[]> query = session.createQuery("select r.subject.name, avg(r.ranking) " +
        " from Ranking r where " +
        "r.skill.name=:skill " +
        "group by r.subject.name " +
        "order by avg(r.ranking) desc", Object[].class);
    query.setParameter("skill", skillName);
    List<Object[]> result = query.list();
    if (result.size() > 0){
      return new HelperClass().findPerson(session, (String) result.get(0)[0]);
    }
    return null;
  }

  private Map<String,Integer> findRankingsFor(Session session, String subjectName) {
    Map<String, Integer> results = new HashMap <>();

    Query<Ranking> query = session.createQuery("from Ranking r where " +
        "r.subject.name=:subject", Ranking.class);
    query.setParameter("subject", subjectName);
    List<Ranking> rankings = query.list();
    String lastSkillName = "";
    int sum=0;
    int count=0;
    for(Ranking r:rankings){
      if(!lastSkillName.equals(r.getSkill().getName())){
        sum=0;
        count=0;
        lastSkillName=r.getSkill().getName();
      }
      sum+=r.getRanking();
      count++;
      results.put(lastSkillName, sum/count);
    }
    return results;
  }

  private void addRanking(Session session, String subjectName, String observerName, String skillName, int rankValue) {
    Person subject = helperClass.savePerson(session, subjectName);
    Person observer = helperClass.savePerson(session, observerName);
    Skill skill = helperClass.saveSkill(session, skillName);

    Ranking ranking = new Ranking();
    ranking.setSubject(subject);
    ranking.setObserver(observer);
    ranking.setSkill(skill);
    ranking.setRanking(rankValue);

    session.save(ranking);
  }

  private Ranking findRanking(Session session, String subjectName, String observerName, String skillName) {
    org.hibernate.query.Query<Ranking> query = session.createQuery("from Ranking r where " +
        "r.subject.name=:subject and " +
        "r.observer.name=:observer and " +
        "r.skill.name=:skill", Ranking.class);
    query.setParameter("subject", subjectName);
    query.setParameter("observer", observerName);
    query.setParameter("skill", skillName);
    Ranking ranking = query.uniqueResult();
    return ranking;
  }

  private int getRankingFor(Session session, String subjectName, String skillName) {
    org.hibernate.query.Query <Ranking> query = session.createQuery("from Ranking r "
        + "where r.subject.name=:name "
        + "and r.skill.name=:skill", Ranking.class);
    query.setParameter("name", subjectName);
    query.setParameter("skill", skillName);

    IntSummaryStatistics stats = query.list()
        .stream()
        .collect(Collectors.summarizingInt(Ranking::getRanking));

    return (int) stats.getAverage();
  }

}
