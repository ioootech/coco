/*
 * Copyright 2002-2016 the original author or authors.
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Miscellaneous collection utility methods. Mainly for internal use within the framework.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Arjen Poutsma
 * @since 1.1.3
 */
public abstract class CollectionUtils {

  private static final Comparator<String> SIMPLE_NAME_COMPARATOR = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
      if (s1 == null && s2 == null) {
        return 0;
      }
      if (s1 == null) {
        return -1;
      }
      if (s2 == null) {
        return 1;
      }
      int i1 = s1.lastIndexOf('.');
      if (i1 >= 0) {
        s1 = s1.substring(i1 + 1);
      }
      int i2 = s2.lastIndexOf('.');
      if (i2 >= 0) {
        s2 = s2.substring(i2 + 1);
      }
      return s1.compareToIgnoreCase(s2);
    }
  };

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> List<T> sort(List<T> list) {
    if (list != null && !list.isEmpty()) {
      Collections.sort((List) list);
    }
    return list;
  }

  public static List<String> sortSimpleName(List<String> list) {
    if (list != null && list.size() > 0) {
      list.sort(SIMPLE_NAME_COMPARATOR);
    }
    return list;
  }

  public static Map<String, Map<String, String>> splitAll(Map<String, List<String>> list, String separator) {
    if (list == null) {
      return null;
    }
    Map<String, Map<String, String>> result = Maps.newHashMap();
    for (Map.Entry<String, List<String>> entry : list.entrySet()) {
      result.put(entry.getKey(), split(entry.getValue(), separator));
    }
    return result;
  }

  public static Map<String, List<String>> joinAll(Map<String, Map<String, String>> map, String separator) {
    if (map == null) {
      return null;
    }
    Map<String, List<String>> result = Maps.newHashMap();
    for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
      result.put(entry.getKey(), join(entry.getValue(), separator));
    }
    return result;
  }

  public static Map<String, String> split(List<String> list, String separator) {
    if (list == null) {
      return null;
    }
    Map<String, String> map = Maps.newHashMap();
    if (list.isEmpty()) {
      return map;
    }
    for (String item : list) {
      int index = item.indexOf(separator);
      if (index == -1) {
        map.put(item, "");
      } else {
        map.put(item.substring(0, index), item.substring(index + 1));
      }
    }
    return map;
  }

  public static List<String> join(Map<String, String> map, String separator) {
    if (map == null) {
      return null;
    }
    List<String> list = Lists.newArrayList();
    if (map.size() == 0) {
      return list;
    }
    for (Map.Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (value == null || value.length() == 0) {
        list.add(key);
      } else {
        list.add(key + separator + value);
      }
    }
    return list;
  }

  public static String join(List<String> list, String separator) {
    StringBuilder sb = new StringBuilder();
    for (String ele : list) {
      if (sb.length() > 0) {
        sb.append(separator);
      }
      sb.append(ele);
    }
    return sb.toString();
  }

  public static boolean mapEquals(Map<?, ?> map1, Map<?, ?> map2) {
    if (map1 == null && map2 == null) {
      return true;
    }
    if (map1 == null || map2 == null) {
      return false;
    }
    if (map1.size() != map2.size()) {
      return false;
    }
    for (Map.Entry<?, ?> entry : map1.entrySet()) {
      Object key = entry.getKey();
      Object value1 = entry.getValue();
      Object value2 = map2.get(key);
      if (!objectEquals(value1, value2)) {
        return false;
      }
    }
    return true;
  }

  private static boolean objectEquals(Object obj1, Object obj2) {
    if (obj1 == null && obj2 == null) {
      return true;
    }
    if (obj1 == null || obj2 == null) {
      return false;
    }
    return obj1.equals(obj2);
  }

  public static Map<String, String> toStringMap(String... pairs) {
    Map<String, String> parameters = Maps.newHashMap();
    if (pairs.length > 0) {
      if (pairs.length % 2 != 0) {
        throw new IllegalArgumentException("pairs must be even.");
      }
      for (int i = 0; i < pairs.length; i = i + 2) {
        parameters.put(pairs[i], pairs[i + 1]);
      }
    }
    return parameters;
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> toMap(Object... pairs) {
    Map<K, V> ret = Maps.newHashMap();
    if (pairs == null || pairs.length == 0) {
      return ret;
    }

    if (pairs.length % 2 != 0) {
      throw new IllegalArgumentException("Map pairs can not be odd number.");
    }
    int len = pairs.length / 2;
    for (int i = 0; i < len; i++) {
      ret.put((K) pairs[2 * i], (V) pairs[2 * i + 1]);
    }
    return ret;
  }

  public static boolean isNotEmpty(Collection<?> collection) {
    return collection != null && !collection.isEmpty();
  }


  /**
   * Return {@code true} if the supplied Collection is {@code null} or empty. Otherwise, return {@code false}.
   *
   * @param collection the Collection to check
   * @return whether the given Collection is empty
   */
  public static boolean isEmpty(Collection<?> collection) {
    return (collection == null || collection.isEmpty());
  }

  /**
   * Return {@code true} if the supplied Map is {@code null} or empty. Otherwise, return {@code false}.
   *
   * @param map the Map to check
   * @return whether the given Map is empty
   */
  public static boolean isEmpty(Map<?, ?> map) {
    return (map == null || map.isEmpty());
  }

  /**
   * Convert the supplied array into a List. A primitive array gets converted into a List of the appropriate wrapper type.
   * <p><b>NOTE:</b> Generally prefer the standard {@link Arrays#asList} method.
   * This {@code arrayToList} method is just meant to deal with an incoming Object value that might be an {@code Object[]} or a primitive array at runtime.
   * <p>A {@code null} source value will be converted to an empty List.
   *
   * @param source the (potentially primitive) array
   * @return the converted List result
   * @see ObjectUtils#toObjectArray(Object)
   * @see Arrays#asList(Object[])
   */
  @SuppressWarnings("rawtypes")
  public static List arrayToList(Object source) {
    return Arrays.asList(ObjectUtils.toObjectArray(source));
  }

  /**
   * Merge the given array into the given Collection.
   *
   * @param array the array to merge (may be {@code null})
   * @param collection the target Collection to merge the array into
   */
  @SuppressWarnings("unchecked")
  public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
    if (collection == null) {
      throw new IllegalArgumentException("Collection must not be null");
    }
    Object[] arr = ObjectUtils.toObjectArray(array);
    for (Object elem : arr) {
      collection.add((E) elem);
    }
  }

  /**
   * Merge the given Properties instance into the given Map, copying all properties (key-value pairs) over.
   * <p>Uses {@code Properties.propertyNames()} to even catch
   * default properties linked into the original Properties instance.
   *
   * @param props the Properties instance to merge (may be {@code null})
   * @param map the target Map to merge the properties into
   */
  @SuppressWarnings("unchecked")
  public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
    if (map == null) {
      throw new IllegalArgumentException("Map must not be null");
    }
    if (props != null) {
      for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
        String key = (String) en.nextElement();
        Object value = props.get(key);
        if (value == null) {
          // Allow for defaults fallback or potentially overridden accessor...
          value = props.getProperty(key);
        }
        map.put((K) key, (V) value);
      }
    }
  }


  /**
   * Check whether the given Iterator contains the given element.
   *
   * @param iterator the Iterator to check
   * @param element the element to look for
   * @return {@code true} if found, {@code false} else
   */
  public static boolean contains(Iterator<?> iterator, Object element) {
    if (iterator != null) {
      while (iterator.hasNext()) {
        Object candidate = iterator.next();
        if (ObjectUtils.nullSafeEquals(candidate, element)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Check whether the given Enumeration contains the given element.
   *
   * @param enumeration the Enumeration to check
   * @param element the element to look for
   * @return {@code true} if found, {@code false} else
   */
  public static boolean contains(Enumeration<?> enumeration, Object element) {
    if (enumeration != null) {
      while (enumeration.hasMoreElements()) {
        Object candidate = enumeration.nextElement();
        if (ObjectUtils.nullSafeEquals(candidate, element)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Check whether the given Collection contains the given element instance.
   * <p>Enforces the given instance to be present, rather than returning
   * {@code true} for an equal element as well.
   *
   * @param collection the Collection to check
   * @param element the element to look for
   * @return {@code true} if found, {@code false} else
   */
  public static boolean containsInstance(Collection<?> collection, Object element) {
    if (collection != null) {
      for (Object candidate : collection) {
        if (candidate == element) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Return {@code true} if any element in '{@code candidates}' is contained in '{@code source}'; otherwise returns {@code false}.
   *
   * @param source the source Collection
   * @param candidates the candidates to search for
   * @return whether any of the candidates has been found
   */
  public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
    if (isEmpty(source) || isEmpty(candidates)) {
      return false;
    }
    for (Object candidate : candidates) {
      if (source.contains(candidate)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return the first element in '{@code candidates}' that is contained in '{@code source}'. If no element in '{@code candidates}' is present in '{@code source}' returns {@code
   * null}. Iteration order is {@link Collection} implementation specific.
   *
   * @param source the source Collection
   * @param candidates the candidates to search for
   * @return the first present object, or {@code null} if not found
   */
  @SuppressWarnings("unchecked")
  public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
    if (isEmpty(source) || isEmpty(candidates)) {
      return null;
    }
    for (Object candidate : candidates) {
      if (source.contains(candidate)) {
        return (E) candidate;
      }
    }
    return null;
  }

  /**
   * Find a single value of the given type in the given Collection.
   *
   * @param collection the Collection to search
   * @param type the type to look for
   * @return a value of the given type found if there is a clear match, or {@code null} if none or more than one such value found
   */
  @SuppressWarnings("unchecked")
  public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
    if (isEmpty(collection)) {
      return null;
    }
    T value = null;
    for (Object element : collection) {
      if (type == null || type.isInstance(element)) {
        if (value != null) {
          // More than one value found... no clear single value.
          return null;
        }
        value = (T) element;
      }
    }
    return value;
  }

  /**
   * Find a single value of one of the given types in the given Collection: searching the Collection for a value of the first type, then searching for a value of the second type,
   * etc.
   *
   * @param collection the collection to search
   * @param types the types to look for, in prioritized order
   * @return a value of one of the given types found if there is a clear match, or {@code null} if none or more than one such value found
   */
  public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
    if (isEmpty(collection) || ObjectUtils.isEmpty(types)) {
      return null;
    }
    for (Class<?> type : types) {
      Object value = findValueOfType(collection, type);
      if (value != null) {
        return value;
      }
    }
    return null;
  }

  /**
   * Determine whether the given Collection only contains a single unique object.
   *
   * @param collection the Collection to check
   * @return {@code true} if the collection contains a single reference or multiple references to the same instance, {@code false} else
   */
  public static boolean hasUniqueObject(Collection<?> collection) {
    if (isEmpty(collection)) {
      return false;
    }
    boolean hasCandidate = false;
    Object candidate = null;
    for (Object elem : collection) {
      if (!hasCandidate) {
        hasCandidate = true;
        candidate = elem;
      } else if (candidate != elem) {
        return false;
      }
    }
    return true;
  }

  /**
   * Find the common element type of the given Collection, if any.
   *
   * @param collection the Collection to check
   * @return the common element type, or {@code null} if no clear common type has been found (or the collection was empty)
   */
  public static Class<?> findCommonElementType(Collection<?> collection) {
    if (isEmpty(collection)) {
      return null;
    }
    Class<?> candidate = null;
    for (Object val : collection) {
      if (val != null) {
        if (candidate == null) {
          candidate = val.getClass();
        } else if (candidate != val.getClass()) {
          return null;
        }
      }
    }
    return candidate;
  }

  /**
   * Marshal the elements from the given enumeration into an array of the given type. Enumeration elements must be assignable to the type of the given array. The array returned
   * will be a different instance than the array given.
   */
  public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
    ArrayList<A> elements = new ArrayList<A>();
    while (enumeration.hasMoreElements()) {
      elements.add(enumeration.nextElement());
    }
    return elements.toArray(array);
  }

  /**
   * Adapt an enumeration to an iterator.
   *
   * @param enumeration the enumeration
   * @return the iterator
   */
  public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
    return new EnumerationIterator<E>(enumeration);
  }

  /**
   * Adapt a {@code Map<K, List<V>>} to an {@code MultiValueMap<K, V>}.
   *
   * @param map the original map
   * @return the multi-value map
   * @since 3.1
   */
  public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> map) {
    return new MultiValueMapAdapter<K, V>(map);
  }

  /**
   * Return an unmodifiable view of the specified multi-value map.
   *
   * @param map the map for which an unmodifiable view is to be returned.
   * @return an unmodifiable view of the specified multi-value map.
   * @since 3.1
   */
  @SuppressWarnings("unchecked")
  public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> map) {
    Assert.notNull(map, "'map' must not be null");
    Map<K, List<V>> result = new LinkedHashMap<K, List<V>>(map.size());
    for (Map.Entry<? extends K, ? extends List<? extends V>> entry : map.entrySet()) {
      List<? extends V> values = Collections.unmodifiableList(entry.getValue());
      result.put(entry.getKey(), (List<V>) values);
    }
    Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
    return toMultiValueMap(unmodifiableMap);
  }


  /**
   * Iterator wrapping an Enumeration.
   */
  private static class EnumerationIterator<E> implements Iterator<E> {

    private final Enumeration<E> enumeration;

    public EnumerationIterator(Enumeration<E> enumeration) {
      this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
      return this.enumeration.hasMoreElements();
    }

    @Override
    public E next() {
      return this.enumeration.nextElement();
    }

    @Override
    public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException("Not supported");
    }
  }


  /**
   * Adapts a Map to the MultiValueMap contract.
   */
  @SuppressWarnings("serial")
  private static class MultiValueMapAdapter<K, V> implements MultiValueMap<K, V>, Serializable {

    private final Map<K, List<V>> map;

    public MultiValueMapAdapter(Map<K, List<V>> map) {
      Assert.notNull(map, "'map' must not be null");
      this.map = map;
    }

    @Override
    public void add(K key, V value) {
      List<V> values = this.map.computeIfAbsent(key, k -> new LinkedList<V>());
      values.add(value);
    }

    @Override
    public V getFirst(K key) {
      List<V> values = this.map.get(key);
      return (values != null ? values.get(0) : null);
    }

    @Override
    public void set(K key, V value) {
      List<V> values = new LinkedList<V>();
      values.add(value);
      this.map.put(key, values);
    }

    @Override
    public void setAll(Map<K, V> values) {
      for (Entry<K, V> entry : values.entrySet()) {
        set(entry.getKey(), entry.getValue());
      }
    }

    @Override
    public Map<K, V> toSingleValueMap() {
      LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K, V>(this.map.size());
      for (Entry<K, List<V>> entry : map.entrySet()) {
        singleValueMap.put(entry.getKey(), entry.getValue().get(0));
      }
      return singleValueMap;
    }

    @Override
    public int size() {
      return this.map.size();
    }

    @Override
    public boolean isEmpty() {
      return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
      return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
      return this.map.containsValue(value);
    }

    @Override
    public List<V> get(Object key) {
      return this.map.get(key);
    }

    @Override
    public List<V> put(K key, List<V> value) {
      return this.map.put(key, value);
    }

    @Override
    public List<V> remove(Object key) {
      return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> map) {
      this.map.putAll(map);
    }

    @Override
    public void clear() {
      this.map.clear();
    }

    @Override
    public Set<K> keySet() {
      return this.map.keySet();
    }

    @Override
    public Collection<List<V>> values() {
      return this.map.values();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
      return this.map.entrySet();
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      return map.equals(other);
    }

    @Override
    public int hashCode() {
      return this.map.hashCode();
    }

    @Override
    public String toString() {
      return this.map.toString();
    }
  }

}
