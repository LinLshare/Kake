package com.home77.common.base.util;

import com.home77.common.base.debug.Check;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

public final class TextHelper {
  public static final String EMPTY = "";
  public static final String EMPTY_STRING = "N/A";
  public static final String UNKNOWN = "unknown";
  /**
   * A cheap and type-safe constant for the UTF-8 Charset.
   */
  public static final Charset UTF_8 = Charset.forName("UTF-8");

  // Common

  /**
   * Check if a string is empty or pure spaces only
   */
  public static boolean isEmptyOrSpaces(String value) {
    if (value != null) {
      for (int i = value.length() - 1; i >= 0; --i) {
        if (value.charAt(i) != ' ') {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Ensure null string will be converted as ""
   */
  public static String ensureNotNull(String s) {
    return s == null ? "" : s;
  }

  public static boolean equalsIgnoreCase(String source, String target) {
    Check.d(!isEmptyOrSpaces(target));
    return !isEmptyOrSpaces(source) && source.equalsIgnoreCase(target);
  }

  // Multiline

  public static final String LINE_BREAK = System.getProperty("line.separator");

  public static String lines(String... lines) {
    StringBuilder sb = new StringBuilder();
    int end = lines.length - 1;
    for (int i = 0; i < end; ++i) {
      sb.append(lines[i]).append(LINE_BREAK);
    }
    sb.append(lines[end]);
    return sb.toString();
  }

  // Concat

  public static String concat(String... strings) {
    Check.d(strings.length > 1 && strings[0] != null);
    StringBuilder sb = new StringBuilder();
    return concat(sb, strings);
  }

  public static String concat(StringBuilder sb, String... strings) {
    if (sb == null) {
      return null;
    }
    for (String s : strings) {
      if (s != null) {
        sb.append(s);
      }
    }
    return sb.toString();
  }

  // Split

  public static String[] splitBySpace(String line) {
    return line.trim().split("\\s+");
  }

  private static final String[] EMPTY_STRING_ARRAY = new String[0];

  public static String[] split(String src, String splitter) {
    return split(src, splitter, true, false);
  }

  /**
   * Split string and make sure the result is predictable.
   * <p/>
   * <p>
   * For example, String.split() will return ["","","a"] for "``a", but ["a"]
   * for "a``" which is unpredictable.
   * </p>
   *
   * @param splitter
   *     currently doesn't support regular expression
   * @param keepEmpty
   *     keep empty
   * @param purgeSpaceIsEmpty
   *     purge space
   *
   * @return string array
   */
  public static String[] split(String src,
                               String splitter,
                               boolean keepEmpty,
                               boolean purgeSpaceIsEmpty) {
    if (isEmptyOrSpaces(src)) {
      return EMPTY_STRING_ARRAY;
    }
    final int srcLen = src.length();

    int index;
    int lastIndex = 0;
    String subStr;
    ArrayList<String> tmpResult = new ArrayList<>();

    while (lastIndex <= srcLen) {
      // when lastIndex == srcLen, no exception, index = -1
      index = src.indexOf(splitter, lastIndex);
      if (index < 0) {
        index = srcLen;
      }
      subStr = src.substring(lastIndex, index);
      lastIndex = index + 1;
      if (purgeSpaceIsEmpty && isEmptyOrSpaces(subStr)) {
        subStr = "";
      }
      if (keepEmpty || subStr.length() > 0) {
        tmpResult.add(subStr);
      }
    }

    String[] result = new String[tmpResult.size()];
    tmpResult.toArray(result);
    return result;
  }

  public static long generateHashLong(String str) {
    long h = 0L;
    int len = str.length();
    if (len == 0) {
      return h;
    }

    for (int i = 0; i < len; i++) {
      h = 31 * h + str.charAt(i);
    }
    return h;
  }

  /**
   * Strip any white space in |input| string.
   *
   * @param input
   *     input string
   * @param replace
   *     replace string
   *
   * @return sanitized string
   */
  public static String sanitizeString(String input, String replace) {
    String result = input;
    if (isEmptyOrSpaces(result)) {
      result = replace;
    }

    result = result.replaceAll("\\s", "");
    if (isEmptyOrSpaces(result)) {
      result = replace;
    }

    return result;
  }

  public static String byteToHexString(byte[] input) {
    if (input == null) {
      return "";
    }
    String output = "";
    String tmp;
    for (byte b : input) {
      tmp = Integer.toHexString(b & 0xFF);
      if (tmp.length() == 1) {
        output = output + "0" + tmp;
      } else {
        output = output + tmp;
      }
    }
    return output.toLowerCase(Locale.ENGLISH);
  }
}
