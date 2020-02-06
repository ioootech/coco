package tech.iooo.boot.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.iooo.boot.core.spring.cache.Cache;
import tech.iooo.boot.core.spring.cache.CacheProxy;
import tech.iooo.boot.example.domain.User;

/**
 * Created on 2018-12-10 14:50
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@CacheProxy
@Service
public class UserServiceImpl implements IUserService {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository repository;

  //10秒刷新一次
  @Override
  @Cache(timeInterval = 50)
  public User getUser() {
    if (logger.isInfoEnabled()) {
      logger.info("Time Out");
    }
    return repository.getUser();
  }
}
