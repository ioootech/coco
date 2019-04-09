package tech.iooo.boot.cache.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ivan97
 */
@SuppressWarnings("unchecked")
public class KryoRedisSerializer {

  private static final ThreadLocal<Kryo> kryo = ThreadLocal.withInitial(() -> {
    Kryo kryo = new Kryo();
    // configure kryo instance, customize settings
    kryo.setReferences(false);
    kryo.register(Collection.class);
    kryo.register(Map.class);
    return kryo;
  });
  private static Map<Class, Registration> registrationMap = new ConcurrentHashMap<>();

  public static <T> byte[] serialize(T obj) {
    Class<T> clazz = (Class<T>) obj.getClass();
    Kryo kryo = KryoRedisSerializer.kryo.get();
    if (!registrationMap.containsKey(clazz)) {
      Registration registration = kryo.register(clazz);
      registrationMap.put(clazz, registration);
    }
    ByteArrayOutputStream outputStream = null;
    Output output = null;
    byte[] bytes;
    try {
      outputStream = new ByteArrayOutputStream();
      output = new Output(outputStream);
      kryo.writeObject(output, obj);
      output.flush();
      bytes = outputStream.toByteArray();
      return bytes;
    } finally {
      try {
        if (output != null) {
          output.close();
        }
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (Exception ignore) {

      }
    }
  }

  public static <T> T deserialize(byte[] bytes) {
    Kryo kryo = KryoRedisSerializer.kryo.get();
    Input input = new Input(bytes);
    return (T) kryo.readClassAndObject(input);
  }

  public static <T> T deserialize(byte[] data, Class<T> clazz) {
    Kryo kryo = KryoRedisSerializer.kryo.get();
    Registration registration = registrationMap.get(clazz);
    if (registration == null) {
      registration = kryo.register(clazz);
      registrationMap.put(clazz, registration);
    }
    T object = null;
    Input input;
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
      input = new Input(byteArrayInputStream);
      object = (T) kryo.readObject(input, registration.getType());
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return object;
  }
}
