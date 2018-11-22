package tech.iooo.boot.core.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created on 2018/11/17 9:28 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ResultBean<T> implements Serializable {

  public static final int NO_LOGIN = -1;
  public static final int SUCCESS = 1;
  public static final int FAIL = 0;
  public static final int NO_PERMISSION = 2;
  public static final int USERNAME_EXIST = -909;

  private static final long serialVersionUID = 5186020669003158216L;

  private String message = "success";
  private boolean ok = true;
  private int code = SUCCESS;
  private T data;

  public ResultBean(T data) {
    super();
    this.data = data;
  }

  public ResultBean(Throwable e) {
    super();
    this.ok = false;
    this.message = e.toString();
    this.code = FAIL;
  }
}
