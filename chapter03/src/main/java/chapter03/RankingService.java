package chapter03;

public interface RankingService {
  void addRanking(String subjectName, String observerName, String skillName, int rankValue);

  int getRankingFor(String subjectName, String skillName);

  void updateRanking(String subjectName, String observerName, String skillName, int rankValue);
}
