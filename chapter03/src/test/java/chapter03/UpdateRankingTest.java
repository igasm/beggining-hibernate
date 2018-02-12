package chapter03;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UpdateRankingTest {

  RankingService service = new HibernateRankingService();

  @Test
  public void updateExistingRanking(){
    service.addRanking("Gene Showrama", "Scottball Most", "Ceylon", 6);
    assertEquals(service.getRankingFor("Gene Showrama", "Scottball Most"), 6);
    service.updateRanking("Gene Showrama", "Scottball Most", "Ceylon", 7);
    assertEquals(service.getRankingFor("Gene Showrama", "Scottball Most"), 7);
  }

}
