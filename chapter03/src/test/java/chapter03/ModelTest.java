package chapter03;

import chapter03.simple.Person;
import chapter03.simple.Ranking;
import chapter03.simple.Skill;
import org.testng.annotations.Test;

public class ModelTest {

  @Test
  public void testModelCreation() {
    Person subject = new Person("J. C. Smell");
    Person observer = new Person("Drew Lombardo");
    Skill skill = new Skill("Java");

    Ranking ranking = new Ranking();
    ranking.setObserver(observer);
    ranking.setSubject(subject);
    ranking.setSkill(skill);
    ranking.setRanking(8);

    System.out.println(ranking);
  }


}
