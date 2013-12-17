package fj;

import fj.control.parallel.Promise;
import fj.data.Array;
import fj.data.IterableW;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.TreeZipper;
import fj.data.Zipper;

import static fj.data.Tree.node;
import static fj.P.p;
import static fj.data.IterableW.wrap;
import static fj.data.Set.iterableSet;
import static fj.data.TreeZipper.treeZipper;
import static fj.data.Zipper.zipper;

/**
 * A transformation function of arity-2 from <code>A</code> and <code>B</code> to <code>C</code>.
 * This type can be represented using the Java 7 closure syntax.
 *
 * @version %build.number%
 */
public interface Func2<A, B, C> {
  /**
   * Transform <code>A</code> and <code>B</code> to <code>C</code>.
   *
   * @param a The <code>A</code> to transform.
   * @param b The <code>B</code> to transform.
   * @return The result of the transformation.
   */
  public abstract C f(A a, B b);


  /**
   * Partial application.
   *
   * @param a The <code>A</code> to which to apply this function.
   * @return The function partially applied to the given argument.
   */
  public default Func<B, C> f(final A a) {
    return new Func<B, C>() {
      public C f(final B b) {
        return Func2.this.f(a, b);
      }
    };
  }

  /**
   * Curries this wrapped function to a wrapped function of arity-1 that returns another wrapped function.
   *
   * @return a wrapped function of arity-1 that returns another wrapped function.
   */
  public default Func<A, Func<B, C>> curry() {
    return new Func<A, Func<B, C>>() {
      public Func<B, C> f(final A a) {
        return new Func<B, C>() {
          public C f(final B b) {
            return Func2.this.f(a, b);
          }
        };
      }
    };
  }

  /**
   * Flips the arguments of this function.
   *
   * @return A new function with the arguments of this function flipped.
   */
  public default Func2<B, A, C> flip() {
    return new Func2<B, A, C>() {
      public C f(final B b, final A a) {
        return Func2.this.f(a, b);
      }
    };
  }

  /**
   * Uncurries this function to a function on tuples.
   *
   * @return A new function that calls this function with the elements of a given tuple.
   */
  public default Func<P2<A, B>, C> tuple() {
    return new Func<P2<A, B>, C>() {
      public C f(final P2<A, B> p) {
        return Func2.this.f(p._1(), p._2());
      }
    };
  }

  /**
   * Promotes this function to a function on Arrays.
   *
   * @return This function promoted to transform Arrays.
   */
  public default Func2<Array<A>, Array<B>, Array<C>> arrayM() {
    return new Func2<Array<A>, Array<B>, Array<C>>() {
      public Array<C> f(final Array<A> a, final Array<B> b) {
        return a.bind(b, Func2.this.curry());
      }
    };
  }

  /**
   * Promotes this function to a function on Promises.
   *
   * @return This function promoted to transform Promises.
   */
  public default Func2<Promise<A>, Promise<B>, Promise<C>> promiseM() {
    return new Func2<Promise<A>, Promise<B>, Promise<C>>() {
      public Promise<C> f(final Promise<A> a, final Promise<B> b) {
        return a.bind(b, Func2.this.curry());
      }
    };
  }

  /**
   * Promotes this function to a function on Iterables.
   *
   * @return This function promoted to transform Iterables.
   */
  public default Func2<Iterable<A>, Iterable<B>, IterableW<C>> iterableM() {
    return new Func2<Iterable<A>, Iterable<B>, IterableW<C>>() {
      public IterableW<C> f(final Iterable<A> a, final Iterable<B> b) {
        return IterableW.liftM2( Func2.this.curry()).f(a).f(b);
      }
    };
  }

  /**
   * Promotes this function to a function on Lists.
   *
   * @return This function promoted to transform Lists.
   */
  public default Func2<List<A>, List<B>, List<C>> listM() {
    return new Func2<List<A>, List<B>, List<C>>() {
      public List<C> f(final List<A> a, final List<B> b) {
        return List.liftM2( Func2.this.curry()).f(a).f(b);
      }
    };
  }

  /**
   * Promotes this function to a function on non-empty lists.
   *
   * @return This function promoted to transform non-empty lists.
   */
  public default Func2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>> nelM() {
    return new Func2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>>() {
      public NonEmptyList<C> f(final NonEmptyList<A> as, final NonEmptyList<B> bs) {
        return NonEmptyList.fromList(as.toList().bind(bs.toList(), Func2.this)).some();
      }
    };
  }

