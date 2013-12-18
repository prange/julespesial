package fj.data.fingertrees;

import fj.Func;
import fj.P2;
import static fj.Bottom.error;

/**
 * The empty tree.
 */
public final class Empty<V, A> extends FingerTree<V, A> {
  Empty(final Measured<V, A> m) {
    super(m);
  }

  @Override public FingerTree<V, A> cons(final A a) {
    return new Single<V, A>(measured(), a);
  }

  @Override public FingerTree<V, A> snoc(final A a) {
    return cons(a);
  }

  @Override public FingerTree<V, A> append(final FingerTree<V, A> t) {
    return t;
  }

  @Override public P2<Integer, A> lookup(final Func<V, Integer> o, final int i) {
    throw error("Lookup of empty tree.");
  }

  @Override public <B> B foldRight(final Func<A, Func<B, B>> aff, final B z) {
    return z;
  }

  public A reduceRight(final Func<A, Func<A, A>> aff) {
    throw error("Reduction of empty tree");
  }

  @Override public <B> B foldLeft(final Func<B, Func<A, B>> bff, final B z) {
    return z;
  }

  @Override public A reduceLeft(final Func<A, Func<A, A>> aff) {
    throw error("Reduction of empty tree");
  }

  @Override public <B> FingerTree<V, B> map(final Func<A, B> abf, final Measured<V, B> m) {
    return new Empty<V, B>(m);
  }

  /**
   * Returns zero.
   *
   * @return Zero.
   */
  public V measure() {
    return measured().zero();
  }

  /**
   * Pattern matching on the structure of this tree. Matches the empty tree.
   */
  @Override public <B> B match(
      final Func<Empty<V, A>, B> empty, final Func<Single<V, A>, B> single, final Func<Deep<V, A>, B> deep) {
    return empty.f(this);
  }


}
