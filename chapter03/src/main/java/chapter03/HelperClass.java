package chapter03;

import chapter03.simple.Person;
import chapter03.simple.Skill;
import org.hibernate.Session;

public class HelperClass {

  public Person findPerson(Session session, String name){
    org.hibernate.query.Query<Person> query
        = session.createQuery("from Person p where p.name = :name", Person.class);
    query.setParameter("name", name);
    Person person = query.uniqueResult();
    return person;
  }

  public Person savePerson(Session session, String name){
    Person person = findPerson(session, name);
    if(person==null){
      person=new Person(name);
      session.save(person);
    }
    return person;
  }

  public Skill saveSkill(Session session, String skillName) {
    Skill skill = new Skill(skillName);
    session.save(skill);
    System.out.println("saving skill: " + skill.toString());
    return skill;
  }

}
