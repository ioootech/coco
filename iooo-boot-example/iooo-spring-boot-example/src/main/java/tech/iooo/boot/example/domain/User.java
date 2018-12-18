package tech.iooo.boot.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2018-12-10 14:49
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private String name;
  private int age;
}
