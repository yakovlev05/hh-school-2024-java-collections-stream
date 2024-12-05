package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    // Учитываем, что нужно начинать со второго
    // Если список пустой, то ошибки не будет
    return persons.stream()
        .skip(1)
        .map(Person::firstName)
        .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // Множество по определению содержит уникальные элементы
    // Используем метод getNames
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    // Убрал дублирование secondName
    // Использовал Optional для обработки null
    return String.join(" ",
        Optional.ofNullable(person.secondName()).orElse(""),
        Optional.ofNullable(person.firstName()).orElse(""));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    // По условию при дубликатах мы оставляем первый вариант - это учтено
    return persons.stream()
        .collect(Collectors.toMap(Person::id, Person::firstName, (current, replace) -> current));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    // Возвращаем true сразу, необязательно полностью пробегать
    Set<Person> setPersons1 = new HashSet<>(persons1); // Можно использовать set для проверки вхождения, т.к. такая операция O(1)
    for (Person person : persons2) {
      if (setPersons1.contains(person)) {
        return true; // содержит
      }
    }
    return false; // не содержит
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    // Убрал лишние действия
    return numbers
        .filter(num -> num % 2 == 0)
        .count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());

    /*
    Вся соль кроется в том, как хэшируется Integer.

    HashSet использует хэш таблицы (вообще основан на HashMap). То есть при добавлении элемента,
    он вычисляет хэш. Но хэш класса Integer равен значению примитива, поэтому элементы в HashSet оказываются
    отсортированными.

    То есть
    assert 555 == Integer.valueOf(555).hashCode()
    */
  }
}
