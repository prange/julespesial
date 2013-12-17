package fj.function;

import fj.Func;
import fj.Func2;

import static fj.Function.curry;
import static fj.Semigroup.longAdditionSemigroup;
import static fj.Semigroup.longMultiplicationSemigroup;

import static java.lang.Math.abs;

/**
 * Curried functions over Longs.
 *
 * @version %build.number%
 */
public final class Longs {
  private Longs() {
    throw new UnsupportedOperationException();
  }

  /**
   * Curried Long addition.
   */
  public static final Func<Long, Func<Long, Long>> add = longAdditionSemigroup.sum();

  /**
   * Curried Long multiplication.
   */
  public static final Func<Long, Func<Long, Long>> multiply = longMultiplicationSemigroup.sum();

  /**
   * Curried Long subtraction.
   */
  public static final Func<Long, Func<Long, Long>> subtract = curry(new Func2<Long, Long, Long>() {
    public Long f(final Long x, final Long y) {
      return x - y;
    }
  });

  /**
   * Negation.
   */
  public static final Func<Long, Long> negate = new Func<Long, Long>() {
    public Long f(final Long x) {
      return x * -1L;
    }
  };

  /**
   * Absolute value.
   */
  public static final Func<Long, Long> abs = new Func<Long, Long>() {
    public Long f(final Long x) {
      return abs(x);
    }
  };

  /**
   * Remainder.
   */
  public static final Func<Long, Func<Long, Long>> remainder = curry(new Func2<Long, Long, Long>() {
    public Long f(final Long a, final Long b) {
      return a % b;
    }
  });
}
