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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.iooo.boot.core.constants.Constants;
import tech.iooo.boot.core.io.UnsafeStringWriter;

/**
 * Miscellaneous {@link String} utility methods.
 *
 * <p>Mainly for internal use within the framework; consider
 * <a href="http://commons.apache.org/proper/commons-lang/">Apache's Commons Lang</a>
 * for a more comprehensive suite of {@code String} utilities.
 *
 * <p>This class delivers some simple functionality that should really be
 * provided by the core Java {@link String} and {@link StringBuilder} classes. It also provides easy-to-use methods to convert between delimited strings, such as CSV strings, and
 * collections and arrays.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Rick Evans
 * @author Arjen Poutsma
 * @author Sam Brannen
 * @author Brian Clozel
 * @since 16 April 2001
 */
public abstract class StringUtils {

  public static final String EMPTY = "";
  public static final int INDEX_NOT_FOUND = -1;
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  private static final String FOLDER_SEPARATOR = "/";
  private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
  private static final String TOP_PATH = "..";
  private static final String CURRENT_PATH = ".";
  private static final char EXTENSION_SEPARATOR = '.';
  private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
  private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)"); //key value pair pattern.
  private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
  private static final int PAD_LIMIT = 8192;
  private static final Gson GSON = new GsonBuilder().create();

  //---------------------------------------------------------------------
  // General convenience methods for working with Strings
  //---------------------------------------------------------------------

  /**
   * Check whether the given {@code String} is empty.
   * <p>This method accepts any Object as an argument, comparing it to
   * {@code null} and the empty String. As a consequence, this method will never return {@code true} for a non-null non-String object.
   * <p>The Object signature is useful for general attribute handling code
   * that commonly deals with Strings but generally has to iterate over Objects since attributes may e.g. be primitive value objects as well.
   *
   * @param str the candidate String
   * @since 3.2.1
   */
  public static boolean isEmpty(Object str) {
    return (str == null || "".equals(str));
  }

  /**
   * Check that the given {@code CharSequence} is neither {@code null} nor of length 0.
   * <p>Note: this method returns {@code true} for a {@code CharSequence}
   * that purely consists of whitespace.
   * <p><pre class="code">
   * StringUtils.hasLength(null) = false
   * StringUtils.hasLength("") = false
   * StringUtils.hasLength(" ") = true
   * StringUtils.hasLength("Hello") = true
   * </pre>
   *
   * @param str the {@code CharSequence} to check (may be {@code null})
   * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
   * @see #hasText(String)
   */
  public static boolean hasLength(CharSequence str) {
    return (str != null && str.length() > 0);
  }

  /**
   * Check that the given {@code String} is neither {@code null} nor of length 0.
   * <p>Note: this method returns {@code true} for a {@code String} that
   * purely consists of whitespace.
   *
   * @param str the {@code String} to check (may be {@code null})
   * @return {@code true} if the {@code String} is not {@code null} and has length
   * @see #hasLength(CharSequence)
   * @see #hasText(String)
   */
  public static boolean hasLength(String str) {
    return (str != null && !str.isEmpty());
  }

  /**
   * Check whether the given {@code CharSequence} contains actual <em>text</em>.
   * <p>More specifically, this method returns {@code true} if the
   * {@code CharSequence} is not {@code null}, its length is greater than 0, and it contains at least one non-whitespace character.
   * <p><pre class="code">
   * StringUtils.hasText(null) = false
   * StringUtils.hasText("") = false
   * StringUtils.hasText(" ") = false
   * StringUtils.hasText("12345") = true
   * StringUtils.hasText(" 12345 ") = true
   * </pre>
   *
   * @param str the {@code CharSequence} to check (may be {@code null})
   * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater than 0, and it does not contain whitespace only
   * @see Character#isWhitespace
   */
  public static boolean hasText(CharSequence str) {
    if (!hasLength(str)) {
      return false;
    }

    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check whether the given {@code String} contains actual <em>text</em>.
   * <p>More specifically, this method returns {@code true} if the
   * {@code String} is not {@code null}, its length is greater than 0, and it contains at least one non-whitespace character.
   *
   * @param str the {@code String} to check (may be {@code null})
   * @return {@code true} if the {@code String} is not {@code null}, its length is greater than 0, and it does not contain whitespace only
   * @see #hasText(CharSequence)
   */
  public static boolean hasText(String str) {
    return (str != null && !str.isEmpty() && hasText((CharSequence) str));
  }

  /**
   * Check whether the given {@code CharSequence} contains any whitespace characters.
   *
   * @param str the {@code CharSequence} to check (may be {@code null})
   * @return {@code true} if the {@code CharSequence} is not empty and contains at least 1 whitespace character
   * @see Character#isWhitespace
   */
  public static boolean containsWhitespace(CharSequence str) {
    if (!hasLength(str)) {
      return false;
    }

    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check whether the given {@code String} contains any whitespace characters.
   *
   * @param str the {@code String} to check (may be {@code null})
   * @return {@code true} if the {@code String} is not empty and contains at least 1 whitespace character
   * @see #containsWhitespace(CharSequence)
   */
  public static boolean containsWhitespace(String str) {
    return containsWhitespace((CharSequence) str);
  }

  /**
   * Trim leading and trailing whitespace from the given {@code String}.
   *
   * @param str the {@code String} to check
   * @return the trimmed {@code String}
   * @see Character#isWhitespace
   */
  public static String trimWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }

    StringBuilder sb = new StringBuilder(str);
    while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
      sb.deleteCharAt(0);
    }
    while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  /**
   * Trim <i>all</i> whitespace from the given {@code String}: leading, trailing, and in between characters.
   *
   * @param str the {@code String} to check
   * @return the trimmed {@code String}
   * @see Character#isWhitespace
   */
  public static String trimAllWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }

    int len = str.length();
    StringBuilder sb = new StringBuilder(str.length());
    for (int i = 0; i < len; i++) {
      char c = str.charAt(i);
      if (!Character.isWhitespace(c)) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Trim leading whitespace from the given {@code String}.
   *
   * @param str the {@code String} to check
   * @return the trimmed {@code String}
   * @see Character#isWhitespace
   */
  public static String trimLeadingWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }

    StringBuilder sb = new StringBuilder(str);
    while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
      sb.deleteCharAt(0);
    }
    return sb.toString();
  }

  /**
   * Trim trailing whitespace from the given {@code String}.
   *
   * @param str the {@code String} to check
   * @return the trimmed {@code String}
   * @see Character#isWhitespace
   */
  public static String trimTrailingWhitespace(String str) {
    if (!hasLength(str)) {
      return str;
    }

    StringBuilder sb = new StringBuilder(str);
    while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  /**
   * Trim all occurrences of the supplied leading character from the given {@code String}.
   *
   * @param str the {@code String} to check
   * @param leadingCharacter the leading character to be trimmed
   * @return the trimmed {@code String}
   */
  public static String trimLeadingCharacter(String str, char leadingCharacter) {
    if (!hasLength(str)) {
      return str;
    }

    StringBuilder sb = new StringBuilder(str);
    while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
      sb.deleteCharAt(0);
    }
    return sb.toString();
  }

  /**
   * Trim all occurrences of the supplied trailing character from the given {@code String}.
   *
   * @param str the {@code String} to check
   * @param trailingCharacter the trailing character to be trimmed
   * @return the trimmed {@code String}
   */
  public static String trimTrailingCharacter(String str, char trailingCharacter) {
    if (!hasLength(str)) {
      return str;
    }

    StringBuilder sb = new StringBuilder(str);
    while (sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }


  /**
   * Test if the given {@code String} starts with the specified prefix, ignoring upper/lower case.
   *
   * @param str the {@code String} to check
   * @param prefix the prefix to look for
   * @see String#startsWith
   */
  public static boolean startsWithIgnoreCase(String str, String prefix) {
    if (str == null || prefix == null) {
      return false;
    }
    if (str.startsWith(prefix)) {
      return true;
    }
    if (str.length() < prefix.length()) {
      return false;
    }

    String lcStr = str.substring(0, prefix.length()).toLowerCase();
    String lcPrefix = prefix.toLowerCase();
    return lcStr.equals(lcPrefix);
  }

  /**
   * Test if the given {@code String} ends with the specified suffix, ignoring upper/lower case.
   *
   * @param str the {@code String} to check
   * @param suffix the suffix to look for
   * @see String#endsWith
   */
  public static boolean endsWithIgnoreCase(String str, String suffix) {
    if (str == null || suffix == null) {
      return false;
    }
    if (str.endsWith(suffix)) {
      return true;
    }
    if (str.length() < suffix.length()) {
      return false;
    }

    String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
    String lcSuffix = suffix.toLowerCase();
    return lcStr.equals(lcSuffix);
  }

  /**
   * Test whether the given string matches the given substring at the given index.
   *
   * @param str the original string (or StringBuilder)
   * @param index the index in the original string to start matching against
   * @param substring the substring to match at the given index
   */
  public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
    for (int j = 0; j < substring.length(); j++) {
      int i = index + j;
      if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Count the occurrences of the substring {@code sub} in string {@code str}.
   *
   * @param str string to search in
   * @param sub string to search for
   */
  public static int countOccurrencesOf(String str, String sub) {
    if (!hasLength(str) || !hasLength(sub)) {
      return 0;
    }

    int count = 0;
    int pos = 0;
    int idx;
    while ((idx = str.indexOf(sub, pos)) != -1) {
      ++count;
      pos = idx + sub.length();
    }
    return count;
  }

  /**
   * Replace all occurrences of a substring within a string with another string.
   *
   * @param inString {@code String} to examine
   * @param oldPattern {@code String} to replace
   * @param newPattern {@code String} to insert
   * @return a {@code String} with the replacements
   */
  public static String replacePattern(String inString, String oldPattern, String newPattern) {
    if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
      return inString;
    }
    int index = inString.indexOf(oldPattern);
    if (index == -1) {
      // no occurrence -> can return input as-is
      return inString;
    }

    int capacity = inString.length();
    if (newPattern.length() > oldPattern.length()) {
      capacity += 16;
    }
    StringBuilder sb = new StringBuilder(capacity);

    int pos = 0;  // our position in the old string
    int patLen = oldPattern.length();
    while (index >= 0) {
      sb.append(inString.substring(pos, index));
      sb.append(newPattern);
      pos = index + patLen;
      index = inString.indexOf(oldPattern, pos);
    }

    // append any characters to the right of a match
    sb.append(inString.substring(pos));
    return sb.toString();
  }

  /**
   * Delete all occurrences of the given substring.
   *
   * @param inString the original {@code String}
   * @param pattern the pattern to delete all occurrences of
   * @return the resulting {@code String}
   */
  public static String delete(String inString, String pattern) {
    return replace(inString, pattern, "");
  }

  /**
   * Delete any character in a given {@code String}.
   *
   * @param inString the original {@code String}
   * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
   * @return the resulting {@code String}
   */
  public static String deleteAny(String inString, String charsToDelete) {
    if (!hasLength(inString) || !hasLength(charsToDelete)) {
      return inString;
    }

    StringBuilder sb = new StringBuilder(inString.length());
    for (int i = 0; i < inString.length(); i++) {
      char c = inString.charAt(i);
      if (charsToDelete.indexOf(c) == -1) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  //---------------------------------------------------------------------
  // Convenience methods for working with formatted Strings
  //---------------------------------------------------------------------

  /**
   * Quote the given {@code String} with single quotes.
   *
   * @param str the input {@code String} (e.g. "myString")
   * @return the quoted {@code String} (e.g. "'myString'"), or {@code null} if the input was {@code null}
   */
  public static String quote(String str) {
    return (str != null ? "'" + str + "'" : null);
  }

  /**
   * Turn the given Object into a {@code String} with single quotes if it is a {@code String}; keeping the Object as-is else.
   *
   * @param obj the input Object (e.g. "myString")
   * @return the quoted {@code String} (e.g. "'myString'"), or the input object as-is if not a {@code String}
   */
  public static Object quoteIfString(Object obj) {
    return (obj instanceof String ? quote((String) obj) : obj);
  }

  /**
   * Unqualify a string qualified by a '.' dot character. For example, "this.name.is.qualified", returns "qualified".
   *
   * @param qualifiedName the qualified name
   */
  public static String unqualify(String qualifiedName) {
    return unqualify(qualifiedName, '.');
  }

  /**
   * Unqualify a string qualified by a separator character. For example, "this:name:is:qualified" returns "qualified" if using a ':' separator.
   *
   * @param qualifiedName the qualified name
   * @param separator the separator
   */
  public static String unqualify(String qualifiedName, char separator) {
    return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
  }

  /**
   * Capitalize a {@code String}, changing the first letter to upper case as per {@link Character#toUpperCase(char)}. No other letters are changed.
   *
   * @param str the {@code String} to capitalize
   * @return the capitalized {@code String}
   */
  public static String capitalize(String str) {
    return changeFirstCharacterCase(str, true);
  }

  /**
   * Uncapitalize a {@code String}, changing the first letter to lower case as per {@link Character#toLowerCase(char)}. No other letters are changed.
   *
   * @param str the {@code String} to uncapitalize
   * @return the uncapitalized {@code String}
   */
  public static String uncapitalize(String str) {
    return changeFirstCharacterCase(str, false);
  }

  private static String changeFirstCharacterCase(String str, boolean capitalize) {
    if (!hasLength(str)) {
      return str;
    }

    char baseChar = str.charAt(0);
    char updatedChar;
    if (capitalize) {
      updatedChar = Character.toUpperCase(baseChar);
    } else {
      updatedChar = Character.toLowerCase(baseChar);
    }
    if (baseChar == updatedChar) {
      return str;
    }

    char[] chars = str.toCharArray();
    chars[0] = updatedChar;
    return new String(chars, 0, chars.length);
  }

  /**
   * Extract the filename from the given Java resource path, e.g. {@code "mypath/myfile.txt" -> "myfile.txt"}.
   *
   * @param path the file path (may be {@code null})
   * @return the extracted filename, or {@code null} if none
   */
  public static String getFilename(String path) {
    if (path == null) {
      return null;
    }

    int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
    return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
  }

  /**
   * Extract the filename extension from the given Java resource path, e.g. "mypath/myfile.txt" -> "txt".
   *
   * @param path the file path (may be {@code null})
   * @return the extracted filename extension, or {@code null} if none
   */
  public static String getFilenameExtension(String path) {
    if (path == null) {
      return null;
    }

    int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
    if (extIndex == -1) {
      return null;
    }

    int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
    if (folderIndex > extIndex) {
      return null;
    }

    return path.substring(extIndex + 1);
  }

  /**
   * Strip the filename extension from the given Java resource path, e.g. "mypath/myfile.txt" -> "mypath/myfile".
   *
   * @param path the file path
   * @return the path with stripped filename extension
   */
  public static String stripFilenameExtension(String path) {
    if (path == null) {
      return null;
    }

    int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
    if (extIndex == -1) {
      return path;
    }

    int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
    if (folderIndex > extIndex) {
      return path;
    }

    return path.substring(0, extIndex);
  }

  /**
   * Apply the given relative path to the given Java resource path, assuming standard Java folder separation (i.e. "/" separators).
   *
   * @param path the path to start from (usually a full file path)
   * @param relativePath the relative path to apply (relative to the full file path above)
   * @return the full file path that results from applying the relative path
   */
  public static String applyRelativePath(String path, String relativePath) {
    int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
    if (separatorIndex != -1) {
      String newPath = path.substring(0, separatorIndex);
      if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
        newPath += FOLDER_SEPARATOR;
      }
      return newPath + relativePath;
    } else {
      return relativePath;
    }
  }

  /**
   * Normalize the path by suppressing sequences like "path/.." and inner simple dots.
   * <p>The result is convenient for path comparison. For other uses,
   * notice that Windows separators ("\") are replaced by simple slashes.
   *
   * @param path the original path
   * @return the normalized path
   */
  public static String cleanPath(String path) {
    if (path == null) {
      return null;
    }
    String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

    // Strip prefix from path to analyze, to not treat it as part of the
    // first path element. This is necessary to correctly parse paths like
    // "file:core/../core/io/Resource.class", where the ".." should just
    // strip the first "core" directory while keeping the "file:" prefix.
    int prefixIndex = pathToUse.indexOf(":");
    String prefix = "";
    if (prefixIndex != -1) {
      prefix = pathToUse.substring(0, prefixIndex + 1);
      if (prefix.contains("/")) {
        prefix = "";
      } else {
        pathToUse = pathToUse.substring(prefixIndex + 1);
      }
    }
    if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
      prefix = prefix + FOLDER_SEPARATOR;
      pathToUse = pathToUse.substring(1);
    }

    String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
    List<String> pathElements = new LinkedList<String>();
    int tops = 0;

    for (int i = pathArray.length - 1; i >= 0; i--) {
      String element = pathArray[i];
      if (CURRENT_PATH.equals(element)) {
        // Points to current directory - drop it.
      } else if (TOP_PATH.equals(element)) {
        // Registering top path found.
        tops++;
      } else {
        if (tops > 0) {
          // Merging path element with element corresponding to top path.
          tops--;
        } else {
          // Normal path element found.
          pathElements.add(0, element);
        }
      }
    }

    // Remaining top paths need to be retained.
    for (int i = 0; i < tops; i++) {
      pathElements.add(0, TOP_PATH);
    }

    return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
  }

  /**
   * Compare two paths after normalization of them.
   *
   * @param path1 first path for comparison
   * @param path2 second path for comparison
   * @return whether the two paths are equivalent after normalization
   */
  public static boolean pathEquals(String path1, String path2) {
    return cleanPath(path1).equals(cleanPath(path2));
  }

  /**
   * Parse the given {@code localeString} value into a {@link Locale}.
   * <p>This is the inverse operation of {@link Locale#toString Locale's toString}.
   *
   * @param localeString the locale {@code String}, following {@code Locale's} {@code toString()} format ("en", "en_UK", etc); also accepts spaces as separators, as an alternative
   * to underscores
   * @return a corresponding {@code Locale} instance, or {@code null} if none
   * @throws IllegalArgumentException in case of an invalid locale specification
   */
  public static Locale parseLocaleString(String localeString) {
    String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
    String language = (parts.length > 0 ? parts[0] : "");
    String country = (parts.length > 1 ? parts[1] : "");

    validateLocalePart(language);
    validateLocalePart(country);

    String variant = "";
    if (parts.length > 2) {
      // There is definitely a variant, and it is everything after the country
      // code sans the separator between the country code and the variant.
      int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
      // Strip off any leading '_' and whitespace, what's left is the variant.
      variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
      if (variant.startsWith("_")) {
        variant = trimLeadingCharacter(variant, '_');
      }
    }
    return (language.length() > 0 ? new Locale(language, country, variant) : null);
  }

  private static void validateLocalePart(String localePart) {
    for (int i = 0; i < localePart.length(); i++) {
      char ch = localePart.charAt(i);
      if (ch != ' ' && ch != '_' && ch != '#' && !Character.isLetterOrDigit(ch)) {
        throw new IllegalArgumentException(
            "Locale part \"" + localePart + "\" contains invalid characters");
      }
    }
  }

  /**
   * Determine the RFC 3066 compliant language tag, as used for the HTTP "Accept-Language" header.
   *
   * @param locale the Locale to transform to a language tag
   * @return the RFC 3066 compliant language tag as {@code String}
   */
  public static String toLanguageTag(Locale locale) {
    return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
  }

  /**
   * Parse the given {@code timeZoneString} value into a {@link TimeZone}.
   *
   * @param timeZoneString the time zone {@code String}, following {@link TimeZone#getTimeZone(String)} but throwing {@link IllegalArgumentException} in case of an invalid time
   * zone specification
   * @return a corresponding {@link TimeZone} instance
   * @throws IllegalArgumentException in case of an invalid time zone specification
   */
  public static TimeZone parseTimeZoneString(String timeZoneString) {
    TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
    if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
      // We don't want that GMT fallback...
      throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
    }
    return timeZone;
  }

  //---------------------------------------------------------------------
  // Convenience methods for working with String arrays
  //---------------------------------------------------------------------

  /**
   * Append the given {@code String} to the given {@code String} array, returning a new array consisting of the input array contents plus the given {@code String}.
   *
   * @param array the array to append to (can be {@code null})
   * @param str the {@code String} to append
   * @return the new array (never {@code null})
   */
  public static String[] addStringToArray(String[] array, String str) {
    if (ObjectUtils.isEmpty(array)) {
      return new String[]{str};
    }

    String[] newArr = new String[array.length + 1];
    System.arraycopy(array, 0, newArr, 0, array.length);
    newArr[array.length] = str;
    return newArr;
  }

  /**
   * Concatenate the given {@code String} arrays into one, with overlapping array elements included twice.
   * <p>The order of elements in the original arrays is preserved.
   *
   * @param array1 the first array (can be {@code null})
   * @param array2 the second array (can be {@code null})
   * @return the new array ({@code null} if both given arrays were {@code null})
   */
  public static String[] concatenateStringArrays(String[] array1, String[] array2) {
    if (ObjectUtils.isEmpty(array1)) {
      return array2;
    }
    if (ObjectUtils.isEmpty(array2)) {
      return array1;
    }

    String[] newArr = new String[array1.length + array2.length];
    System.arraycopy(array1, 0, newArr, 0, array1.length);
    System.arraycopy(array2, 0, newArr, array1.length, array2.length);
    return newArr;
  }

  /**
   * Merge the given {@code String} arrays into one, with overlapping array elements only included once.
   * <p>The order of elements in the original arrays is preserved
   * (with the exception of overlapping elements, which are only included on their first occurrence).
   *
   * @param array1 the first array (can be {@code null})
   * @param array2 the second array (can be {@code null})
   * @return the new array ({@code null} if both given arrays were {@code null})
   */
  public static String[] mergeStringArrays(String[] array1, String[] array2) {
    if (ObjectUtils.isEmpty(array1)) {
      return array2;
    }
    if (ObjectUtils.isEmpty(array2)) {
      return array1;
    }

    List<String> result = new ArrayList<String>();
    result.addAll(Arrays.asList(array1));
    for (String str : array2) {
      if (!result.contains(str)) {
        result.add(str);
      }
    }
    return toStringArray(result);
  }

  /**
   * Turn given source {@code String} array into sorted array.
   *
   * @param array the source array
   * @return the sorted array (never {@code null})
   */
  public static String[] sortStringArray(String[] array) {
    if (ObjectUtils.isEmpty(array)) {
      return new String[0];
    }

    Arrays.sort(array);
    return array;
  }

  /**
   * Copy the given {@code Collection} into a {@code String} array.
   * <p>The {@code Collection} must contain {@code String} elements only.
   *
   * @param collection the {@code Collection} to copy
   * @return the {@code String} array
   */
  public static String[] toStringArray(Collection<String> collection) {
    if (collection == null) {
      return null;
    }

    return collection.toArray(new String[collection.size()]);
  }

  /**
   * Copy the given Enumeration into a {@code String} array. The Enumeration must contain {@code String} elements only.
   *
   * @param enumeration the Enumeration to copy
   * @return the {@code String} array
   */
  public static String[] toStringArray(Enumeration<String> enumeration) {
    if (enumeration == null) {
      return null;
    }

    List<String> list = Collections.list(enumeration);
    return list.toArray(new String[list.size()]);
  }

  /**
   * Trim the elements of the given {@code String} array, calling {@code String.trim()} on each of them.
   *
   * @param array the original {@code String} array
   * @return the resulting array (of the same size) with trimmed elements
   */
  public static String[] trimArrayElements(String[] array) {
    if (ObjectUtils.isEmpty(array)) {
      return new String[0];
    }

    String[] result = new String[array.length];
    for (int i = 0; i < array.length; i++) {
      String element = array[i];
      result[i] = (element != null ? element.trim() : null);
    }
    return result;
  }

  /**
   * Remove duplicate strings from the given array.
   * <p>As of 4.2, it preserves the original order, as it uses a {@link LinkedHashSet}.
   *
   * @param array the {@code String} array
   * @return an array without duplicates, in natural sort order
   */
  public static String[] removeDuplicateStrings(String[] array) {
    if (ObjectUtils.isEmpty(array)) {
      return array;
    }

    Set<String> set = new LinkedHashSet<String>();
    for (String element : array) {
      set.add(element);
    }
    return toStringArray(set);
  }

  /**
   * Split a {@code String} at the first occurrence of the delimiter. Does not include the delimiter in the result.
   *
   * @param toSplit the string to split
   * @param delimiter to split the string up with
   * @return a two element array with index 0 being before the delimiter, and index 1 being after the delimiter (neither element includes the delimiter); or {@code null} if the
   * delimiter wasn't found in the given input {@code String}
   */
  public static String[] split(String toSplit, String delimiter) {
    if (!hasLength(toSplit) || !hasLength(delimiter)) {
      return null;
    }
    int offset = toSplit.indexOf(delimiter);
    if (offset < 0) {
      return null;
    }

    String beforeDelimiter = toSplit.substring(0, offset);
    String afterDelimiter = toSplit.substring(offset + delimiter.length());
    return new String[]{beforeDelimiter, afterDelimiter};
  }

  /**
   * Take an array of strings and split each element based on the given delimiter. A {@code Properties} instance is then generated, with the left of the delimiter providing the
   * key, and the right of the delimiter providing the value.
   * <p>Will trim both the key and value before adding them to the
   * {@code Properties} instance.
   *
   * @param array the array to process
   * @param delimiter to split each element using (typically the equals symbol)
   * @return a {@code Properties} instance representing the array contents, or {@code null} if the array to process was {@code null} or empty
   */
  public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
    return splitArrayElementsIntoProperties(array, delimiter, null);
  }

  /**
   * Take an array of strings and split each element based on the given delimiter. A {@code Properties} instance is then generated, with the left of the delimiter providing the
   * key, and the right of the delimiter providing the value.
   * <p>Will trim both the key and value before adding them to the
   * {@code Properties} instance.
   *
   * @param array the array to process
   * @param delimiter to split each element using (typically the equals symbol)
   * @param charsToDelete one or more characters to remove from each element prior to attempting the split operation (typically the quotation mark symbol), or {@code null} if no
   * removal should occur
   * @return a {@code Properties} instance representing the array contents, or {@code null} if the array to process was {@code null} or empty
   */
  public static Properties splitArrayElementsIntoProperties(
      String[] array, String delimiter, String charsToDelete) {

    if (ObjectUtils.isEmpty(array)) {
      return null;
    }

    Properties result = new Properties();
    for (String element : array) {
      if (charsToDelete != null) {
        element = deleteAny(element, charsToDelete);
      }
      String[] splittedElement = split(element, delimiter);
      if (splittedElement == null) {
        continue;
      }
      result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
    }
    return result;
  }

  /**
   * Tokenize the given {@code String} into a {@code String} array via a {@link StringTokenizer}.
   * <p>Trims tokens and omits empty tokens.
   * <p>The given {@code delimiters} string can consist of any number of
   * delimiter characters. Each of those characters can be used to separate tokens. A delimiter is always a single character; for multi-character delimiters, consider using {@link
   * #delimitedListToStringArray}.
   *
   * @param str the {@code String} to tokenize
   * @param delimiters the delimiter characters, assembled as a {@code String} (each of the characters is individually considered as a delimiter)
   * @return an array of the tokens
   * @see StringTokenizer
   * @see String#trim()
   * @see #delimitedListToStringArray
   */
  public static String[] tokenizeToStringArray(String str, String delimiters) {
    return tokenizeToStringArray(str, delimiters, true, true);
  }

  /**
   * Tokenize the given {@code String} into a {@code String} array via a {@link StringTokenizer}.
   * <p>The given {@code delimiters} string can consist of any number of
   * delimiter characters. Each of those characters can be used to separate tokens. A delimiter is always a single character; for multi-character delimiters, consider using {@link
   * #delimitedListToStringArray}.
   *
   * @param str the {@code String} to tokenize
   * @param delimiters the delimiter characters, assembled as a {@code String} (each of the characters is individually considered as a delimiter)
   * @param trimTokens trim the tokens via {@link String#trim()}
   * @param ignoreEmptyTokens omit empty tokens from the result array (only applies to tokens that are empty after trimming; StringTokenizer will not consider subsequent delimiters
   * as token in the first place).
   * @return an array of the tokens
   * @see StringTokenizer
   * @see String#trim()
   * @see #delimitedListToStringArray
   */
  public static String[] tokenizeToStringArray(
      String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

    if (str == null) {
      return null;
    }

    StringTokenizer st = new StringTokenizer(str, delimiters);
    List<String> tokens = new ArrayList<String>();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (trimTokens) {
        token = token.trim();
      }
      if (!ignoreEmptyTokens || token.length() > 0) {
        tokens.add(token);
      }
    }
    return toStringArray(tokens);
  }

  /**
   * Take a {@code String} that is a delimited list and convert it into a {@code String} array.
   * <p>A single {@code delimiter} may consist of more than one character,
   * but it will still be considered as a single delimiter string, rather than as bunch of potential delimiter characters, in contrast to {@link #tokenizeToStringArray}.
   *
   * @param str the input {@code String}
   * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter characters)
   * @return an array of the tokens in the list
   * @see #tokenizeToStringArray
   */
  public static String[] delimitedListToStringArray(String str, String delimiter) {
    return delimitedListToStringArray(str, delimiter, null);
  }

  /**
   * Take a {@code String} that is a delimited list and convert it into a {@code String} array.
   * <p>A single {@code delimiter} may consist of more than one character,
   * but it will still be considered as a single delimiter string, rather than as bunch of potential delimiter characters, in contrast to {@link #tokenizeToStringArray}.
   *
   * @param str the input {@code String}
   * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter characters)
   * @param charsToDelete a set of characters to delete; useful for deleting unwanted line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a {@code String}
   * @return an array of the tokens in the list
   * @see #tokenizeToStringArray
   */
  public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
    if (str == null) {
      return new String[0];
    }
    if (delimiter == null) {
      return new String[]{str};
    }

    List<String> result = new ArrayList<String>();
    if ("".equals(delimiter)) {
      for (int i = 0; i < str.length(); i++) {
        result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
      }
    } else {
      int pos = 0;
      int delPos;
      while ((delPos = str.indexOf(delimiter, pos)) != -1) {
        result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
        pos = delPos + delimiter.length();
      }
      if (str.length() > 0 && pos <= str.length()) {
        // Add rest of String, but not in case of empty input.
        result.add(deleteAny(str.substring(pos), charsToDelete));
      }
    }
    return toStringArray(result);
  }

  /**
   * Convert a comma delimited list (e.g., a row from a CSV file) into an array of strings.
   *
   * @param str the input {@code String}
   * @return an array of strings, or the empty array in case of empty input
   */
  public static String[] commaDelimitedListToStringArray(String str) {
    return delimitedListToStringArray(str, ",");
  }

  /**
   * Convert a comma delimited list (e.g., a row from a CSV file) into a set.
   * <p>Note that this will suppress duplicates, and as of 4.2, the elements in
   * the returned set will preserve the original order in a {@link LinkedHashSet}.
   *
   * @param str the input {@code String}
   * @return a set of {@code String} entries in the list
   * @see #removeDuplicateStrings(String[])
   */
  public static Set<String> commaDelimitedListToSet(String str) {
    Set<String> set = new LinkedHashSet<String>();
    String[] tokens = commaDelimitedListToStringArray(str);
    for (String token : tokens) {
      set.add(token);
    }
    return set;
  }

  /**
   * Convert a {@link Collection} to a delimited {@code String} (e.g. CSV).
   * <p>Useful for {@code toString()} implementations.
   *
   * @param coll the {@code Collection} to convert
   * @param delim the delimiter to use (typically a ",")
   * @param prefix the {@code String} to start each element with
   * @param suffix the {@code String} to end each element with
   * @return the delimited {@code String}
   */
  public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
    if (CollectionUtils.isEmpty(coll)) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    Iterator<?> it = coll.iterator();
    while (it.hasNext()) {
      sb.append(prefix).append(it.next()).append(suffix);
      if (it.hasNext()) {
        sb.append(delim);
      }
    }
    return sb.toString();
  }

  /**
   * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
   * <p>Useful for {@code toString()} implementations.
   *
   * @param coll the {@code Collection} to convert
   * @param delim the delimiter to use (typically a ",")
   * @return the delimited {@code String}
   */
  public static String collectionToDelimitedString(Collection<?> coll, String delim) {
    return collectionToDelimitedString(coll, delim, "", "");
  }

  /**
   * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
   * <p>Useful for {@code toString()} implementations.
   *
   * @param coll the {@code Collection} to convert
   * @return the delimited {@code String}
   */
  public static String collectionToCommaDelimitedString(Collection<?> coll) {
    return collectionToDelimitedString(coll, ",");
  }

  /**
   * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
   * <p>Useful for {@code toString()} implementations.
   *
   * @param arr the array to display
   * @param delim the delimiter to use (typically a ",")
   * @return the delimited {@code String}
   */
  public static String arrayToDelimitedString(Object[] arr, String delim) {
    if (ObjectUtils.isEmpty(arr)) {
      return "";
    }
    if (arr.length == 1) {
      return ObjectUtils.nullSafeToString(arr[0]);
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < arr.length; i++) {
      if (i > 0) {
        sb.append(delim);
      }
      sb.append(arr[i]);
    }
    return sb.toString();
  }

  /**
   * Convert a {@code String} array into a comma delimited {@code String} (i.e., CSV).
   * <p>Useful for {@code toString()} implementations.
   *
   * @param arr the array to display
   * @return the delimited {@code String}
   */
  public static String arrayToCommaDelimitedString(Object[] arr) {
    return arrayToDelimitedString(arr, ",");
  }


  /**
   * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
   *
   * @param cs a CharSequence or {@code null}
   * @return CharSequence length or {@code 0} if the CharSequence is {@code null}.
   */
  public static int length(final CharSequence cs) {
    return cs == null ? 0 : cs.length();
  }

  /**
   * <p>Repeat a String {@code repeat} times to form a
   * new String.</p>
   *
   * <pre>
   * StringUtils.repeat(null, 2) = null
   * StringUtils.repeat("", 0)   = ""
   * StringUtils.repeat("", 2)   = ""
   * StringUtils.repeat("a", 3)  = "aaa"
   * StringUtils.repeat("ab", 2) = "abab"
   * StringUtils.repeat("a", -2) = ""
   * </pre>
   *
   * @param str the String to repeat, may be null
   * @param repeat number of times to repeat str, negative treated as zero
   * @return a new String consisting of the original String repeated, {@code null} if null String input
   */
  public static String repeat(final String str, final int repeat) {
    // Performance tuned for 2.0 (JDK1.4)

    if (str == null) {
      return null;
    }
    if (repeat <= 0) {
      return EMPTY;
    }
    final int inputLength = str.length();
    if (repeat == 1 || inputLength == 0) {
      return str;
    }
    if (inputLength == 1 && repeat <= PAD_LIMIT) {
      return repeat(str.charAt(0), repeat);
    }

    final int outputLength = inputLength * repeat;
    switch (inputLength) {
      case 1:
        return repeat(str.charAt(0), repeat);
      case 2:
        final char ch0 = str.charAt(0);
        final char ch1 = str.charAt(1);
        final char[] output2 = new char[outputLength];
        for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
          output2[i] = ch0;
          output2[i + 1] = ch1;
        }
        return new String(output2);
      default:
        final StringBuilder buf = new StringBuilder(outputLength);
        for (int i = 0; i < repeat; i++) {
          buf.append(str);
        }
        return buf.toString();
    }
  }

  /**
   * <p>Repeat a String {@code repeat} times to form a
   * new String, with a String separator injected each time. </p>
   *
   * <pre>
   * StringUtils.repeat(null, null, 2) = null
   * StringUtils.repeat(null, "x", 2)  = null
   * StringUtils.repeat("", null, 0)   = ""
   * StringUtils.repeat("", "", 2)     = ""
   * StringUtils.repeat("", "x", 3)    = "xxx"
   * StringUtils.repeat("?", ", ", 3)  = "?, ?, ?"
   * </pre>
   *
   * @param str the String to repeat, may be null
   * @param separator the String to inject, may be null
   * @param repeat number of times to repeat str, negative treated as zero
   * @return a new String consisting of the original String repeated, {@code null} if null String input
   * @since 2.5
   */
  public static String repeat(final String str, final String separator, final int repeat) {
    if (str == null || separator == null) {
      return repeat(str, repeat);
    }
    // given that repeat(String, int) is quite optimized, better to rely on it than try and splice this into it
    final String result = repeat(str + separator, repeat);
    return removeEnd(result, separator);
  }

  /**
   * <p>Removes a substring only if it is at the end of a source string,
   * otherwise returns the source string.</p>
   *
   * <p>A {@code null} source string will return {@code null}.
   * An empty ("") source string will return the empty string. A {@code null} search string will return the source string.</p>
   *
   * <pre>
   * StringUtils.removeEnd(null, *)      = null
   * StringUtils.removeEnd("", *)        = ""
   * StringUtils.removeEnd(*, null)      = *
   * StringUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
   * StringUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
   * StringUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
   * StringUtils.removeEnd("abc", "")    = "abc"
   * </pre>
   *
   * @param str the source String to search, may be null
   * @param remove the String to search for and remove, may be null
   * @return the substring with the string removed if found, {@code null} if null String input
   */
  public static String removeEnd(final String str, final String remove) {
    if (isEmpty(str) || isEmpty(remove)) {
      return str;
    }
    if (str.endsWith(remove)) {
      return str.substring(0, str.length() - remove.length());
    }
    return str;
  }

  /**
   * <p>Returns padding using the specified delimiter repeated
   * to a given length.</p>
   *
   * <pre>
   * StringUtils.repeat('e', 0)  = ""
   * StringUtils.repeat('e', 3)  = "eee"
   * StringUtils.repeat('e', -2) = ""
   * </pre>
   *
   * <p>Note: this method doesn't not support padding with
   * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
   * as they require a pair of {@code char}s to be represented. If you are needing to support full I18N of your applications consider using {@link #repeat(String, int)} instead.
   * </p>
   *
   * @param ch character to repeat
   * @param repeat number of times to repeat char, negative treated as zero
   * @return String with repeated character
   * @see #repeat(String, int)
   */
  public static String repeat(final char ch, final int repeat) {
    final char[] buf = new char[repeat];
    for (int i = repeat - 1; i >= 0; i--) {
      buf[i] = ch;
    }
    return new String(buf);
  }

  /**
   * <p>Strips any of a set of characters from the end of a String.</p>
   *
   * <p>A {@code null} input String returns {@code null}.
   * An empty string ("") input returns the empty string.</p>
   *
   * <p>If the stripChars String is {@code null}, whitespace is
   * stripped as defined by {@link Character#isWhitespace(char)}.</p>
   *
   * <pre>
   * StringUtils.stripEnd(null, *)          = null
   * StringUtils.stripEnd("", *)            = ""
   * StringUtils.stripEnd("abc", "")        = "abc"
   * StringUtils.stripEnd("abc", null)      = "abc"
   * StringUtils.stripEnd("  abc", null)    = "  abc"
   * StringUtils.stripEnd("abc  ", null)    = "abc"
   * StringUtils.stripEnd(" abc ", null)    = " abc"
   * StringUtils.stripEnd("  abcyx", "xyz") = "  abc"
   * StringUtils.stripEnd("120.00", ".0")   = "12"
   * </pre>
   *
   * @param str the String to remove characters from, may be null
   * @param stripChars the set of characters to remove, null treated as whitespace
   * @return the stripped String, {@code null} if null String input
   */
  public static String stripEnd(final String str, final String stripChars) {
    int end;
    if (str == null || (end = str.length()) == 0) {
      return str;
    }

    if (stripChars == null) {
      while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
        end--;
      }
    } else if (stripChars.isEmpty()) {
      return str;
    } else {
      while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != INDEX_NOT_FOUND) {
        end--;
      }
    }
    return str.substring(0, end);
  }

  /**
   * <p>Replaces all occurrences of a String within another String.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replace(null, *, *)        = null
   * StringUtils.replace("", *, *)          = ""
   * StringUtils.replace("any", null, *)    = "any"
   * StringUtils.replace("any", *, null)    = "any"
   * StringUtils.replace("any", "", *)      = "any"
   * StringUtils.replace("aba", "a", null)  = "aba"
   * StringUtils.replace("aba", "a", "")    = "b"
   * StringUtils.replace("aba", "a", "z")   = "zbz"
   * </pre>
   *
   * @param text text to search and replace in, may be null
   * @param searchString the String to search for, may be null
   * @param replacement the String to replace it with, may be null
   * @return the text with any replacements processed, {@code null} if null String input
   * @see #replace(String text, String searchString, String replacement, int max)
   */
  public static String replace(final String text, final String searchString, final String replacement) {
    return replace(text, searchString, replacement, -1);
  }

  /**
   * <p>Replaces a String with another String inside a larger String,
   * for the first {@code max} values of the search String.</p>
   *
   * <p>A {@code null} reference passed to this method is a no-op.</p>
   *
   * <pre>
   * StringUtils.replace(null, *, *, *)         = null
   * StringUtils.replace("", *, *, *)           = ""
   * StringUtils.replace("any", null, *, *)     = "any"
   * StringUtils.replace("any", *, null, *)     = "any"
   * StringUtils.replace("any", "", *, *)       = "any"
   * StringUtils.replace("any", *, *, 0)        = "any"
   * StringUtils.replace("abaa", "a", null, -1) = "abaa"
   * StringUtils.replace("abaa", "a", "", -1)   = "b"
   * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
   * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
   * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
   * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
   * </pre>
   *
   * @param text text to search and replace in, may be null
   * @param searchString the String to search for, may be null
   * @param replacement the String to replace it with, may be null
   * @param max maximum number of values to replace, or {@code -1} if no maximum
   * @return the text with any replacements processed, {@code null} if null String input
   */
  public static String replace(final String text, final String searchString, final String replacement, int max) {
    if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
      return text;
    }
    int start = 0;
    int end = text.indexOf(searchString, start);
    if (end == INDEX_NOT_FOUND) {
      return text;
    }
    final int replLength = searchString.length();
    int increase = replacement.length() - replLength;
    increase = increase < 0 ? 0 : increase;
    increase *= max < 0 ? 16 : max > 64 ? 64 : max;
    final StringBuilder buf = new StringBuilder(text.length() + increase);
    while (end != INDEX_NOT_FOUND) {
      buf.append(text.substring(start, end)).append(replacement);
      start = end + replLength;
      if (--max == 0) {
        break;
      }
      end = text.indexOf(searchString, start);
    }
    buf.append(text.substring(start));
    return buf.toString();
  }

  public static boolean isBlank(String str) {
    if (str == null || str.length() == 0) {
      return true;
    }
    return false;
  }

  /**
   * is empty string.
   *
   * @param str source string.
   * @return is empty.
   */
  public static boolean isEmpty(String str) {
    if (str == null || str.length() == 0) {
      return true;
    }
    return false;
  }

  /**
   * is not empty string.
   *
   * @param str source string.
   * @return is not empty.
   */
  public static boolean isNotEmpty(String str) {
    return str != null && str.length() > 0;
  }

  /**
   * @return equals
   */
  public static boolean isEquals(String s1, String s2) {
    if (s1 == null && s2 == null) {
      return true;
    }
    if (s1 == null || s2 == null) {
      return false;
    }
    return s1.equals(s2);
  }

  /**
   * is integer string.
   *
   * @return is integer
   */
  public static boolean isInteger(String str) {
    if (str == null || str.length() == 0) {
      return false;
    }
    return INT_PATTERN.matcher(str).matches();
  }

  public static int parseInteger(String str) {
    if (!isInteger(str)) {
      return 0;
    }
    return Integer.parseInt(str);
  }

  /**
   * Returns true if s is a legal Java identifier.<p>
   * <a href="http://www.exampledepot.com/egs/java.lang/IsJavaId.html">more info.</a>
   */
  public static boolean isJavaIdentifier(String s) {
    if (s.length() == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
      return false;
    }
    for (int i = 1; i < s.length(); i++) {
      if (!Character.isJavaIdentifierPart(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isContains(String values, String value) {
    if (values == null || values.length() == 0) {
      return false;
    }
    return isContains(Constants.COMMA_SPLIT_PATTERN.split(values), value);
  }

  /**
   * @return contains
   */
  public static boolean isContains(String[] values, String value) {
    if (value != null && value.length() > 0 && values != null && values.length > 0) {
      for (String v : values) {
        if (value.equals(v)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isNumeric(String str) {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (Character.isDigit(str.charAt(i)) == false) {
        return false;
      }
    }
    return true;
  }

  /**
   * @return string
   */
  public static String toString(Throwable e) {
    UnsafeStringWriter w = new UnsafeStringWriter();
    PrintWriter p = new PrintWriter(w);
    p.print(e.getClass().getName());
    if (e.getMessage() != null) {
      p.print(": " + e.getMessage());
    }
    p.println();
    try {
      e.printStackTrace(p);
      return w.toString();
    } finally {
      p.close();
    }
  }

  /**
   * @return string
   */
  public static String toString(String msg, Throwable e) {
    UnsafeStringWriter w = new UnsafeStringWriter();
    w.write(msg + "\n");
    PrintWriter p = new PrintWriter(w);
    try {
      e.printStackTrace(p);
      return w.toString();
    } finally {
      p.close();
    }
  }

  /**
   * translat.
   *
   * @param src source string.
   * @param from src char table.
   * @param to target char table.
   * @return String.
   */
  public static String translat(String src, String from, String to) {
    if (isEmpty(src)) {
      return src;
    }
    StringBuilder sb = null;
    int ix;
    char c;
    for (int i = 0, len = src.length(); i < len; i++) {
      c = src.charAt(i);
      ix = from.indexOf(c);
      if (ix == -1) {
        if (sb != null) {
          sb.append(c);
        }
      } else {
        if (sb == null) {
          sb = new StringBuilder(len);
          sb.append(src, 0, i);
        }
        if (ix < to.length()) {
          sb.append(to.charAt(ix));
        }
      }
    }
    return sb == null ? src : sb.toString();
  }

  /**
   * split.
   *
   * @param ch char.
   * @return string array.
   */
  public static String[] split(String str, char ch) {
    List<String> list = null;
    char c;
    int ix = 0, len = str.length();
    for (int i = 0; i < len; i++) {
      c = str.charAt(i);
      if (c == ch) {
        if (list == null) {
          list = new ArrayList<String>();
        }
        list.add(str.substring(ix, i));
        ix = i + 1;
      }
    }
    if (ix > 0) {
      list.add(str.substring(ix));
    }
    return list == null ? EMPTY_STRING_ARRAY : (String[]) list.toArray(EMPTY_STRING_ARRAY);
  }

  /**
   * join string.
   *
   * @param array String array.
   * @return String.
   */
  public static String join(String[] array) {
    if (array.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (String s : array) {
      sb.append(s);
    }
    return sb.toString();
  }

  /**
   * join string like javascript.
   *
   * @param array String array.
   * @param split split
   * @return String.
   */
  public static String join(String[] array, char split) {
    if (array.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(split);
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  /**
   * join string like javascript.
   *
   * @param array String array.
   * @param split split
   * @return String.
   */
  public static String join(String[] array, String split) {
    if (array.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(split);
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String join(Collection<String> coll, String split) {
    if (coll.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    for (String s : coll) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(split);
      }
      sb.append(s);
    }
    return sb.toString();
  }

  /**
   * parse key-value pair.
   *
   * @param str string.
   * @param itemSeparator item separator.
   * @return key-value map;
   */
  private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
    String[] tmp = str.split(itemSeparator);
    Map<String, String> map = new HashMap<String, String>(tmp.length);
    for (int i = 0; i < tmp.length; i++) {
      Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
      if (matcher.matches() == false) {
        continue;
      }
      map.put(matcher.group(1), matcher.group(2));
    }
    return map;
  }

  public static String getQueryStringValue(String qs, String key) {
    Map<String, String> map = StringUtils.parseQueryString(qs);
    return map.get(key);
  }

  /**
   * parse query string to Parameters.
   *
   * @param qs query string.
   * @return Parameters instance.
   */
  public static Map<String, String> parseQueryString(String qs) {
    if (qs == null || qs.length() == 0) {
      return new HashMap<String, String>();
    }
    return parseKeyValuePair(qs, "\\&");
  }

  public static String getServiceKey(Map<String, String> ps) {
    StringBuilder buf = new StringBuilder();
    String group = ps.get(Constants.GROUP_KEY);
    if (group != null && group.length() > 0) {
      buf.append(group).append("/");
    }
    buf.append(ps.get(Constants.INTERFACE_KEY));
    String version = ps.get(Constants.VERSION_KEY);
    if (version != null && version.length() > 0) {
      buf.append(":").append(version);
    }
    return buf.toString();
  }

  public static String toQueryString(Map<String, String> ps) {
    StringBuilder buf = new StringBuilder();
    if (ps != null && ps.size() > 0) {
      for (Map.Entry<String, String> entry : new TreeMap<String, String>(ps).entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        if (key != null && key.length() > 0
            && value != null && value.length() > 0) {
          if (buf.length() > 0) {
            buf.append("&");
          }
          buf.append(key);
          buf.append("=");
          buf.append(value);
        }
      }
    }
    return buf.toString();
  }

  public static String camelToSplitName(String camelName, String split) {
    if (camelName == null || camelName.length() == 0) {
      return camelName;
    }
    StringBuilder buf = null;
    for (int i = 0; i < camelName.length(); i++) {
      char ch = camelName.charAt(i);
      if (ch >= 'A' && ch <= 'Z') {
        if (buf == null) {
          buf = new StringBuilder();
          if (i > 0) {
            buf.append(camelName.substring(0, i));
          }
        }
        if (i > 0) {
          buf.append(split);
        }
        buf.append(Character.toLowerCase(ch));
      } else if (buf != null) {
        buf.append(ch);
      }
    }
    return buf == null ? camelName : buf.toString();
  }

  public static String toArgumentString(Object[] args) {
    StringBuilder buf = new StringBuilder();
    for (Object arg : args) {
      if (buf.length() > 0) {
        buf.append(Constants.COMMA_SEPARATOR);
      }
      if (arg == null || ReflectionUtils.isPrimitives(arg.getClass())) {
        buf.append(arg);
      } else {
        try {
          buf.append(GSON.toJson(arg));
        } catch (Exception e) {
          logger.warn(e.getMessage(), e);
          buf.append(arg);
        }
      }
    }
    return buf.toString();
  }
}
