package tech.iooo.boot.json;


import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import java.util.Map;
import org.junit.jupiter.api.Test;
import tech.iooo.boot.json.domain.Person;

/**
 * @author 龙也
 * @date 2020/4/25 9:46 下午
 */
class JacksonUtilsTest {

  @Test
  public void test() {
    Person person = new Person();
    person.setName("ivan");
    person.setAge(28);
    Map<String, Object> data = Maps.newHashMap();
    data.put("foo", "bar");
    person.setData(data);
    System.out.println(JacksonUtils.toJson(person));
    String json = "{\"name\":\"ivan\",\"age\":28,\"data\":{\"foo\":\"bar\"}}";
    System.out.println(JacksonUtils.fromJson(json, Person.class));

    System.out.println(JacksonUtils.toJson(data));
    System.out.println(JacksonUtils.fromJson("{\"foo\":\"bar\"}", Map.class));

    Map<Integer, Person> personMap = Maps.newHashMap();
    personMap.put(1, person);
    System.out.println(JacksonUtils.toJson(personMap));
    TypeToken<Map<Integer, Person>> typeToken = new TypeToken<Map<Integer, Person>>() {
    };
    Map<Integer, Person> map = JacksonUtils.fromJson("{\"1\":{\"name\":\"ivan\",\"age\":28,\"data\":{\"foo\":\"bar\"}}}", typeToken.getType());
    System.out.println(map);
    System.out.println(map.get(1));
  }
}