  /**
   * Promotes this function to a function on Options.
   *
   * @return This function promoted to transform Options.
   */
  public default Func2<Option<A>, Option<B>, Option<C>> optionM() {
    return new Func2<Option<A>, Option<B>, Option<C>>() {
      public Option<C> f(final Option<A> a, final Option<B> b) {
        return Option.liftM2( Func2.this.curry()).f(a).f(b);
      }
    };
  }

  /**
   * Promotes this function to a function on Sets.
   *
   * @param o An ordering for the result of the promoted function.
   * @return This function promoted to transform Sets.
   */
  public default Func2<Set<A>, Set<B>, Set<C>> setM(final Ord<C> o) {
    return new Func2<Set<A>, Set<B>, Set<C>>() {
      public Set<C> f(final Set<A> as, final Set<B> bs) {
        Set<C> cs = Set.empty(o);
        for (final A a : as)
          for (final B b : bs)
            cs = cs.insert( Func2.this.f(a, b));
        return cs;
      }
    };
  }

  /**
   * Promotes this function to a function on Streams.
   *
   * @return This function promoted to transform Streams.
   */
  public default Func2<Stream<A>, Stream<B>, Stream<C>> streamM() {
    return new Func2<Stream<A>, Stream<B>, Stream<C>>() {
      public Stream<C> f(final Stream<A> as, final Stream<B> bs) {
        return as.bind(bs, Func2.this);
      }
    };
  }

  /**
   * Promotes this function to a function on Trees.
   *
   * @return This function promoted to transform Trees.
   */
  public default Func2<Tree<A>, Tree<B>, Tree<C>> treeM() {
    return new Func2<Tree<A>, Tree<B>, Tree<C>>() {
      public Tree<C> f(final Tree<A> as, final Tree<B> bs) {
        final Func2<Tree<A>, Tree<B>, Tree<C>> self = this;
        return node( Func2.this.f(as.root(), bs.root()), new P1<Stream<Tree<C>>>() {
          public Stream<Tree<C>> _1() {
            return self.streamM().f(as.subForest()._1(), bs.subForest()._1());
          }
        });
      }
    };
  }

  /**
   * Promotes this function to zip two arrays, applying the function lock-step over both Arrays.
   *
   * @return A function that zips two arrays with this function.
   */
  public default Func2<Array<A>, Array<B>, Array<C>> zipArrayM() {
    return new Func2<Array<A>, Array<B>, Array<C>>() {
      public Array<C> f(final Array<A> as, final Array<B> bs) {
        return as.zipWith(bs, Func2.this);
      }
    };
  }

  /**
   * Promotes this function to zip two iterables, applying the function lock-step over both iterables.
   *
   * @return A function that zips two iterables with this function.
   */
  public default Func2<Iterable<A>, Iterable<B>, Iterable<C>> zipIterableM() {
    return new Func2<Iterable<A>, Iterable<B>, Iterable<C>>() {
      public Iterable<C> f(final Iterable<A> as, final Iterable<B> bs) {
        return wrap(as).zipWith(bs, Func2.this);
      }
    };
  }

  /**
   * Promotes this function to zip two lists, applying the function lock-step over both lists.
   *
   * @return A function that zips two lists with this function.
   */
  public default Func2<List<A>, List<B>, List<C>> zipListM() {
    return new Func2<List<A>, List<B>, List<C>>() {
      public List<C> f(final List<A> as, final List<B> bs) {
        return as.zipWith(bs, Func2.this);
      }
    };
  }


  /**
   * Promotes this function to zip two streams, applying the function lock-step over both streams.
   *
   * @return A function that zips two streams with this function.
   */
  public default Func2<Stream<A>, Stream<B>, Stream<C>> zipStreamM() {
    return new Func2<Stream<A>, Stream<B>, Stream<C>>() {
      public Stream<C> f(final Stream<A> as, final Stream<B> bs) {
        return as.zipWith(bs, Func2.this);
      }
    };
  }

  /**
   * Promotes this function to zip two non-empty lists, applying the function lock-step over both lists.
   *
   * @return A function that zips two non-empty lists with this function.
   */
  public default Func2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>> zipNelM() {
    return new Func2<NonEmptyList<A>, NonEmptyList<B>, NonEmptyList<C>>() {
      public NonEmptyList<C> f(final NonEmptyList<A> as, final NonEmptyList<B> bs) {
        return NonEmptyList.fromList(as.toList().zipWith(bs.toList(), Func2.this)).some();
      }
    };
  }

