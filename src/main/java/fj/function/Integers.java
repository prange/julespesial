package fj.function;

import fj.Func;
import fj.Func2;
import static fj.Function.curry;

import fj.Monoid;
import fj.data.List;
import fj.data.Option;
import static fj.data.Option.some;
import static fj.data.Option.none;
import static fj.Semigroup.intAdditionSemigroup;
import static fj.Semigroup.intMultiplicationSemigroup;

import static java.lang.Math.abs;

/**
 * Curried functions over Integers.
 *
 * @version %build.number%
 */
public final class Integers {
  private Integers() {
    throw new UnsupportedOperationException();
  }

  /**
   * Curried Integer addition.
   */
  public static final Func<Integer, Func<Integer, Integer>> add = intAdditionSemigroup.sum();

  /**
   * Curried Integer multiplication.
   */
  public static final Func<Integer, Func<Integer, Integer>> multiply = intMultiplicationSemigroup.sum();

  /**
   * Curried Integer subtraction.
   */
  public static final Func<Integer, Func<Integer, Integer>> subtract = curry(new Func2<Integer, Integer, Integer>() {
    public Integer f(final Integer x, final Integer y) {
      return x - y;
    }
  });

  /**
   * Negation.
   */
  public static final Func<Integer, Integer> negate = new Func<Integer, Integer>() {
    public Integer f(final Integer x) {
      return x * -1;
    }
  };

  /**
   * Absolute value.
   */
  public static final Func<Integer, Integer> abs = new Func<Integer, Integer>() {
    public Integer f(final Integer x) {
      return abs(x);
    }
  };

  /**
   * Remainder.
   */
  public static final Func<Integer, Func<Integer, Integer>> remainder = curry(new Func2<Integer, Integer, Integer>() {
    public Integer f(final Integer a, final Integer b) {
      return a % b;
    }
  });

  /**
   * Power.
   */
  public static final Func<Integer, Func<Integer, Integer>> power = curry(new Func2<Integer, Integer, Integer>() {
    public Integer f(final Integer a, final Integer b) {
      return (int) StrictMath.pow(a, b);
    }
  });

  /**
   * Evenness.
   */
  public static final Func<Integer, Boolean> even = new Func<Integer, Boolean>() {
    public Boolean f(final Integer i) {
      return i % 2 == 0;
    }
  };

  /**
   * Sums a list of integers.
   *
   * @param ints A list of integers to sum.
   * @return The sum of the integers in the list.
   */
  public static int sum(final List<Integer> ints) {
    return Monoid.intAdditionMonoid.sumLeft(ints);
  }

  /**
   * Returns the product of a list of integers.
   *
   * @param ints A list of integers to multiply together.
   * @return The product of the integers in the list.
   */
  public static int product(final List<Integer> ints) {
    return Monoid.intMultiplicationMonoid.sumLeft(ints);
  }

  /**
   * A function that converts strings to integers.
   *
   * @return A function that converts strings to integers.
   */
  public static Func<String, Option<Integer>> fromString() {
    return new Func<String, Option<Integer>>() {
      public Option<Integer> f(final String s) {
        try { return some(Integer.valueOf(s)); }
        catch (final NumberFormatException ignored) {
          return none();
        }
      }
    };
  }

  /**
   * A function that returns true if the given integer is greater than zero.
   */
  public static final Func<Integer, Boolean> gtZero = new Func<Integer, Boolean>() {
    public Boolean f(final Integer i) {
      return i > 0;
    }
  };

  /**
   * A function that returns true if the given integer is greater than or equal to zero.
   */
  public static final Func<Integer, Boolean> gteZero = new Func<Integer, Boolean>() {
    public Boolean f(final Integer i) {
      return i >= 0;
    }
  };

  /**
   * A function that returns true if the given integer is less than zero.
   */
  public static final Func<Integer, Boolean> ltZero = new Func<Integer, Boolean>() {
    public Boolean f(final Integer i) {
      return i < 0;
    }
  };

  /**
   * A function that returns true if the given integer is less than or equal to zero. 
   */
  public static final Func<Integer, Boolean> lteZero = new Func<Integer, Boolean>() {
    public Boolean f(final Integer i) {
      return i <= 0;
    }
  };
}
