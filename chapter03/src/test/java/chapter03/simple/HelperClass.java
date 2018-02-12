package chapter03.simple;

import org.hibernate.Session;

public class HelperClass {

  Person findPerson(Session session, String name){
    org.hibernate.query.Query<Person> query
        = session.createQuery("from Person p where p.name = :name", Person.class);
    query.setParameter("name", name);
    Person person = query.uniqueResult();
    return person;
  }

  Person savePerson(Session session, String name){
    Person person = findPerson(session, name);
    if(person==null){
      person=new Person(name);
      session.save(person);
    }
    return person;
  }

  Skill saveSkill(Session session, String skillName) {
    Skill skill = new Skill(skillName);
    session.save(skill);
    System.out.println("saving skill: " + skill.toString());
    return skill;
  }

}
