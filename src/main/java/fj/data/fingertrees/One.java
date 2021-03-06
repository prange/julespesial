package fj.data.fingertrees;

import fj.Func;

/**
 * A single-element prefix or suffix of a finger tree.
 */
public final class One<V, A> extends Digit<V, A> {
  private final A a;

  One(final Measured<V, A> m, final A a) {
    super(m);
    this.a = a;
  }

  public <B> B foldRight(final Func<A, Func<B, B>> aff, final B z) {
    return aff.f(a).f(z);
  }

  public <B> B foldLeft(final Func<B, Func<A, B>> bff, final B z) {
    return bff.f(z).f(a);
  }

  @Override public <B> B match(
      final Func<One<V, A>, B> one, final Func<Two<V, A>, B> two, final Func<Three<V, A>, B> three,
      final Func<Four<V, A>, B> four) {
    return one.f(this);
  }

  /**
   * Returns the single element in this digit.
   *
   * @return the single element in this digit.
   */
  public A value() {
    return a;
  }
}
