package tasks;

import common.Person;
import common.PersonService;
import common.PersonWithResumes;
import common.Resume;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
  Еще один вариант задачи обогащения
  На вход имеем коллекцию персон
  Сервис умеет по personId искать их резюме (у каждой персоны может быть несколько резюме)
  На выходе хотим получить объекты с персоной и ее списком резюме
 */
public class Task8 {
  private final PersonService personService;

  public Task8(PersonService personService) {
    this.personService = personService;
  }

  public Set<PersonWithResumes> enrichPersonsWithResumes(Collection<Person> persons) {
    Set<Resume> resumes = personService.findResumes(persons.stream().map(Person::id).toList());

    Map<Integer, Set<Resume>> personIdToResume = resumes.stream()
        .collect(Collectors.toMap(
            Resume::personId,
            Set::of,
            this::union));

    return persons.stream()
        .map(p -> new PersonWithResumes(p, personIdToResume.getOrDefault(p.id(), Set.of())))
        .collect(Collectors.toSet());
  }

  private <T> Set<T> union(Set<T> first, Set<T> second) {
    return Stream.concat(first.stream(), second.stream())
        .collect(Collectors.toSet());
  }
}
