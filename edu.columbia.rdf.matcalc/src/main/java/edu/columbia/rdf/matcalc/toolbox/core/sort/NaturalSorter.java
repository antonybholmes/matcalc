/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.matcalc.toolbox.core.sort;

import java.util.Comparator;

/**
 * The Class NaturalSorter implements natural sorting for data so that chr2
 * comes before chr11 rather than vanilla ascii sorting.
 */
public class NaturalSorter implements Comparator<String> {

  /**
   * Static object for reuse.
   */
  public static final Comparator<String> INSTANCE = new NaturalSorter();

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  @Override
  public int compare(String o1, String o2) {
    char[] a = o1.toCharArray();
    char[] b = o2.toCharArray();

    int ia = 0;
    int ib = 0;
    char ca;
    char cb;
    int result;

    while (true) {
      // Look at characters one at a time in each string
      ca = charAt(a, ia);
      cb = charAt(b, ib);

      //
      // Skip over leading spaces or zeros
      //

      while (isLeading(ca)) {
        ca = charAt(a, ++ia);
      }

      while (isLeading(cb)) {
        cb = charAt(b, ++ib);
      }

      //
      // Compare strings
      //

      // If the leading portion of the strings are the same, we
      // try to sort by checking runs of digits in the strings and
      // compare them numerically
      if (Character.isDigit(ca) && Character.isDigit(cb)) {
        result = compareNumericalSuffixes(a, ia, b, ib);

        // If there are differences in the numbers, we have
        // examined enough of the strings to determine how to
        // sort them. Otherwise, we keep checking
        if (result != 0) {
          return result;
        }
      }

      // If ca and cb are 0 it means we have reached the end of both
      // strings and we are looking at null/terminating characters. It
      // also means the strings are the same so return 0.
      if (ca == 0 && cb == 0) {
        return 0;
      }

      // If at any point we can determine the strings are different,
      // rank them by testing which character is smaller than the other.
      if (ca < cb) {
        return -1;
      } else if (ca > cb) {
        return 1;
      } else {
        // Chars in both string are the same so move to the next index.
        ++ia;
        ++ib;
      }
    }
  }

  /**
   * Returns true if the char is a space or zero since we ignore leading runs of
   * these characters.
   *
   * @param c the c
   * @return true, if is leading
   */
  private static boolean isLeading(char c) {
    return Character.isSpaceChar(c) || c == '0';
  }

  /**
   * Compares the suffix of two strings to look for runs of digits to make a
   * numerical sort comparison. The prefix of both strings is by definition the
   * same so it cannot be used for sorting.
   *
   * @param a  String a.
   * @param ai The index in a where to begin.
   * @param b  String b.
   * @param bi The index in b where to begin.
   * @return the int
   */
  private int compareNumericalSuffixes(char[] a, int ai, char[] b, int bi) {
    int bias = 0;

    // The longest run of digits wins. That aside, the greatest
    // value wins, but we can't know that it will until we've scanned
    // both numbers to know that they have the same magnitude, so we
    // remember it in BIAS.

    while (true) {
      char ca = charAt(a, ai++);
      char cb = charAt(b, bi++);

      // Either we come to the end of a run of numbers or the
      // strings themselves
      if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
        return bias;
      }

      // A has fewer digits than B so A must be smaller and come
      // first
      if (!Character.isDigit(ca)) {
        return -1;
      }

      // B has as fewer digits than A so A must be larger so it comes
      // after B
      if (!Character.isDigit(cb)) {
        return 1;
      }

      // The first time we encounter a difference, record who is
      // smaller
      if (ca < cb) {
        if (bias == 0) {
          bias = -1;
        }
      }

      if (ca > cb) {
        if (bias == 0) {
          bias = 1;
        }
      }
    }
  }

  /**
   * Returns a char from a char array or 0 if the bounds of the array are
   * exceeded. Guaranteed to return a value rather than throwing an exception.
   *
   * @param s the s
   * @param i the i
   * @return the char
   */
  private static char charAt(final char[] s, int i) {
    if (i >= s.length) {
      return 0;
    } else {
      return s[i];
    }
  }
}