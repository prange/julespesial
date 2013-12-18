package fj.data.fingertrees;

import fj.data.vector.V3;
import fj.Func;

/**
 * A three-element prefix or suffix of a finger tree.
 */
public final class Three<V, A> extends Digit<V, A> {
  private final V3<A> as;

  Three(final Measured<V, A> m, final V3<A> as) {
    super(m);
    this.as = as;
  }

  public <B> B foldRight(final Func<A, Func<B, B>> aff, final B z) {
    return aff.f(as._1()).f(aff.f(as._2()).f(aff.f(as._3()).f(z)));
  }

  public <B> B foldLeft(final Func<B, Func<A, B>> bff, final B z) {
    return as.toStream().foldLeft(bff, z);
  }

  @Override public <B> B match(
      final Func<One<V, A>, B> one, final Func<Two<V, A>, B> two, final Func<Three<V, A>, B> three,
      final Func<Four<V, A>, B> four) {
    return three.f(this);
  }

  /**
   * Returns the elements of this digit as a vector.
   *
   * @return the elements of this digit as a vector.
   */
  public V3<A> values() {
    return as;
  }
}
