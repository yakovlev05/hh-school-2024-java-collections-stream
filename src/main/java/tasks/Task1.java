package tasks;

import common.Person;
import common.PersonService;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    return persons.stream()
        .sorted(Comparator.comparingInt(p -> personIds.indexOf(p.id())))
        .toList();
  }
}
/*
Оценка сложности
пусть n - длина persons и personIds (они одинаковые)

Метод indexOf работает за O(n) - пробегает весь список в худшем случае
Метод sorted() использует TimSort (прочитал в инете), его сложность nlog(n)

В итоге (мб ошибаюсь): O(nlog(n) + n*n)
*/