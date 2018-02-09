package chapter03.simple;

public class Skill {
  private String name;

  //this one for Hibernate
  public Skill() {
  }

  public Skill(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString(){
    return name;
  }
}
