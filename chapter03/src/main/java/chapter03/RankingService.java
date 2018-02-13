package chapter03;

import chapter03.simple.Person;

import java.util.Map;

public interface RankingService {
  void addRanking(String subjectName, String observerName, String skillName, int rankValue);

  int getRankingFor(String subjectName, String skillName);

  void updateRanking(String subjectName, String observerName, String skillName, int rankValue);

  void removeRanking(String subjectName, String observerName, String skillName);

  Map<String,Integer> findRankingsFor(String subjectName);

  Person findBestPersonFor(String skillName);
}
