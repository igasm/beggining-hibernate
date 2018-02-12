package chapter03;

import chapter03.simple.Person;
import chapter03.simple.Ranking;
import chapter03.simple.Skill;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.SessionUtil;

import java.util.IntSummaryStatistics;
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

  @Override
  public int getRankingFor(String subjectName, String skillName) {
    try (Session session = SessionUtil.getSession()) {
      Transaction tx = session.beginTransaction();
      int average = getRankingFor(session, subjectName, skillName);
      tx.commit();
      return average;
    }
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
