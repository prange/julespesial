package fj;

import static fj.Unit.unit;

/**
 * Represents a side-effect.
 *
 * @version %build.number%
 */
public interface Effect<A> {
  public abstract void e(A a);


  /**
   * Returns a function for the given effect.
   *
   * @return The function using the given effect.
   */
  public default Func<A, Unit> e() {
    return new Func<A, Unit>() {
      public Unit f(final A a) {
        e(a);
        return unit();
      }
    };
  }

  /**
   * A contra-variant functor on effect.
   *
   * @param f The function to map over the effect.
   * @return An effect after a contra-variant map.
   */
  public default <B> Effect<B> comap(final Func<B, A> f) {
    return new Effect<B>() {
      public void e(final B b) {
        Effect.this.e(f.f(b));
      }
    };
  }
  
  public static <A> Effect<A> f(final Func<A, Unit> f) {
    return new Effect<A>() {
      public void e(final A a) {
        f.f(a);
      }
    };
  }
}
