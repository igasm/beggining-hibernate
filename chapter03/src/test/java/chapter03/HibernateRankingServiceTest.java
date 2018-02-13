package chapter03;

import chapter03.simple.Person;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

public class HibernateRankingServiceTest {

  public static final String OBSERVER_NAME = "Scottball Most";
  public static final String SKILL_NAME = "Ceylon";
  public static final String SUBJECT_NAME = "Gene Showrama";

  RankingService service = new HibernateRankingService();

  @Test
  public void addRanking(){
    service.addRanking("J. C. Smell", "Drew Lombardo", "Mule", 8);
    assertEquals(service.getRankingFor("J. C. Smell", "Mule"), 8);
  }

  @Test
  public void updateExistingRanking(){
    service.addRanking(SUBJECT_NAME, OBSERVER_NAME, SKILL_NAME, 6);
    assertEquals(service.getRankingFor(SUBJECT_NAME, SKILL_NAME), 6);
    service.updateRanking(SUBJECT_NAME, OBSERVER_NAME, SKILL_NAME, 7);
    assertEquals(service.getRankingFor(SUBJECT_NAME, SKILL_NAME), 7);
  }

  @Test
  public void updateNotExistingRanking(){
    assertEquals(service.getRankingFor(SUBJECT_NAME, "Java"), 0);
    service.updateRanking(SUBJECT_NAME, OBSERVER_NAME, "Java", 7);
    assertEquals(service.getRankingFor(SUBJECT_NAME, "Java"), 7);
  }

  @Test
  public void removeRanking() {
    service.addRanking(SUBJECT_NAME, OBSERVER_NAME, SKILL_NAME, 8);
    assertEquals(service.getRankingFor(SUBJECT_NAME, SKILL_NAME), 8);
    service.removeRanking(SUBJECT_NAME, OBSERVER_NAME, SKILL_NAME);
    assertEquals(service.getRankingFor(SUBJECT_NAME, SKILL_NAME), 0);
  }

  @Test
  public void removeNotExistingRanking(){
    service.removeRanking(SUBJECT_NAME, OBSERVER_NAME, SKILL_NAME);
  }

  @Test
  public void validateRankingAverage(){
    service.addRanking("A", "B", "C", 4);
    service.addRanking("A", "B", "C", 5);
    service.addRanking("A", "B", "C", 6);
    assertEquals(service.getRankingFor("A", "C"), 5);
    service.addRanking("A", "B", "C", 7);
    service.addRanking("A", "B", "C", 8);
    assertEquals(service.getRankingFor("A", "C"), 6);
  }

  @Test
  public void findAllRankingEmptySet(){
    String subjectName = "Nobody";
    assertEquals(service.getRankingFor(subjectName, "Java"), 0);
    assertEquals(service.getRankingFor(subjectName, "Python"), 0);
    Map<String, Integer> ranking=service.findRankingsFor(subjectName);
    //we expect map to be empty
    assertEquals(ranking.size(), 0);
  }

  @Test
  public void findAllRankings() {
    String subjectName = "Somebody";
    String observerName = "Nobody";

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(service.getRankingFor(subjectName, "Java")).isEqualTo(0);
    softly.assertThat(service.getRankingFor(subjectName, "Python")).isEqualTo(0);

    service.addRanking(subjectName, observerName, "Java", 9);
    service.addRanking(subjectName, observerName, "Java", 7);
    service.addRanking(subjectName, observerName, "Python", 7);
    service.addRanking(subjectName, observerName, "Python", 5);


    Map<String, Integer> rankings = service.findRankingsFor("Somebody");
    softly.assertThat(rankings.size()).isEqualTo(2);
    softly.assertThat(rankings.get("Java")).isNotNull();
    softly.assertThat(rankings.get("Python"));
    softly.assertThat(rankings.get("Python")).isEqualTo(6);
    softly.assertAll();
  }

  @Test
  public void findBestForNonexistentSkill() {
    Person p = service.findBestPersonFor("no skill");
    assertThat(p).isNull();
  }

  @Test
  public void findBestForSkill() {
    service.addRanking("S1", "01", "Sk1", 6);
    service.addRanking("S1", "02", "Sk1", 8);
    service.addRanking("S2", "01", "Sk1", 5);
    service.addRanking("S2", "02", "Sk1", 7);
    service.addRanking("S3", "01", "Sk1", 7);
    service.addRanking("S3", "02", "Sk1", 9);
    //data that should not factor in!
    service.addRanking("S3", "01", "Sk2", 2);
    Person p = service.findBestPersonFor("Sk1");
    assertThat(p.getName()).isEqualTo("S3");
  }

}