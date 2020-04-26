package tech.iooo.boot.json.domain;

import java.util.Map;
import lombok.Data;

/**
 * @author 龙也
 * @date 2020/4/25 9:48 下午
 */
@Data
public class Person {

  private String name;
  private Integer age;
  private Map<String, Object> data;
}
