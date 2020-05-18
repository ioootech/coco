package tech.iooo.boot.json;


import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.jupiter.api.Test;
import tech.iooo.boot.core.constants.SuppressTypeConstants;
import tech.iooo.boot.json.domain.Person;

/**
 * @author 龙也
 * @date 2020/4/25 9:46 下午
 */
class JacksonUtilsTest {

  @Test
  @SuppressWarnings(SuppressTypeConstants.UNSTABLE_API_USAGE)
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
    Type type = new TypeToken<Map<Integer, Person>>() {
    }.getType();
    Map<Integer, Person> map = JacksonUtils.fromJson("{\"1\":{\"name\":\"ivan\",\"age\":28,\"data\":{\"foo\":\"bar\"}}}", type);
    System.out.println(map);
    System.out.println(map.get(1));
  }
}
