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

  private static final long serialVersionUID = 5186020669003158216L;

  public static final String SUCCESS = "SUCCESS";
  public static final String ERROR = "ERROR";
  public static final String NO_PERMISSION = "NO_PERMISSION";

  private boolean success = true;
  private String errCode;
  private String message = SUCCESS;
  private T data;

  public static <T> BaseResult<T> of(T data) {
    return BaseResult.<T>builder()
        .success(true)
        .message(SUCCESS)
        .data(data)
        .build();
  }

  public static <T> BaseResult<T> fail(String errCode, String message) {
    return BaseResult.<T>builder()
        .success(false)
        .errCode(errCode)
        .message(message)
        .build();
  }

  public static <T> BaseResult<T> fail(Throwable e) {
    return BaseResult.<T>builder()
        .success(false)
        .errCode(ERROR)
        .message(e.getMessage())
        .build();
  }
}