  /**
   * Promotes this function to zip two sets, applying the function lock-step over both sets.
   *
   * @param o An ordering for the resulting set.
   * @return A function that zips two sets with this function.
   */
  public default Func2<Set<A>, Set<B>, Set<C>> zipSetM(final Ord<C> o) {
    return new Func2<Set<A>, Set<B>, Set<C>>() {
      public Set<C> f(final Set<A> as, final Set<B> bs) {
        return iterableSet(o, as.toStream().zipWith(bs.toStream(), Func2.this));
      }
    };
  }

  /**
   * Promotes this function to zip two trees, applying the function lock-step over both trees.
   * The structure of the resulting tree is the structural intersection of the two trees.
   *
   * @return A function that zips two trees with this function.
   */
  public default Func2<Tree<A>, Tree<B>, Tree<C>> zipTreeM() {
    return new Func2<Tree<A>, Tree<B>, Tree<C>>() {
      public Tree<C> f(final Tree<A> ta, final Tree<B> tb) {
        final Func2<Tree<A>, Tree<B>, Tree<C>> self = this;
        return node( Func2.this.f(ta.root(), tb.root()), new P1<Stream<Tree<C>>>() {
          public Stream<Tree<C>> _1() {
            return self.zipStreamM().f(ta.subForest()._1(), tb.subForest()._1());
          }
        });
      }
    };
  }

  /**
   * Promotes this function to zip two zippers, applying the function lock-step over both zippers in both directions.
   * The structure of the resulting zipper is the structural intersection of the two zippers.
   *
   * @return A function that zips two zippers with this function.
   */
  public default Func2<Zipper<A>, Zipper<B>, Zipper<C>> zipZipperM() {
    return new Func2<Zipper<A>, Zipper<B>, Zipper<C>>() {
      @SuppressWarnings({"unchecked"})
      public Zipper<C> f(final Zipper<A> ta, final Zipper<B> tb) {
        final Func2<Stream<A>, Stream<B>, Stream<C>> sf = Func2.this.zipStreamM();
        return zipper(sf.f(ta.lefts(), tb.lefts()), Func2.this.f(ta.focus(), tb.focus()), sf.f(ta.rights(), tb.rights()));
      }
    };
  }

  /**
   * Promotes this function to zip two TreeZippers, applying the function lock-step over both zippers in all directions.
   * The structure of the resulting TreeZipper is the structural intersection of the two TreeZippers.
   *
   * @return A function that zips two TreeZippers with this function.
   */
  public default Func2<TreeZipper<A>, TreeZipper<B>, TreeZipper<C>> zipTreeZipperM() {
    return new Func2<TreeZipper<A>, TreeZipper<B>, TreeZipper<C>>() {
      @SuppressWarnings({"unchecked"})
      public TreeZipper<C> f(final TreeZipper<A> ta, final TreeZipper<B> tb) {
        final Func2<Stream<Tree<A>>, Stream<Tree<B>>, Stream<Tree<C>>> sf = Func2.this.treeM().zipStreamM();
        final
        Func2<Stream<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>>,
                    Stream<P3<Stream<Tree<B>>, B, Stream<Tree<B>>>>,
                    Stream<P3<Stream<Tree<C>>, C, Stream<Tree<C>>>>>
            pf =
            new Func2<P3<Stream<Tree<A>>, A, Stream<Tree<A>>>,
                            P3<Stream<Tree<B>>, B, Stream<Tree<B>>>,
                            P3<Stream<Tree<C>>, C, Stream<Tree<C>>>>() {
              public P3<Stream<Tree<C>>, C, Stream<Tree<C>>> f(final P3<Stream<Tree<A>>, A, Stream<Tree<A>>> pa,
                                                               final P3<Stream<Tree<B>>, B, Stream<Tree<B>>> pb) {
                return p( Func2.this.treeM().zipStreamM().f(pa._1(), pb._1()), Func2.this.f(pa._2(), pb._2()),
                         Func2.this.treeM().zipStreamM().f(pa._3(), pb._3()));
              }
            }.zipStreamM();
        return treeZipper( Func2.this.treeM().f(ta.p()._1(), tb.p()._1()), sf.f(ta.lefts(), tb.lefts()),
                          sf.f(ta.rights(), tb.rights()), pf.f(ta.p()._4(), tb.p()._4()));
      }
    };
  }
}
