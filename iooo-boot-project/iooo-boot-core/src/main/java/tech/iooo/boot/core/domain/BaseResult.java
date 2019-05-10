package tech.iooo.boot.core.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created on 2018/11/17 9:28 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BaseResult<T> implements Serializable {

  public static final int NO_LOGIN = -1;
  public static final int SUCCESS = 1;
  public static final int FAIL = 0;
  public static final int NO_PERMISSION = 2;
  public static final int USERNAME_EXIST = -909;

  private static final long serialVersionUID = 5186020669003158216L;

  private boolean success = true;
  private int code = SUCCESS;
  private String message = "success";
  private T data;

  public BaseResult(T data) {
    super();
    this.data = data;
  }

  public BaseResult(Throwable e) {
    super();
    this.success = false;
    this.message = e.toString();
    this.code = FAIL;
  }
}
