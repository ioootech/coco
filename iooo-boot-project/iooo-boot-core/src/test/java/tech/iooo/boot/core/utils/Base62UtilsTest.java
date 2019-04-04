package tech.iooo.boot.core.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Created on 2018/11/21 9:28 AM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@DisplayName("Base62")
class Base62UtilsTest {

  private final Base62Utils standardEncoder = Base62Utils.createInstance();

  private final Base62Utils[] encoders = {
      Base62Utils.createInstanceWithGmpCharacterSet(),
      Base62Utils.createInstanceWithInvertedCharacterSet()
  };

  @Test
  @DisplayName("should preserve identity of simple byte arrays")
  public void preservesIdentity() {
    for (byte[] message : Environment.getRawInputs()) {
      for (Base62Utils encoder : encoders) {
        final byte[] encoded = encoder.encode(message);
        final byte[] decoded = encoder.decode(encoded);

        assertArrayEquals(message, decoded);
      }
    }
  }

  @Test
  @DisplayName("should produce encodings that only contain alphanumeric characters")
  public void alphaNumericOutput() {
    for (byte[] message : Environment.getRawInputs()) {
      for (Base62Utils encoder : encoders) {
        final byte[] encoded = encoder.encode(message);
        final String encodedStr = new String(encoded);

        assertTrue(isAlphaNumeric(encodedStr));
      }
    }
  }

  @Test
  @DisplayName("should be able to handle empty inputs")
  public void emptyInputs() {
    final byte[] empty = new byte[0];

    for (Base62Utils encoder : encoders) {
      final byte[] encoded = encoder.encode(empty);
      assertArrayEquals(empty, encoded);

      final byte[] decoded = encoder.decode(empty);
      assertArrayEquals(empty, decoded);
    }
  }

  @Test
  @DisplayName("should behave correctly on naive test set")
  public void naiveTestSet() {
    for (Map.Entry<String, String> testSetEntry : Environment.getNaiveTestSet().entrySet()) {
      assertEquals(encode(testSetEntry.getKey()), testSetEntry.getValue());
    }
  }

  private String encode(final String input) {
    return new String(standardEncoder.encode(input.getBytes()));
  }

  private boolean isAlphaNumeric(final String str) {
    return str.matches("^[a-zA-Z0-9]+$");
  }
}
