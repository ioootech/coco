package tech.iooo.boot.example.service;

import org.springframework.stereotype.Service;
import tech.iooo.boot.example.domain.User;

/**
 * Created on 2018-12-10 14:50
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Service
public class UserRepository {

  public UserRepository() {
  }

  public User getUser() {
    return new User("wilbur", 10);
  }
}
