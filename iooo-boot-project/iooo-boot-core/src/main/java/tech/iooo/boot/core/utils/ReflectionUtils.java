/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.iooo.boot.core.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Simple utility class for working with the reflection API and handling reflection exceptions.
 *
 * <p>Only intended for internal use.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Rod Johnson
 * @author Costin Leau
 * @author Sam Brannen
 * @author Chris Beams
 * @since 1.2.2
 */
public abstract class ReflectionUtils {


  /**
   * void(V).
   */
  public static final char JVM_VOID = 'V';

  /**
   * boolean(Z).
   */
  public static final char JVM_BOOLEAN = 'Z';

  /**
   * byte(B).
   */
  public static final char JVM_BYTE = 'B';

  /**
   * char(C).
   */
  public static final char JVM_CHAR = 'C';

  /**
   * double(D).
   */
  public static final char JVM_DOUBLE = 'D';

  /**
   * float(F).
   */
  public static final char JVM_FLOAT = 'F';

  /**
   * int(I).
   */
  public static final char JVM_INT = 'I';

  /**
   * long(J).
   */
  public static final char JVM_LONG = 'J';

  /**
   * short(S).
   */
  public static final char JVM_SHORT = 'S';

  /**
   * Pre-built FieldFilter that matches all non-static, non-final fields.
   */
  public static final FieldFilter COPYABLE_FIELDS = new FieldFilter() {

    @Override
    public boolean matches(Field field) {
      return !(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()));
    }
  };
  /**
   * Pre-built MethodFilter that matches all non-bridge methods.
   */
  public static final MethodFilter NON_BRIDGED_METHODS = new MethodFilter() {

    @Override
    public boolean matches(Method method) {
      return !method.isBridge();
    }
  };
  /**
   * Pre-built MethodFilter that matches all non-bridge methods which are not declared on {@code java.lang.Object}.
   */
  public static final MethodFilter USER_DECLARED_METHODS = new MethodFilter() {

    @Override
    public boolean matches(Method method) {
      return (!method.isBridge() && method.getDeclaringClass() != Object.class);
    }
  };
  public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
  public static final String JAVA_IDENT_REGEX = "(?:[_$a-zA-Z][_$a-zA-Z0-9]*)";
  public static final String JAVA_NAME_REGEX = "(?:" + JAVA_IDENT_REGEX + "(?:\\." + JAVA_IDENT_REGEX + ")*)";
  public static final String CLASS_DESC = "(?:L" + JAVA_IDENT_REGEX + "(?:\\/" + JAVA_IDENT_REGEX + ")*;)";
  public static final String ARRAY_DESC = "(?:\\[+(?:(?:[VZBCDFIJS])|" + CLASS_DESC + "))";
  public static final String DESC_REGEX = "(?:(?:[VZBCDFIJS])|" + CLASS_DESC + "|" + ARRAY_DESC + ")";
  public static final Pattern DESC_PATTERN = Pattern.compile(DESC_REGEX);
  public static final String METHOD_DESC_REGEX = "(?:(" + JAVA_IDENT_REGEX + ")?\\((" + DESC_REGEX + "*)\\)(" + DESC_REGEX + ")?)";
  public static final Pattern METHOD_DESC_PATTERN = Pattern.compile(METHOD_DESC_REGEX);
  public static final Pattern GETTER_METHOD_DESC_PATTERN = Pattern.compile("get([A-Z][_a-zA-Z0-9]*)\\(\\)(" + DESC_REGEX + ")");
  public static final Pattern SETTER_METHOD_DESC_PATTERN = Pattern.compile("set([A-Z][_a-zA-Z0-9]*)\\((" + DESC_REGEX + ")\\)V");
  public static final Pattern IS_HAS_CAN_METHOD_DESC_PATTERN = Pattern.compile("(?:is|has|can)([A-Z][_a-zA-Z0-9]*)\\(\\)Z");
  /**
   * Naming prefix for CGLIB-renamed methods.
   *
   * @see #isCglibRenamedMethod
   */
  private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";
  private static final Method[] NO_METHODS = {};
  private static final Field[] NO_FIELDS = {};
  /**
   * Cache for {@link Class#getDeclaredMethods()} plus equivalent default methods from Java 8 based interfaces, allowing for fast iteration.
   */
  private static final Map<Class<?>, Method[]> declaredMethodsCache =
      new ConcurrentReferenceHashMap<Class<?>, Method[]>(256);
  /**
   * Cache for {@link Class#getDeclaredFields()}, allowing for fast iteration.
   */
  private static final Map<Class<?>, Field[]> declaredFieldsCache =
      new ConcurrentReferenceHashMap<Class<?>, Field[]>(256);
  private static final ConcurrentMap<String, Class<?>> DESC_CLASS_CACHE = new ConcurrentHashMap<String, Class<?>>();
  private static final ConcurrentMap<String, Class<?>> NAME_CLASS_CACHE = new ConcurrentHashMap<String, Class<?>>();
  private static final ConcurrentMap<String, Method> Signature_METHODS_CACHE = new ConcurrentHashMap<String, Method>();

  /**
   * Attempt to find a {@link Field field} on the supplied {@link Class} with the supplied {@code name}. Searches all superclasses up to {@link Object}.
   *
   * @param clazz the class to introspect
   * @param name the name of the field
   * @return the corresponding Field object, or {@code null} if not found
   */
  public static Field findField(Class<?> clazz, String name) {
    return findField(clazz, name, null);
  }

  /**
   * Attempt to find a {@link Field field} on the supplied {@link Class} with the supplied {@code name} and/or {@link Class type}. Searches all superclasses up to {@link Object}.
   *
   * @param clazz the class to introspect
   * @param name the name of the field (may be {@code null} if type is specified)
   * @param type the type of the field (may be {@code null} if name is specified)
   * @return the corresponding Field object, or {@code null} if not found
   */
  public static Field findField(Class<?> clazz, String name, Class<?> type) {
    Assert.notNull(clazz, "Class must not be null");
    Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
    Class<?> searchType = clazz;
    while (Object.class != searchType && searchType != null) {
      Field[] fields = getDeclaredFields(searchType);
      for (Field field : fields) {
        if ((name == null || name.equals(field.getName())) &&
            (type == null || type.equals(field.getType()))) {
          return field;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  /**
   * Set the field represented by the supplied {@link Field field object} on the specified {@link Object target object} to the specified {@code value}. In accordance with {@link
   * Field#set(Object, Object)} semantics, the new value is automatically unwrapped if the underlying field has a primitive type.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
   *
   * @param field the field to set
   * @param target the target object on which to set the field
   * @param value the value to set (may be {@code null})
   */
  public static void setField(Field field, Object target, Object value) {
    try {
      field.set(target, value);
    } catch (IllegalAccessException ex) {
      handleReflectionException(ex);
      throw new IllegalStateException(
          "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
    }
  }

  /**
   * Get the field represented by the supplied {@link Field field object} on the specified {@link Object target object}. In accordance with {@link Field#get(Object)} semantics, the
   * returned value is automatically wrapped if the underlying field has a primitive type.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
   *
   * @param field the field to get
   * @param target the target object from which to get the field
   * @return the field's current value
   */
  public static Object getField(Field field, Object target) {
    try {
      return field.get(target);
    } catch (IllegalAccessException ex) {
      handleReflectionException(ex);
      throw new IllegalStateException(
          "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
    }
  }

  /**
   * Attempt to find a {@link Method} on the supplied class with the supplied name and no parameters. Searches all superclasses up to {@code Object}.
   * <p>Returns {@code null} if no {@link Method} can be found.
   *
   * @param clazz the class to introspect
   * @param name the name of the method
   * @return the Method object, or {@code null} if none found
   */
  public static Method findMethod(Class<?> clazz, String name) {
    return findMethod(clazz, name, new Class<?>[0]);
  }

  /**
   * Attempt to find a {@link Method} on the supplied class with the supplied name and parameter types. Searches all superclasses up to {@code Object}.
   * <p>Returns {@code null} if no {@link Method} can be found.
   *
   * @param clazz the class to introspect
   * @param name the name of the method
   * @param paramTypes the parameter types of the method (may be {@code null} to indicate any signature)
   * @return the Method object, or {@code null} if none found
   */
  public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
    Assert.notNull(clazz, "Class must not be null");
    Assert.notNull(name, "Method name must not be null");
    Class<?> searchType = clazz;
    while (searchType != null) {
      Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
      for (Method method : methods) {
        if (name.equals(method.getName()) &&
            (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
          return method;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with no arguments. The target object can be {@code null} when invoking a static {@link Method}.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   *
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @return the invocation result, if any
   * @see #invokeMethod(Method, Object, Object[])
   */
  public static Object invokeMethod(Method method, Object target) {
    return invokeMethod(method, target, new Object[0]);
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with the supplied arguments. The target object can be {@code null} when invoking a static {@link
   * Method}.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   *
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @param args the invocation arguments (may be {@code null})
   * @return the invocation result, if any
   */
  public static Object invokeMethod(Method method, Object target, Object... args) {
    try {
      return method.invoke(target, args);
    } catch (Exception ex) {
      handleReflectionException(ex);
    }
    throw new IllegalStateException("Should never get here");
  }

  /**
   * Invoke the specified JDBC API {@link Method} against the supplied target object with no arguments.
   *
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @return the invocation result, if any
   * @throws SQLException the JDBC API SQLException to rethrow (if any)
   * @see #invokeJdbcMethod(Method, Object, Object[])
   */
  public static Object invokeJdbcMethod(Method method, Object target) throws SQLException {
    return invokeJdbcMethod(method, target, new Object[0]);
  }

  /**
   * Invoke the specified JDBC API {@link Method} against the supplied target object with the supplied arguments.
   *
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @param args the invocation arguments (may be {@code null})
   * @return the invocation result, if any
   * @throws SQLException the JDBC API SQLException to rethrow (if any)
   * @see #invokeMethod(Method, Object, Object[])
   */
  public static Object invokeJdbcMethod(Method method, Object target, Object... args) throws SQLException {
    try {
      return method.invoke(target, args);
    } catch (IllegalAccessException ex) {
      handleReflectionException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getTargetException() instanceof SQLException) {
        throw (SQLException) ex.getTargetException();
      }
      handleInvocationTargetException(ex);
    }
    throw new IllegalStateException("Should never get here");
  }

  /**
   * Handle the given reflection exception. Should only be called if no checked exception is expected to be thrown by the target method.
   * <p>Throws the underlying RuntimeException or Error in case of an
   * InvocationTargetException with such a root cause. Throws an IllegalStateException with an appropriate message or UndeclaredThrowableException otherwise.
   *
   * @param ex the reflection exception to handle
   */
  public static void handleReflectionException(Exception ex) {
    if (ex instanceof NoSuchMethodException) {
      throw new IllegalStateException("Method not found: " + ex.getMessage());
    }
    if (ex instanceof IllegalAccessException) {
      throw new IllegalStateException("Could not access method: " + ex.getMessage());
    }
    if (ex instanceof InvocationTargetException) {
      handleInvocationTargetException((InvocationTargetException) ex);
    }
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    throw new UndeclaredThrowableException(ex);
  }

  /**
   * Handle the given invocation target exception. Should only be called if no checked exception is expected to be thrown by the target method.
   * <p>Throws the underlying RuntimeException or Error in case of such a root
   * cause. Throws an UndeclaredThrowableException otherwise.
   *
   * @param ex the invocation target exception to handle
   */
  public static void handleInvocationTargetException(InvocationTargetException ex) {
    rethrowRuntimeException(ex.getTargetException());
  }

  /**
   * Rethrow the given {@link Throwable exception}, which is presumably the
   * <em>target exception</em> of an {@link InvocationTargetException}.
   * Should only be called if no checked exception is expected to be thrown by the target method.
   * <p>Rethrows the underlying exception cast to a {@link RuntimeException} or
   * {@link Error} if appropriate; otherwise, throws an {@link UndeclaredThrowableException}.
   *
   * @param ex the exception to rethrow
   * @throws RuntimeException the rethrown exception
   */
  public static void rethrowRuntimeException(Throwable ex) {
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    throw new UndeclaredThrowableException(ex);
  }

  /**
   * Rethrow the given {@link Throwable exception}, which is presumably the
   * <em>target exception</em> of an {@link InvocationTargetException}.
   * Should only be called if no checked exception is expected to be thrown by the target method.
   * <p>Rethrows the underlying exception cast to an {@link Exception} or
   * {@link Error} if appropriate; otherwise, throws an {@link UndeclaredThrowableException}.
   *
   * @param ex the exception to rethrow
   * @throws Exception the rethrown exception (in case of a checked exception)
   */
  public static void rethrowException(Throwable ex) throws Exception {
    if (ex instanceof Exception) {
      throw (Exception) ex;
    }
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    throw new UndeclaredThrowableException(ex);
  }

  /**
   * Determine whether the given method explicitly declares the given exception or one of its superclasses, which means that an exception of that type can be propagated as-is
   * within a reflective invocation.
   *
   * @param method the declaring method
   * @param exceptionType the exception to throw
   * @return {@code true} if the exception can be thrown as-is; {@code false} if it needs to be wrapped
   */
  public static boolean declaresException(Method method, Class<?> exceptionType) {
    Assert.notNull(method, "Method must not be null");
    Class<?>[] declaredExceptions = method.getExceptionTypes();
    for (Class<?> declaredException : declaredExceptions) {
      if (declaredException.isAssignableFrom(exceptionType)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determine whether the given field is a "public static final" constant.
   *
   * @param field the field to check
   */
  public static boolean isPublicStaticFinal(Field field) {
    int modifiers = field.getModifiers();
    return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
  }

  /**
   * Determine whether the given method is an "equals" method.
   *
   * @see Object#equals(Object)
   */
  public static boolean isEqualsMethod(Method method) {
    if (method == null || !method.getName().equals("equals")) {
      return false;
    }
    Class<?>[] paramTypes = method.getParameterTypes();
    return (paramTypes.length == 1 && paramTypes[0] == Object.class);
  }

  /**
   * Determine whether the given method is a "hashCode" method.
   *
   * @see Object#hashCode()
   */
  public static boolean isHashCodeMethod(Method method) {
    return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
  }

  /**
   * Determine whether the given method is a "toString" method.
   *
   * @see Object#toString()
   */
  public static boolean isToStringMethod(Method method) {
    return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
  }

  /**
   * Determine whether the given method is originally declared by {@link Object}.
   */
  public static boolean isObjectMethod(Method method) {
    if (method == null) {
      return false;
    }
    try {
      Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * Determine whether the given method is a CGLIB 'renamed' method, following the pattern "CGLIB$methodName$0".
   *
   * @param renamedMethod the method to check
   */
  public static boolean isCglibRenamedMethod(Method renamedMethod) {
    String name = renamedMethod.getName();
    if (name.startsWith(CGLIB_RENAMED_METHOD_PREFIX)) {
      int i = name.length() - 1;
      while (i >= 0 && Character.isDigit(name.charAt(i))) {
        i--;
      }
      return ((i > CGLIB_RENAMED_METHOD_PREFIX.length()) &&
          (i < name.length() - 1) && name.charAt(i) == '$');
    }
    return false;
  }

  /**
   * Make the given field accessible, explicitly setting it accessible if necessary. The {@code setAccessible(true)} method is only called when actually necessary, to avoid
   * unnecessary conflicts with a JVM SecurityManager (if active).
   *
   * @param field the field to make accessible
   * @see Field#setAccessible
   */
  public static void makeAccessible(Field field) {
    if ((!Modifier.isPublic(field.getModifiers()) ||
        !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
        Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
      field.setAccessible(true);
    }
  }

  /**
   * Make the given method accessible, explicitly setting it accessible if necessary. The {@code setAccessible(true)} method is only called when actually necessary, to avoid
   * unnecessary conflicts with a JVM SecurityManager (if active).
   *
   * @param method the method to make accessible
   * @see Method#setAccessible
   */
  public static void makeAccessible(Method method) {
    if ((!Modifier.isPublic(method.getModifiers()) ||
        !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
      method.setAccessible(true);
    }
  }

  /**
   * Make the given constructor accessible, explicitly setting it accessible if necessary. The {@code setAccessible(true)} method is only called when actually necessary, to avoid
   * unnecessary conflicts with a JVM SecurityManager (if active).
   *
   * @param ctor the constructor to make accessible
   * @see Constructor#setAccessible
   */
  public static void makeAccessible(Constructor<?> ctor) {
    if ((!Modifier.isPublic(ctor.getModifiers()) ||
        !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
      ctor.setAccessible(true);
    }
  }

  /**
   * Perform the given callback operation on all matching methods of the given class, as locally declared or equivalent thereof (such as default methods on Java 8 based interfaces
   * that the given class implements).
   *
   * @param clazz the class to introspect
   * @param mc the callback to invoke for each method
   * @see #doWithMethods
   * @since 4.2
   */
  public static void doWithLocalMethods(Class<?> clazz, MethodCallback mc) {
    Method[] methods = getDeclaredMethods(clazz);
    for (Method method : methods) {
      try {
        mc.doWith(method);
      } catch (IllegalAccessException ex) {
        throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
      }
    }
  }

  /**
   * Perform the given callback operation on all matching methods of the given class and superclasses.
   * <p>The same named method occurring on subclass and superclass will appear
   * twice, unless excluded by a {@link MethodFilter}.
   *
   * @param clazz the class to introspect
   * @param mc the callback to invoke for each method
   * @see #doWithMethods(Class, MethodCallback, MethodFilter)
   */
  public static void doWithMethods(Class<?> clazz, MethodCallback mc) {
    doWithMethods(clazz, mc, null);
  }

  /**
   * Perform the given callback operation on all matching methods of the given class and superclasses (or given interface and super-interfaces).
   * <p>The same named method occurring on subclass and superclass will appear
   * twice, unless excluded by the specified {@link MethodFilter}.
   *
   * @param clazz the class to introspect
   * @param mc the callback to invoke for each method
   * @param mf the filter that determines the methods to apply the callback to
   */
  public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf) {
    // Keep backing up the inheritance hierarchy.
    Method[] methods = getDeclaredMethods(clazz);
    for (Method method : methods) {
      if (mf != null && !mf.matches(method)) {
        continue;
      }
      try {
        mc.doWith(method);
      } catch (IllegalAccessException ex) {
        throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
      }
    }
    if (clazz.getSuperclass() != null) {
      doWithMethods(clazz.getSuperclass(), mc, mf);
    } else if (clazz.isInterface()) {
      for (Class<?> superIfc : clazz.getInterfaces()) {
        doWithMethods(superIfc, mc, mf);
      }
    }
  }

  /**
   * Get all declared methods on the leaf class and all superclasses. Leaf class methods are included first.
   *
   * @param leafClass the class to introspect
   */
  public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
    final List<Method> methods = new ArrayList<Method>(32);
    doWithMethods(leafClass, new MethodCallback() {
      @Override
      public void doWith(Method method) {
        methods.add(method);
      }
    });
    return methods.toArray(new Method[methods.size()]);
  }

  /**
   * Get the unique set of declared methods on the leaf class and all superclasses. Leaf class methods are included first and while traversing the superclass hierarchy any methods
   * found with signatures matching a method already included are filtered out.
   *
   * @param leafClass the class to introspect
   */
  public static Method[] getUniqueDeclaredMethods(Class<?> leafClass) {
    final List<Method> methods = new ArrayList<Method>(32);
    doWithMethods(leafClass, new MethodCallback() {
      @Override
      public void doWith(Method method) {
        boolean knownSignature = false;
        Method methodBeingOverriddenWithCovariantReturnType = null;
        for (Method existingMethod : methods) {
          if (method.getName().equals(existingMethod.getName()) &&
              Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())) {
            // Is this a covariant return type situation?
            if (existingMethod.getReturnType() != method.getReturnType() &&
                existingMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
              methodBeingOverriddenWithCovariantReturnType = existingMethod;
            } else {
              knownSignature = true;
            }
            break;
          }
        }
        if (methodBeingOverriddenWithCovariantReturnType != null) {
          methods.remove(methodBeingOverriddenWithCovariantReturnType);
        }
        if (!knownSignature && !isCglibRenamedMethod(method)) {
          methods.add(method);
        }
      }
    });
    return methods.toArray(new Method[methods.size()]);
  }

  /**
   * This variant retrieves {@link Class#getDeclaredMethods()} from a local cache in order to avoid the JVM's SecurityManager check and defensive array copying. In addition, it
   * also includes Java 8 default methods from locally implemented interfaces, since those are effectively to be treated just like declared methods.
   *
   * @param clazz the class to introspect
   * @return the cached array of methods
   * @see Class#getDeclaredMethods()
   */
  private static Method[] getDeclaredMethods(Class<?> clazz) {
    Assert.notNull(clazz, "Class must not be null");
    Method[] result = declaredMethodsCache.get(clazz);
    if (result == null) {
      Method[] declaredMethods = clazz.getDeclaredMethods();
      List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
      if (defaultMethods != null) {
        result = new Method[declaredMethods.length + defaultMethods.size()];
        System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
        int index = declaredMethods.length;
        for (Method defaultMethod : defaultMethods) {
          result[index] = defaultMethod;
          index++;
        }
      } else {
        result = declaredMethods;
      }
      declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
    }
    return result;
  }

  private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
    List<Method> result = null;
    for (Class<?> ifc : clazz.getInterfaces()) {
      for (Method ifcMethod : ifc.getMethods()) {
        if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
          if (result == null) {
            result = new LinkedList<Method>();
          }
          result.add(ifcMethod);
        }
      }
    }
    return result;
  }

  /**
   * Invoke the given callback on all fields in the target class, going up the class hierarchy to get all declared fields.
   *
   * @param clazz the target class to analyze
   * @param fc the callback to invoke for each field
   * @see #doWithFields
   * @since 4.2
   */
  public static void doWithLocalFields(Class<?> clazz, FieldCallback fc) {
    for (Field field : getDeclaredFields(clazz)) {
      try {
        fc.doWith(field);
      } catch (IllegalAccessException ex) {
        throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
      }
    }
  }

  /**
   * Invoke the given callback on all fields in the target class, going up the class hierarchy to get all declared fields.
   *
   * @param clazz the target class to analyze
   * @param fc the callback to invoke for each field
   */
  public static void doWithFields(Class<?> clazz, FieldCallback fc) {
    doWithFields(clazz, fc, null);
  }

  /**
   * Invoke the given callback on all fields in the target class, going up the class hierarchy to get all declared fields.
   *
   * @param clazz the target class to analyze
   * @param fc the callback to invoke for each field
   * @param ff the filter that determines the fields to apply the callback to
   */
  public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff) {
    // Keep backing up the inheritance hierarchy.
    Class<?> targetClass = clazz;
    do {
      Field[] fields = getDeclaredFields(targetClass);
      for (Field field : fields) {
        if (ff != null && !ff.matches(field)) {
          continue;
        }
        try {
          fc.doWith(field);
        } catch (IllegalAccessException ex) {
          throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
        }
      }
      targetClass = targetClass.getSuperclass();
    }
    while (targetClass != null && targetClass != Object.class);
  }

  /**
   * This variant retrieves {@link Class#getDeclaredFields()} from a local cache in order to avoid the JVM's SecurityManager check and defensive array copying.
   *
   * @param clazz the class to introspect
   * @return the cached array of fields
   * @see Class#getDeclaredFields()
   */
  private static Field[] getDeclaredFields(Class<?> clazz) {
    Assert.notNull(clazz, "Class must not be null");
    Field[] result = declaredFieldsCache.get(clazz);
    if (result == null) {
      result = clazz.getDeclaredFields();
      declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
    }
    return result;
  }

  /**
   * Given the source object and the destination, which must be the same class or a subclass, copy all fields, including inherited fields. Designed to work on objects with public
   * no-arg constructors.
   */
  public static void shallowCopyFieldState(final Object src, final Object dest) {
    Assert.notNull(src, "Source for field copy cannot be null");
    Assert.notNull(dest, "Destination for field copy cannot be null");
    if (!src.getClass().isAssignableFrom(dest.getClass())) {
      throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() +
          "] must be same or subclass as source class [" + src.getClass().getName() + "]");
    }
    doWithFields(src.getClass(), new FieldCallback() {
      @Override
      public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        makeAccessible(field);
        Object srcValue = field.get(src);
        field.set(dest, srcValue);
      }
    }, COPYABLE_FIELDS);
  }

  /**
   * Clear the internal method/field cache.
   *
   * @since 4.2.4
   */
  public static void clearCache() {
    declaredMethodsCache.clear();
    declaredFieldsCache.clear();
  }

  public static boolean isPrimitives(Class<?> cls) {
    if (cls.isArray()) {
      return isPrimitive(cls.getComponentType());
    }
    return isPrimitive(cls);
  }

  public static boolean isPrimitive(Class<?> cls) {
    return cls.isPrimitive() || cls == String.class || cls == Boolean.class || cls == Character.class
        || Number.class.isAssignableFrom(cls) || Date.class.isAssignableFrom(cls);
  }

  public static Class<?> getBoxedClass(Class<?> c) {
    if (c == int.class) {
      c = Integer.class;
    } else if (c == boolean.class) {
      c = Boolean.class;
    } else if (c == long.class) {
      c = Long.class;
    } else if (c == float.class) {
      c = Float.class;
    } else if (c == double.class) {
      c = Double.class;
    } else if (c == char.class) {
      c = Character.class;
    } else if (c == byte.class) {
      c = Byte.class;
    } else if (c == short.class) {
      c = Short.class;
    }
    return c;
  }

  /**
   * is compatible.
   *
   * @param c class.
   * @param o instance.
   * @return compatible or not.
   */
  public static boolean isCompatible(Class<?> c, Object o) {
    boolean pt = c.isPrimitive();
    if (o == null) {
      return !pt;
    }

    if (pt) {
      c = getBoxedClass(c);
    }

    return c == o.getClass() || c.isInstance(o);
  }

  /**
   * is compatible.
   *
   * @param cs class array.
   * @param os object array.
   * @return compatible or not.
   */
  public static boolean isCompatible(Class<?>[] cs, Object[] os) {
    int len = cs.length;
    if (len != os.length) {
      return false;
    }
    if (len == 0) {
      return true;
    }
    for (int i = 0; i < len; i++) {
      if (!isCompatible(cs[i], os[i])) {
        return false;
      }
    }
    return true;
  }

  public static String getCodeBase(Class<?> cls) {
    if (cls == null) {
      return null;
    }
    ProtectionDomain domain = cls.getProtectionDomain();
    if (domain == null) {
      return null;
    }
    CodeSource source = domain.getCodeSource();
    if (source == null) {
      return null;
    }
    URL location = source.getLocation();
    if (location == null) {
      return null;
    }
    return location.getFile();
  }

  /**
   * get name. java.lang.Object[][].class => "java.lang.Object[][]"
   *
   * @param c class.
   * @return name.
   */
  public static String getName(Class<?> c) {
    if (c.isArray()) {
      StringBuilder sb = new StringBuilder();
      do {
        sb.append("[]");
        c = c.getComponentType();
      }
      while (c.isArray());

      return c.getName() + sb.toString();
    }
    return c.getName();
  }

  public static Class<?> getGenericClass(Class<?> cls) {
    return getGenericClass(cls, 0);
  }

  public static Class<?> getGenericClass(Class<?> cls, int i) {
    try {
      ParameterizedType parameterizedType = ((ParameterizedType) cls.getGenericInterfaces()[0]);
      Object genericClass = parameterizedType.getActualTypeArguments()[i];
      if (genericClass instanceof ParameterizedType) { // handle nested generic type
        return (Class<?>) ((ParameterizedType) genericClass).getRawType();
      } else if (genericClass instanceof GenericArrayType) { // handle array generic type
        return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
      } else if (((Class) genericClass).isArray()) {
        // Requires JDK 7 or higher, Foo<int[]> is no longer GenericArrayType
        return ((Class) genericClass).getComponentType();
      } else {
        return (Class<?>) genericClass;
      }
    } catch (Throwable e) {
      throw new IllegalArgumentException(cls.getName()
          + " generic type undefined!", e);
    }
  }

  /**
   * get method name. "void do(int)", "void do()", "int do(java.lang.String,boolean)"
   *
   * @param m method.
   * @return name.
   */
  public static String getName(final Method m) {
    StringBuilder ret = new StringBuilder();
    ret.append(getName(m.getReturnType())).append(' ');
    ret.append(m.getName()).append('(');
    Class<?>[] parameterTypes = m.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      if (i > 0) {
        ret.append(',');
      }
      ret.append(getName(parameterTypes[i]));
    }
    ret.append(')');
    return ret.toString();
  }

  public static String getSignature(String methodName, Class<?>[] parameterTypes) {
    StringBuilder sb = new StringBuilder(methodName);
    sb.append("(");
    if (parameterTypes != null && parameterTypes.length > 0) {
      boolean first = true;
      for (Class<?> type : parameterTypes) {
        if (first) {
          first = false;
        } else {
          sb.append(",");
        }
        sb.append(type.getName());
      }
    }
    sb.append(")");
    return sb.toString();
  }

  /**
   * get constructor name. "()", "(java.lang.String,int)"
   *
   * @param c constructor.
   * @return name.
   */
  public static String getName(final Constructor<?> c) {
    StringBuilder ret = new StringBuilder("(");
    Class<?>[] parameterTypes = c.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      if (i > 0) {
        ret.append(',');
      }
      ret.append(getName(parameterTypes[i]));
    }
    ret.append(')');
    return ret.toString();
  }

  /**
   * get class desc. boolean[].class => "[Z" Object.class => "Ljava/lang/Object;"
   *
   * @param c class.
   * @return desc.
   */
  public static String getDesc(Class<?> c) {
    StringBuilder ret = new StringBuilder();

    while (c.isArray()) {
      ret.append('[');
      c = c.getComponentType();
    }

    if (c.isPrimitive()) {
      String t = c.getName();
      if ("void".equals(t)) {
        ret.append(JVM_VOID);
      } else if ("boolean".equals(t)) {
        ret.append(JVM_BOOLEAN);
      } else if ("byte".equals(t)) {
        ret.append(JVM_BYTE);
      } else if ("char".equals(t)) {
        ret.append(JVM_CHAR);
      } else if ("double".equals(t)) {
        ret.append(JVM_DOUBLE);
      } else if ("float".equals(t)) {
        ret.append(JVM_FLOAT);
      } else if ("int".equals(t)) {
        ret.append(JVM_INT);
      } else if ("long".equals(t)) {
        ret.append(JVM_LONG);
      } else if ("short".equals(t)) {
        ret.append(JVM_SHORT);
      }
    } else {
      ret.append('L');
      ret.append(c.getName().replace('.', '/'));
      ret.append(';');
    }
    return ret.toString();
  }

  /**
   * get class array desc. [int.class, boolean[].class, Object.class] => "I[ZLjava/lang/Object;"
   *
   * @param cs class array.
   * @return desc.
   */
  public static String getDesc(final Class<?>[] cs) {
    if (cs.length == 0) {
      return "";
    }

    StringBuilder sb = new StringBuilder(64);
    for (Class<?> c : cs) {
      sb.append(getDesc(c));
    }
    return sb.toString();
  }

  /**
   * get method desc. int do(int arg1) => "do(I)I" void do(String arg1,boolean arg2) => "do(Ljava/lang/String;Z)V"
   *
   * @param m method.
   * @return desc.
   */
  public static String getDesc(final Method m) {
    StringBuilder ret = new StringBuilder(m.getName()).append('(');
    Class<?>[] parameterTypes = m.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      ret.append(getDesc(parameterTypes[i]));
    }
    ret.append(')').append(getDesc(m.getReturnType()));
    return ret.toString();
  }

  /**
   * get constructor desc. "()V", "(Ljava/lang/String;I)V"
   *
   * @param c constructor.
   * @return desc
   */
  public static String getDesc(final Constructor<?> c) {
    StringBuilder ret = new StringBuilder("(");
    Class<?>[] parameterTypes = c.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      ret.append(getDesc(parameterTypes[i]));
    }
    ret.append(')').append('V');
    return ret.toString();
  }

  /**
   * get method desc. "(I)I", "()V", "(Ljava/lang/String;Z)V"
   *
   * @param m method.
   * @return desc.
   */
  public static String getDescWithoutMethodName(Method m) {
    StringBuilder ret = new StringBuilder();
    ret.append('(');
    Class<?>[] parameterTypes = m.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      ret.append(getDesc(parameterTypes[i]));
    }
    ret.append(')').append(getDesc(m.getReturnType()));
    return ret.toString();
  }

  /**
   * get class desc. Object.class => "Ljava/lang/Object;" boolean[].class => "[Z"
   *
   * @param c class.
   * @return desc.
   */
  public static String getDesc(final CtClass c) throws NotFoundException {
    StringBuilder ret = new StringBuilder();
    if (c.isArray()) {
      ret.append('[');
      ret.append(getDesc(c.getComponentType()));
    } else if (c.isPrimitive()) {
      String t = c.getName();
      if ("void".equals(t)) {
        ret.append(JVM_VOID);
      } else if ("boolean".equals(t)) {
        ret.append(JVM_BOOLEAN);
      } else if ("byte".equals(t)) {
        ret.append(JVM_BYTE);
      } else if ("char".equals(t)) {
        ret.append(JVM_CHAR);
      } else if ("double".equals(t)) {
        ret.append(JVM_DOUBLE);
      } else if ("float".equals(t)) {
        ret.append(JVM_FLOAT);
      } else if ("int".equals(t)) {
        ret.append(JVM_INT);
      } else if ("long".equals(t)) {
        ret.append(JVM_LONG);
      } else if ("short".equals(t)) {
        ret.append(JVM_SHORT);
      }
    } else {
      ret.append('L');
      ret.append(c.getName().replace('.', '/'));
      ret.append(';');
    }
    return ret.toString();
  }

  /**
   * get method desc. "do(I)I", "do()V", "do(Ljava/lang/String;Z)V"
   *
   * @param m method.
   * @return desc.
   */
  public static String getDesc(final CtMethod m) throws NotFoundException {
    StringBuilder ret = new StringBuilder(m.getName()).append('(');
    CtClass[] parameterTypes = m.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      ret.append(getDesc(parameterTypes[i]));
    }
    ret.append(')').append(getDesc(m.getReturnType()));
    return ret.toString();
  }

  /**
   * get constructor desc. "()V", "(Ljava/lang/String;I)V"
   *
   * @param c constructor.
   * @return desc
   */
  public static String getDesc(final CtConstructor c) throws NotFoundException {
    StringBuilder ret = new StringBuilder("(");
    CtClass[] parameterTypes = c.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      ret.append(getDesc(parameterTypes[i]));
    }
    ret.append(')').append('V');
    return ret.toString();
  }

  /**
   * get method desc. "(I)I", "()V", "(Ljava/lang/String;Z)V".
   *
   * @param m method.
   * @return desc.
   */
  public static String getDescWithoutMethodName(final CtMethod m) throws NotFoundException {
    StringBuilder ret = new StringBuilder();
    ret.append('(');
    CtClass[] parameterTypes = m.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      ret.append(getDesc(parameterTypes[i]));
    }
    ret.append(')').append(getDesc(m.getReturnType()));
    return ret.toString();
  }

  /**
   * name to desc. java.util.Map[][] => "[[Ljava/util/Map;"
   *
   * @param name name.
   * @return desc.
   */
  public static String name2desc(String name) {
    StringBuilder sb = new StringBuilder();
    int c = 0, index = name.indexOf('[');
    if (index > 0) {
      c = (name.length() - index) / 2;
      name = name.substring(0, index);
    }
    while (c-- > 0) {
      sb.append("[");
    }
    if ("void".equals(name)) {
      sb.append(JVM_VOID);
    } else if ("boolean".equals(name)) {
      sb.append(JVM_BOOLEAN);
    } else if ("byte".equals(name)) {
      sb.append(JVM_BYTE);
    } else if ("char".equals(name)) {
      sb.append(JVM_CHAR);
    } else if ("double".equals(name)) {
      sb.append(JVM_DOUBLE);
    } else if ("float".equals(name)) {
      sb.append(JVM_FLOAT);
    } else if ("int".equals(name)) {
      sb.append(JVM_INT);
    } else if ("long".equals(name)) {
      sb.append(JVM_LONG);
    } else if ("short".equals(name)) {
      sb.append(JVM_SHORT);
    } else {
      sb.append('L').append(name.replace('.', '/')).append(';');
    }
    return sb.toString();
  }

  /**
   * desc to name. "[[I" => "int[][]"
   *
   * @param desc desc.
   * @return name.
   */
  public static String desc2name(String desc) {
    StringBuilder sb = new StringBuilder();
    int c = desc.lastIndexOf('[') + 1;
    if (desc.length() == c + 1) {
      switch (desc.charAt(c)) {
        case JVM_VOID: {
          sb.append("void");
          break;
        }
        case JVM_BOOLEAN: {
          sb.append("boolean");
          break;
        }
        case JVM_BYTE: {
          sb.append("byte");
          break;
        }
        case JVM_CHAR: {
          sb.append("char");
          break;
        }
        case JVM_DOUBLE: {
          sb.append("double");
          break;
        }
        case JVM_FLOAT: {
          sb.append("float");
          break;
        }
        case JVM_INT: {
          sb.append("int");
          break;
        }
        case JVM_LONG: {
          sb.append("long");
          break;
        }
        case JVM_SHORT: {
          sb.append("short");
          break;
        }
        default:
          throw new RuntimeException();
      }
    } else {
      sb.append(desc.substring(c + 1, desc.length() - 1).replace('/', '.'));
    }
    while (c-- > 0) {
      sb.append("[]");
    }
    return sb.toString();
  }

  public static Class<?> forName(String name) {
    try {
      return name2class(name);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Not found class " + name + ", cause: " + e.getMessage(), e);
    }
  }

  public static Class<?> forName(ClassLoader cl, String name) {
    try {
      return name2class(cl, name);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Not found class " + name + ", cause: " + e.getMessage(), e);
    }
  }

  /**
   * name to class. "boolean" => boolean.class "java.util.Map[][]" => java.util.Map[][].class
   *
   * @param name name.
   * @return Class instance.
   */
  public static Class<?> name2class(String name) throws ClassNotFoundException {
    return name2class(ClassUtils.getClassLoader(), name);
  }

  /**
   * name to class. "boolean" => boolean.class "java.util.Map[][]" => java.util.Map[][].class
   *
   * @param cl ClassLoader instance.
   * @param name name.
   * @return Class instance.
   */
  private static Class<?> name2class(ClassLoader cl, String name) throws ClassNotFoundException {
    int c = 0, index = name.indexOf('[');
    if (index > 0) {
      c = (name.length() - index) / 2;
      name = name.substring(0, index);
    }
    if (c > 0) {
      StringBuilder sb = new StringBuilder();
      while (c-- > 0) {
        sb.append("[");
      }

      if ("void".equals(name)) {
        sb.append(JVM_VOID);
      } else if ("boolean".equals(name)) {
        sb.append(JVM_BOOLEAN);
      } else if ("byte".equals(name)) {
        sb.append(JVM_BYTE);
      } else if ("char".equals(name)) {
        sb.append(JVM_CHAR);
      } else if ("double".equals(name)) {
        sb.append(JVM_DOUBLE);
      } else if ("float".equals(name)) {
        sb.append(JVM_FLOAT);
      } else if ("int".equals(name)) {
        sb.append(JVM_INT);
      } else if ("long".equals(name)) {
        sb.append(JVM_LONG);
      } else if ("short".equals(name)) {
        sb.append(JVM_SHORT);
      } else {
        sb.append('L').append(name).append(';'); // "java.lang.Object" ==> "Ljava.lang.Object;"
      }
      name = sb.toString();
    } else {
      if ("void".equals(name)) {
        return void.class;
      } else if ("boolean".equals(name)) {
        return boolean.class;
      } else if ("byte".equals(name)) {
        return byte.class;
      } else if ("char".equals(name)) {
        return char.class;
      } else if ("double".equals(name)) {
        return double.class;
      } else if ("float".equals(name)) {
        return float.class;
      } else if ("int".equals(name)) {
        return int.class;
      } else if ("long".equals(name)) {
        return long.class;
      } else if ("short".equals(name)) {
        return short.class;
      }
    }

    if (cl == null) {
      cl = ClassUtils.getClassLoader();
    }
    Class<?> clazz = NAME_CLASS_CACHE.get(name);
    if (clazz == null) {
      clazz = Class.forName(name, true, cl);
      NAME_CLASS_CACHE.put(name, clazz);
    }
    return clazz;
  }

  /**
   * desc to class. "[Z" => boolean[].class "[[Ljava/util/Map;" => java.util.Map[][].class
   *
   * @param desc desc.
   * @return Class instance.
   */
  public static Class<?> desc2class(String desc) throws ClassNotFoundException {
    return desc2class(ClassUtils.getClassLoader(), desc);
  }

  /**
   * desc to class. "[Z" => boolean[].class "[[Ljava/util/Map;" => java.util.Map[][].class
   *
   * @param cl ClassLoader instance.
   * @param desc desc.
   * @return Class instance.
   */
  private static Class<?> desc2class(ClassLoader cl, String desc) throws ClassNotFoundException {
    switch (desc.charAt(0)) {
      case JVM_VOID:
        return void.class;
      case JVM_BOOLEAN:
        return boolean.class;
      case JVM_BYTE:
        return byte.class;
      case JVM_CHAR:
        return char.class;
      case JVM_DOUBLE:
        return double.class;
      case JVM_FLOAT:
        return float.class;
      case JVM_INT:
        return int.class;
      case JVM_LONG:
        return long.class;
      case JVM_SHORT:
        return short.class;
      case 'L':
        desc = desc.substring(1, desc.length() - 1).replace('/', '.'); // "Ljava/lang/Object;" ==> "java.lang.Object"
        break;
      case '[':
        desc = desc.replace('/', '.');  // "[[Ljava/lang/Object;" ==> "[[Ljava.lang.Object;"
        break;
      default:
        throw new ClassNotFoundException("Class not found: " + desc);
    }

    if (cl == null) {
      cl = ClassUtils.getClassLoader();
    }
    Class<?> clazz = DESC_CLASS_CACHE.get(desc);
    if (clazz == null) {
      clazz = Class.forName(desc, true, cl);
      DESC_CLASS_CACHE.put(desc, clazz);
    }
    return clazz;
  }

  /**
   * get class array instance.
   *
   * @param desc desc.
   * @return Class class array.
   */
  public static Class<?>[] desc2classArray(String desc) throws ClassNotFoundException {
    Class<?>[] ret = desc2classArray(ClassUtils.getClassLoader(), desc);
    return ret;
  }

  /**
   * get class array instance.
   *
   * @param cl ClassLoader instance.
   * @param desc desc.
   * @return Class[] class array.
   */
  private static Class<?>[] desc2classArray(ClassLoader cl, String desc) throws ClassNotFoundException {
    if (desc.length() == 0) {
      return EMPTY_CLASS_ARRAY;
    }

    List<Class<?>> cs = new ArrayList<Class<?>>();
    Matcher m = DESC_PATTERN.matcher(desc);
    while (m.find()) {
      cs.add(desc2class(cl, m.group()));
    }
    return cs.toArray(EMPTY_CLASS_ARRAY);
  }

  /**
   * Find method from method signature
   *
   * @param clazz Target class to find method
   * @param methodName Method signature, e.g.: method1(int, String). It is allowed to provide method name only, e.g.: method2
   * @return target method
   * @throws IllegalStateException when multiple methods are found (overridden method when parameter info is not provided)
   */
  public static Method findMethodByMethodSignature(Class<?> clazz, String methodName, String[] parameterTypes)
      throws NoSuchMethodException, ClassNotFoundException {
    String signature = clazz.getName() + "." + methodName;
    if (parameterTypes != null && parameterTypes.length > 0) {
      signature += StringUtils.join(parameterTypes);
    }
    Method method = Signature_METHODS_CACHE.get(signature);
    if (method != null) {
      return method;
    }
    if (parameterTypes == null) {
      List<Method> finded = new ArrayList<Method>();
      for (Method m : clazz.getMethods()) {
        if (m.getName().equals(methodName)) {
          finded.add(m);
        }
      }
      if (finded.isEmpty()) {
        throw new NoSuchMethodException("No such method " + methodName + " in class " + clazz);
      }
      if (finded.size() > 1) {
        String msg = String.format("Not unique method for method name(%s) in class(%s), find %d methods.",
            methodName, clazz.getName(), finded.size());
        throw new IllegalStateException(msg);
      }
      method = finded.get(0);
    } else {
      Class<?>[] types = new Class<?>[parameterTypes.length];
      for (int i = 0; i < parameterTypes.length; i++) {
        types[i] = ReflectionUtils.name2class(parameterTypes[i]);
      }
      method = clazz.getMethod(methodName, types);

    }
    Signature_METHODS_CACHE.put(signature, method);
    return method;
  }

  public static Method findMethodByMethodName(Class<?> clazz, String methodName)
      throws NoSuchMethodException, ClassNotFoundException {
    return findMethodByMethodSignature(clazz, methodName, null);
  }

  public static Constructor<?> findConstructor(Class<?> clazz, Class<?> paramType) throws NoSuchMethodException {
    Constructor<?> targetConstructor;
    try {
      targetConstructor = clazz.getConstructor(new Class<?>[]{paramType});
    } catch (NoSuchMethodException e) {
      targetConstructor = null;
      Constructor<?>[] constructors = clazz.getConstructors();
      for (Constructor<?> constructor : constructors) {
        if (Modifier.isPublic(constructor.getModifiers())
            && constructor.getParameterTypes().length == 1
            && constructor.getParameterTypes()[0].isAssignableFrom(paramType)) {
          targetConstructor = constructor;
          break;
        }
      }
      if (targetConstructor == null) {
        throw e;
      }
    }
    return targetConstructor;
  }

  /**
   * Check if one object is the implementation for a given interface.
   * <p>
   * This method will not trigger classloading for the given interface, therefore it will not lead to error when the given interface is not visible by the classloader
   *
   * @param obj Object to examine
   * @param interfaceClazzName The given interface
   * @return true if the object implements the given interface, otherwise return false
   */
  public static boolean isInstance(Object obj, String interfaceClazzName) {
    for (Class<?> clazz = obj.getClass();
        clazz != null && !clazz.equals(Object.class);
        clazz = clazz.getSuperclass()) {
      Class<?>[] interfaces = clazz.getInterfaces();
      for (Class<?> itf : interfaces) {
        if (itf.getName().equals(interfaceClazzName)) {
          return true;
        }
      }
    }
    return false;
  }

  public static Object getEmptyObject(Class<?> returnType) {
    return getEmptyObject(returnType, new HashMap<Class<?>, Object>(), 0);
  }

  private static Object getEmptyObject(Class<?> returnType, Map<Class<?>, Object> emptyInstances, int level) {
    if (level > 2) {
      return null;
    }
    if (returnType == null) {
      return null;
    } else if (returnType == boolean.class || returnType == Boolean.class) {
      return false;
    } else if (returnType == char.class || returnType == Character.class) {
      return '\0';
    } else if (returnType == byte.class || returnType == Byte.class) {
      return (byte) 0;
    } else if (returnType == short.class || returnType == Short.class) {
      return (short) 0;
    } else if (returnType == int.class || returnType == Integer.class) {
      return 0;
    } else if (returnType == long.class || returnType == Long.class) {
      return 0L;
    } else if (returnType == float.class || returnType == Float.class) {
      return 0F;
    } else if (returnType == double.class || returnType == Double.class) {
      return 0D;
    } else if (returnType.isArray()) {
      return Array.newInstance(returnType.getComponentType(), 0);
    } else if (returnType.isAssignableFrom(ArrayList.class)) {
      return new ArrayList<Object>(0);
    } else if (returnType.isAssignableFrom(HashSet.class)) {
      return new HashSet<Object>(0);
    } else if (returnType.isAssignableFrom(HashMap.class)) {
      return new HashMap<Object, Object>(0);
    } else if (String.class.equals(returnType)) {
      return "";
    } else if (!returnType.isInterface()) {
      try {
        Object value = emptyInstances.get(returnType);
        if (value == null) {
          value = returnType.newInstance();
          emptyInstances.put(returnType, value);
        }
        Class<?> cls = value.getClass();
        while (cls != null && cls != Object.class) {
          Field[] fields = cls.getDeclaredFields();
          for (Field field : fields) {
            if (field.isSynthetic()) {
              continue;
            }
            Object property = getEmptyObject(field.getType(), emptyInstances, level + 1);
            if (property != null) {
              try {
                if (!field.isAccessible()) {
                  field.setAccessible(true);
                }
                field.set(value, property);
              } catch (Throwable e) {
              }
            }
          }
          cls = cls.getSuperclass();
        }
        return value;
      } catch (Throwable e) {
        return null;
      }
    } else {
      return null;
    }
  }

  public static boolean isBeanPropertyReadMethod(Method method) {
    return method != null
        && Modifier.isPublic(method.getModifiers())
        && !Modifier.isStatic(method.getModifiers())
        && method.getReturnType() != void.class
        && method.getDeclaringClass() != Object.class
        && method.getParameterTypes().length == 0
        && ((method.getName().startsWith("get") && method.getName().length() > 3)
        || (method.getName().startsWith("is") && method.getName().length() > 2));
  }

  public static String getPropertyNameFromBeanReadMethod(Method method) {
    if (isBeanPropertyReadMethod(method)) {
      if (method.getName().startsWith("get")) {
        return method.getName().substring(3, 4).toLowerCase()
            + method.getName().substring(4);
      }
      if (method.getName().startsWith("is")) {
        return method.getName().substring(2, 3).toLowerCase()
            + method.getName().substring(3);
      }
    }
    return null;
  }

  public static boolean isBeanPropertyWriteMethod(Method method) {
    return method != null
        && Modifier.isPublic(method.getModifiers())
        && !Modifier.isStatic(method.getModifiers())
        && method.getDeclaringClass() != Object.class
        && method.getParameterTypes().length == 1
        && method.getName().startsWith("set")
        && method.getName().length() > 3;
  }

  public static String getPropertyNameFromBeanWriteMethod(Method method) {
    if (isBeanPropertyWriteMethod(method)) {
      return method.getName().substring(3, 4).toLowerCase()
          + method.getName().substring(4);
    }
    return null;
  }

  public static boolean isPublicInstanceField(Field field) {
    return Modifier.isPublic(field.getModifiers())
        && !Modifier.isStatic(field.getModifiers())
        && !Modifier.isFinal(field.getModifiers())
        && !field.isSynthetic();
  }

  public static Map<String, Field> getBeanPropertyFields(Class cl) {
    Map<String, Field> properties = new HashMap<String, Field>();
    for (; cl != null; cl = cl.getSuperclass()) {
      Field[] fields = cl.getDeclaredFields();
      for (Field field : fields) {
        if (Modifier.isTransient(field.getModifiers())
            || Modifier.isStatic(field.getModifiers())) {
          continue;
        }

        field.setAccessible(true);

        properties.put(field.getName(), field);
      }
    }

    return properties;
  }

  public static Map<String, Method> getBeanPropertyReadMethods(Class cl) {
    Map<String, Method> properties = new HashMap<String, Method>();
    for (; cl != null; cl = cl.getSuperclass()) {
      Method[] methods = cl.getDeclaredMethods();
      for (Method method : methods) {
        if (isBeanPropertyReadMethod(method)) {
          method.setAccessible(true);
          String property = getPropertyNameFromBeanReadMethod(method);
          properties.put(property, method);
        }
      }
    }

    return properties;
  }

  /**
   * Action to take on each method.
   */
  public interface MethodCallback {

    /**
     * Perform an operation using the given method.
     *
     * @param method the method to operate on
     */
    void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
  }

  /**
   * Callback optionally used to filter methods to be operated on by a method callback.
   */
  public interface MethodFilter {

    /**
     * Determine whether the given method matches.
     *
     * @param method the method to check
     */
    boolean matches(Method method);
  }

  /**
   * Callback interface invoked on each field in the hierarchy.
   */
  public interface FieldCallback {

    /**
     * Perform an operation using the given field.
     *
     * @param field the field to operate on
     */
    void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
  }

  /**
   * Callback optionally used to filter fields to be operated on by a field callback.
   */
  public interface FieldFilter {

    /**
     * Determine whether the given field matches.
     *
     * @param field the field to check
     */
    boolean matches(Field field);
  }

}
