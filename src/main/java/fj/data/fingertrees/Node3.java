package fj.data.fingertrees;

import fj.Func;
import fj.data.vector.V3;
import fj.P2;

/**
 * A three-element inner tree node.
 */
public final class Node3<V, A> extends Node<V, A> {
  private final V3<A> as;

  Node3(final Measured<V, A> m, final V3<A> as) {
    super(m, m.sum(m.measure(as._1()), m.sum(m.measure(as._2()), m.measure(as._3()))));
    this.as = as;
  }

  public <B> B foldRight(final Func<A, Func<B, B>> aff, final B z) {
    return aff.f(as._1()).f(aff.f(as._2()).f(aff.f(as._3()).f(z)));
  }

  public <B> B foldLeft(final Func<B, Func<A, B>> bff, final B z) {
    return bff.f(bff.f(bff.f(z).f(as._1())).f(as._2())).f(as._3());
  }

  public <B> B match(final Func<Node2<V, A>, B> n2, final Func<Node3<V, A>, B> n3) {
    return n3.f(this);
  }

  public Digit<V, A> toDigit() {
    return new Three<V, A>(measured(), as);
  }

  @SuppressWarnings({"ReturnOfNull"})
  public P2<Integer, A> lookup(final Func<V, Integer> o, final int i) {
    return null;  //TODO
  }

  public V3<A> toVector() {
    return as;
  }
}
