package tech.iooo.boot.cache.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author Ivan97
 */
@SuppressWarnings("unchecked")
public class KryoRedisSerializer {

  public static byte[] serialize(Object t) {
    Kryo kryo = new Kryo();
    byte[] buffer = new byte[10240000];
    Output output = new Output(buffer);
    kryo.writeClassAndObject(output, t);
    return output.toBytes();
  }

  public static <T> T deserialize(byte[] bytes) {
    Kryo kryo = new Kryo();
    Input input = new Input(bytes);
    return (T) kryo.readClassAndObject(input);
  }
}
