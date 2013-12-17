package fj.function;

import fj.Func;
import fj.Func2;

import static fj.Function.curry;

/**
 * Curried string functions.
 *
 * @version %build.number%
 */
public final class Strings {
  private Strings() {
    throw new UnsupportedOperationException();
  }

  /**
   * This function checks if a given String is neither <code>null</code> nor empty.
   */
  public static final Func<String, Boolean> isNotNullOrEmpty = new Func<String, Boolean>() {
    @Override
    public Boolean f(final String a) {
      return a != null && a.length() > 0;
    }
  };

  /**
   * A curried version of {@link String#isEmpty()}.
   */
  public static final Func<String, Boolean> isEmpty = new Func<String, Boolean>() {
    public Boolean f(final String s) {
      return s.length() == 0;
    }
  };

  /**
   * A curried version of {@link String#length()}.
   */
  public static final Func<String, Integer> length = new Func<String, Integer>() {
    public Integer f(final String s) {
      return s.length();
    }
  };

  /**
   * A curried version of {@link String#contains(CharSequence)}.
   * The function returns true if the second argument contains the first.
   */
  public static final Func<String, Func<String, Boolean>> contains = curry(new Func2<String, String, Boolean>() {
    public Boolean f(final String s1, final String s2) {
      return s2.contains(s1);
    }
  });

  /**
   * A curried version of {@link String#matches(String)}.
   * The function returns true if the second argument matches the first.
   */
  public static final Func<String, Func<String, Boolean>> matches = curry(new Func2<String, String, Boolean>() {
    public Boolean f(final String s1, final String s2) {
      return s2.matches(s1);
    }
  });

}
