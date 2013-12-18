package fj.data.fingertrees;

import fj.Func;
import fj.Func2;
import fj.P2;
import static fj.Function.curry;

/**
 * An inner node of the 2-3 tree.
 */
public abstract class Node<V, A> {
  private final Measured<V, A> m;
  private final V measure;

  public abstract <B> B foldRight(final Func<A, Func<B, B>> f, final B z);

  public abstract <B> B foldLeft(final Func<B, Func<A, B>> f, final B z);

  public static <V, A, B> Func<B, Func<Node<V, A>, B>> foldLeft_(final Func<B, Func<A, B>> bff) {
    return curry(new Func2<B, Node<V, A>, B>() {
      public B f(final B b, final Node<V, A> node) { return node.foldLeft(bff, b); }
    });
  }

  public static <V, A, B> Func<B, Func<Node<V, A>, B>> foldRight_(final Func<A, Func<B, B>> aff) {
    return curry(new Func2<B, Node<V, A>, B>() {
      public B f(final B b, final Node<V, A> node) { return node.foldRight(aff, b); }
    });
  }

  public final <B> Node<V, B> map(final Func<A, B> f, final Measured<V, B> m) {
    return match(new Func<Node2<V, A>, Node<V, B>>() {
      public Node<V, B> f(final Node2<V, A> node2) {
        return new Node2<V, B>(m, node2.toVector().map(f));
      }
    }, new Func<Node3<V, A>, Node<V, B>>() {
      public Node<V, B> f(final Node3<V, A> node3) {
        return new Node3<V, B>(m, node3.toVector().map(f));
      }
    });
  }

  public static <V, A, B> Func<Node<V, A>, Node<V, B>> liftM(final Func<A, B> f, final Measured<V, B> m) {
    return new Func<Node<V, A>, Node<V, B>>() {
      public Node<V, B> f(final Node<V, A> node) {
        return node.map(f, m);
      }
    };
  }

  public abstract Digit<V, A> toDigit();

  Node(final Measured<V, A> m, final V measure) {
    this.m = m;
    this.measure = measure;
  }

  public final V measure() {
    return measure;
  }

  Measured<V, A> measured() {
    return m;
  }

  public abstract P2<Integer, A> lookup(final Func<V, Integer> o, final int i);

  public abstract <B> B match(final Func<Node2<V, A>, B> n2, final Func<Node3<V, A>, B> n3);
}
