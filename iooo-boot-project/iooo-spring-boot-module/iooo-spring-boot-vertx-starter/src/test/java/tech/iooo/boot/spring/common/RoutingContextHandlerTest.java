package tech.iooo.boot.spring.common;

import io.vertx.ext.web.RoutingContext;
import org.junit.jupiter.api.Test;

/**
 * @author 龙也
 * @date 2019/8/28 2:38 下午
 */
public class RoutingContextHandlerTest {

  private static class TestController implements RoutingContextHandler {

    @Override
    public void handle(RoutingContext context) {

    }
  }

  @Test
  public void test() {
    TestController testController = new TestController();
    System.out.println(testController.path());
  }
}
