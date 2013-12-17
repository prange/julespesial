package fj;

import fj.control.parallel.Actor;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.data.Array;
import fj.data.Either;
import fj.data.IterableW;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.TreeZipper;
import fj.data.Validation;
import fj.data.Zipper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import static fj.data.Option.some;
import static fj.data.Stream.iterableStream;
import static fj.data.Zipper.fromStream;

/**
 * A transformation or function from <code>A</code> to <code>B</code>. This type can be represented
 * using the Java 7 closure syntax.
 *
 * @version %build.number%
 */
public interface Func<A, B> {
  /**
   * Transform <code>A</code> to <code>B</code>.
   *
   * @param a The <code>A</code> to transform.
   * @return The result of the transformation.
   */
  public abstract B f(A a);


  /**
   * Function composition
   *
   * @param g A function to compose with this one.
   * @return The composed function such that this function is applied last.
   */
  public default <C> Func<C, B> o(final Func<C, A> g) {
    return new Func<C, B>() {
      public B f(final C c) {
        return Func.this.f( g.f( c ) );
      }
    };
  }

  /**
   * First-class function composition
   *
   * @return A function that composes this function with another.
   */
  public default <C> Func<Func<C, A>, Func<C, B>> o() {
    return new Func<Func<C, A>, Func<C, B>>() {
      public Func<C, B> f(final Func<C, A> g) {
        return Func.this.o(g);
      }
    };
  }

  /**
   * Function composition flipped.
   *
   * @param g A function with which to compose this one.
   * @return The composed function such that this function is applied first.
   */
  @SuppressWarnings({"unchecked"})
  public default <C> Func<A, C> andThen(final Func<B, C> g) {
    return g.o(this);
  }

  /**
   * First-class composition flipped.
   *
   * @return A function that invokes this function and then a given function on the result.
   */
  public default <C> Func<Func<B, C>, Func<A, C>> andThen() {
    return new Func<Func<B, C>, Func<A, C>>() {
      public Func<A, C> f(final Func<B, C> g) {
        return Func.this.andThen(g);
      }
    };
  }

  /**
   * Binds a given function across this function (Reader Monad).
   *
   * @param g A function that takes the return value of this function as an argument, yielding a new function.
   * @return A function that invokes this function on its argument and then the given function on the result.
   */
  public default <C> Func<A, C> bind(final Func<B, Func<A, C>> g) {
    return new Func<A, C>() {
      @SuppressWarnings({"unchecked"})
      public C f(final A a) {
        return g.f( Func.this.f( a ) ).f( a );
      }
    };
  }

  /**
   * First-class function binding.
   *
   * @return A function that binds another function across this function.
   */
  public default <C> Func<Func<B, Func<A, C>>, Func<A, C>> bind() {
    return new Func<Func<B, Func<A, C>>, Func<A, C>>() {
      public Func<A, C> f(final Func<B, Func<A, C>> g) {
        return Func.this.bind(g);
      }
    };
  }

  /**
   * Function application in an environment (Applicative Functor).
   *
   * @param g A function with the same argument type as this function, yielding a function that takes the return
   *          value of this function.
   * @return A new function that invokes the given function on its argument, yielding a new function that is then
   *         applied to the result of applying this function to the argument.
   */
  public default <C> Func<A, C> apply(final Func<A, Func<B, C>> g) {
    return new Func<A, C>() {
      @SuppressWarnings({"unchecked"})
      public C f(final A a) {
        return g.f( a ).f( Func.this.f( a ) );
      }
    };
  }

  /**
   * First-class function application in an environment.
   *
   * @return A function that applies a given function within the environment of this function.
   */
  public default <C> Func<Func<A, Func<B, C>>, Func<A, C>> apply() {
    return new Func<Func<A, Func<B, C>>, Func<A, C>>() {
      public Func<A, C> f(final Func<A, Func<B, C>> g) {
        return Func.this.apply(g);
      }
    };
  }

  /**
   * Applies this function over the arguments of another function.
   *
   * @param g The function over whose arguments to apply this function.
   * @return A new function that invokes this function on its arguments before invoking the given function.
   */
  public default <C> Func<A, Func<A, C>> on(final Func<B, Func<B, C>> g) {
    return new Func<A, Func<A, C>>() {
      public Func<A, C> f(final A a1) {
        return new Func<A, C>() {
          @SuppressWarnings({"unchecked"})
          public C f(final A a2) {
            return g.f( Func.this.f( a1 ) ).f( Func.this.f( a2 ) );
          }
        };
      }
    };
  }


  /**
   * Applies this function over the arguments of another function.
   *
   * @return A function that applies this function over the arguments of another function.
   */
  public default <C> Func<Func<B, Func<B, C>>, Func<A, Func<A, C>>> on() {
    return new Func<Func<B, Func<B, C>>, Func<A, Func<A, C>>>() {
      public Func<A, Func<A, C>> f(final Func<B, Func<B, C>> g) {
        return Func.this.on(g);
      }
    };
  }

  /**
   * Promotes this function so that it returns its result in a product-1. Kleisli arrow for P1.
   *
   * @return This function promoted to return its result in a product-1.
   */
  public default Func<A, P1<B>> lazy() {
    return new Func<A, P1<B>>() {
      public P1<B> f(final A a) {
        return new P1<B>() {
          public B _1() {
            return Func.this.f( a );
          }
        };
      }
    };
  }

  /**
   * Promotes this function to map over a product-1.
   *
   * @return This function promoted to map over a product-1.
   */
  public default Func<P1<A>, P1<B>> mapP1() {
    return new Func<P1<A>, P1<B>>() {
      public P1<B> f(final P1<A> p) {
        return p.map( Func.this);
      }
    };
  }

  /**
   * Promotes this function so that it returns its result in an Option. Kleisli arrow for Option.
   *
   * @return This function promoted to return its result in an Option.
   */
  public default Func<A, Option<B>> optionK() {
    return new Func<A, Option<B>>() {
      public Option<B> f(final A a) {
        return some( Func.this.f( a ));
      }
    };
  }

  /**
   * Promotes this function to map over an optional value.
   *
   * @return This function promoted to map over an optional value.
   */
  public default Func<Option<A>, Option<B>> mapOption() {
    return new Func<Option<A>, Option<B>>() {
      public Option<B> f(final Option<A> o) {
        return o.map( Func.this);
      }
    };
  }

  /**
   * Promotes this function so that it returns its result in a List. Kleisli arrow for List.
   *
   * @return This function promoted to return its result in a List.
   */
  public default Func<A, List<B>> listK() {
    return new Func<A, List<B>>() {
      public List<B> f(final A a) {
        return List.single( Func.this.f( a ));
      }
    };
  }

  /**
   * Promotes this function to map over a List.
   *
   * @return This function promoted to map over a List.
   */
  public default Func<List<A>, List<B>> mapList() {
    return new Func<List<A>, List<B>>() {
      public List<B> f(final List<A> x) {
        return x.map( Func.this);
      }
    };
  }

  /**
   * Promotes this function so that it returns its result in a Stream. Kleisli arrow for Stream.
   *
   * @return This function promoted to return its result in a Stream.
   */
  public default Func<A, Stream<B>> streamK() {
    return new Func<A, Stream<B>>() {
      public Stream<B> f(final A a) {
        return Stream.single( Func.this.f( a ));
      }
    };
  }

  /**
   * Promotes this function to map over a Stream.
   *
   * @return This function promoted to map over a Stream.
   */
  public default Func<Stream<A>, Stream<B>> mapStream() {
    return new Func<Stream<A>, Stream<B>>() {
      public Stream<B> f(final Stream<A> x) {
        return x.map( Func.this);
      }
    };
  }

  /**
   * Promotes this function so that it returns its result in a Array. Kleisli arrow for Array.
   *
   * @return This function promoted to return its result in a Array.
   */
  public default Func<A, Array<B>> arrayK() {
    return new Func<A, Array<B>>() {
      public Array<B> f(final A a) {
        return Array.single( Func.this.f( a ));
      }
    };
  }

  /**
   * Promotes this function to map over a Array.
   *
   * @return This function promoted to map over a Array.
   */
  public default Func<Array<A>, Array<B>> mapArray() {
    return new Func<Array<A>, Array<B>>() {
      public Array<B> f(final Array<A> x) {
        return x.map( Func.this);
      }
    };
  }

  /**
   * Returns a function that comaps over a given actor.
   *
   * @return A function that comaps over a given actor.
   */
  public default Func<Actor<B>, Actor<A>> comapActor() {
    return new Func<Actor<B>, Actor<A>>() {
      public Actor<A> f(final Actor<B> a) {
        return a.comap( Func.this);
      }
    };
  }

  /**
   * Promotes this function to a concurrent function that returns a Promise of a value.
   *
   * @param s A parallel strategy for concurrent execution.
   * @return A concurrent function that returns a Promise of a value.
   */
  public default Func<A, Promise<B>> promiseK(final Strategy<Unit> s) {
    return Promise.promise(s, this);
  }

  /**
   * Promotes this function to map over a Promise.
   *
   * @return This function promoted to map over Promises.
   */
  public default Func<Promise<A>, Promise<B>> mapPromise() {
    return new Func<Promise<A>, Promise<B>>() {
      public Promise<B> f(final Promise<A> p) {
        return p.fmap( Func.this);
      }
    };
  }

  /**
   * Promotes this function so that it returns its result on the left side of an Either.
   * Kleisli arrow for the Either left projection.
   *
   * @return This function promoted to return its result on the left side of an Either.
   */
  @SuppressWarnings({"unchecked"})
  public default <C> Func<A, Either<B, C>> eitherLeftK() {
    return Either.<B, C>left_().o( Func.this);
  }

  /**
   * Promotes this function so that it returns its result on the right side of an Either.
   * Kleisli arrow for the Either right projection.
   *
   * @return This function promoted to return its result on the right side of an Either.
   */
  @SuppressWarnings({"unchecked"})
  public default <C> Func<A, Either<C, B>> eitherRightK() {
    return Either.<C, B>right_().o( Func.this);
  }

  /**
   * Promotes this function to map over the left side of an Either.
   *
   * @return This function promoted to map over the left side of an Either.
   */
  @SuppressWarnings({"unchecked"})
  public default <X> Func<Either<A, X>, Either<B, X>> mapLeft() {
    return Either.<A, X, B>leftMap_().f( Func.this );
  }

  /**
   * Promotes this function to map over the right side of an Either.
   *
   * @return This function promoted to map over the right side of an Either.
   */
  @SuppressWarnings({"unchecked"})
  public default <X> Func<Either<X, A>, Either<X, B>> mapRight() {
    return Either.<X, A, B>rightMap_().f( Func.this );
  }

  /**
   * Returns a function that returns the left side of a given Either, or this function applied to the right side.
   *
   * @return a function that returns the left side of a given Either, or this function applied to the right side.
   */
  public default Func<Either<B, A>, B> onLeft() {
    return new Func<Either<B, A>, B>() {
      public B f(final Either<B, A> either) {
        return either.left().on( Func.this);
      }
    };
  }

  /**
   * Returns a function that returns the right side of a given Either, or this function applied to the left side.
   *
   * @return a function that returns the right side of a given Either, or this function applied to the left side.
   */
  public default Func<Either<A, B>, B> onRight() {
    return new Func<Either<A, B>, B>() {
      public B f(final Either<A, B> either) {
        return either.right().on( Func.this);
      }
    };
  }

  /**
   * Promotes this function to return its value in an Iterable.
   *
   * @return This function promoted to return its value in an Iterable.
   */
  @SuppressWarnings({"unchecked"})
  public default Func<A, IterableW<B>> iterableK() {
    return IterableW.<A, B>arrow().f( Func.this );
  }

  /**
   * Promotes this function to map over Iterables.
   *
   * @return This function promoted to map over Iterables.
   */
  @SuppressWarnings({"unchecked"})
  public default Func<Iterable<A>, IterableW<B>> mapIterable() {
    return IterableW.<A, B>map().f( Func.this ).o(IterableW.<A, Iterable<A>>wrap());
  }

  /**
   * Promotes this function to return its value in a NonEmptyList.
   *
   * @return This function promoted to return its value in a NonEmptyList.
   */
  @SuppressWarnings({"unchecked"})
  public default Func<A, NonEmptyList<B>> nelK() {
    return NonEmptyList.<B>nel().o( Func.this);
  }

  /**
   * Promotes this function to map over a NonEmptyList.
   *
   * @return This function promoted to map over a NonEmptyList.
   */
  public default Func<NonEmptyList<A>, NonEmptyList<B>> mapNel() {
    return new Func<NonEmptyList<A>, NonEmptyList<B>>() {
      public NonEmptyList<B> f(final NonEmptyList<A> list) {
        return list.map( Func.this);
      }
    };
  }

  /**
   * Promotes this function to return its value in a Set.
   *
   * @param o An order for the set.
   * @return This function promoted to return its value in a Set.
   */
  public default Func<A, Set<B>> setK(final Ord<B> o) {
    return new Func<A, Set<B>>() {
      public Set<B> f(final A a) {
        return Set.single(o, Func.this.f( a ));
      }
    };
  }

  /**
   * Promotes this function to map over a Set.
   *
   * @param o An order for the resulting set.
   * @return This function promoted to map over a Set.
   */
  public default Func<Set<A>, Set<B>> mapSet(final Ord<B> o) {
    return new Func<Set<A>, Set<B>>() {
      public Set<B> f(final Set<A> set) {
        return set.map(o, Func.this);
      }
    };
  }

  /**
   * Promotes this function to return its value in a Tree.
   *
   * @return This function promoted to return its value in a Tree.
   */
  public default Func<A, Tree<B>> treeK() {
    return new Func<A, Tree<B>>() {
      public Tree<B> f(final A a) {
        return Tree.leaf( Func.this.f( a ));
      }
    };
  }

  /**
   * Promotes this function to map over a Tree.
   *
   * @return This function promoted to map over a Tree.
   */
  @SuppressWarnings({"unchecked"})
  public default Func<Tree<A>, Tree<B>> mapTree() {
    return Tree.<A, B>fmap_().f( Func.this );
  }

  /**
   * Returns a function that maps this function over a tree and folds it with the given monoid.
   *
   * @param m The monoid with which to fold the mapped tree.
   * @return a function that maps this function over a tree and folds it with the given monoid.
   */
  public default Func<Tree<A>, B> foldMapTree(final Monoid<B> m) {
    return Tree.foldMap_( Func.this, m);
  }

  /**
   * Promotes this function to return its value in a TreeZipper.
   *
   * @return This function promoted to return its value in a TreeZipper.
   */
  public default Func<A, TreeZipper<B>> treeZipperK() {
    return treeK().andThen(TreeZipper.<B>fromTree());
  }

  /**
   * Promotes this function to map over a TreeZipper.
   *
   * @return This function promoted to map over a TreeZipper.
   */
  public default Func<TreeZipper<A>, TreeZipper<B>> mapTreeZipper() {
    return new Func<TreeZipper<A>, TreeZipper<B>>() {
      public TreeZipper<B> f(final TreeZipper<A> zipper) {
        return zipper.map( Func.this);
      }
    };
  }

  /**
   * Promotes this function so that it returns its result on the failure side of a Validation.
   * Kleisli arrow for the Validation failure projection.
   *
   * @return This function promoted to return its result on the failure side of a Validation.
   */
  public default <C> Func<A, Validation<B, C>> failK() {
    return new Func<A, Validation<B, C>>() {
      public Validation<B, C> f(final A a) {
        return Validation.fail( Func.this.f( a ));
      }
    };
  }

  /**
   * Promotes this function so that it returns its result on the success side of an Validation.
   * Kleisli arrow for the Validation success projection.
   *
   * @return This function promoted to return its result on the success side of an Validation.
   */
  public default <C> Func<A, Validation<C, B>> successK() {
    return new Func<A, Validation<C, B>>() {
      public Validation<C, B> f(final A a) {
        return Validation.success( Func.this.f( a ));
      }
    };
  }

  /**
   * Promotes this function to map over the failure side of a Validation.
   *
   * @return This function promoted to map over the failure side of a Validation.
   */
  public default <X> Func<Validation<A, X>, Validation<B, X>> mapFail() {
    return new Func<Validation<A, X>, Validation<B, X>>() {
      public Validation<B, X> f(final Validation<A, X> validation) {
        return validation.f().map( Func.this);
      }
    };
  }

  /**
   * Promotes this function to map over the success side of a Validation.
   *
   * @return This function promoted to map over the success side of a Validation.
   */
  public default <X> Func<Validation<X, A>, Validation<X, B>> mapSuccess() {
    return new Func<Validation<X, A>, Validation<X, B>>() {
      public Validation<X, B> f(final Validation<X, A> validation) {
        return validation.map( Func.this);
      }
    };
  }

  /**
   * Returns a function that returns the failure side of a given Validation,
   * or this function applied to the success side.
   *
   * @return a function that returns the failure side of a given Validation,
   *         or this function applied to the success side.
   */
  public default Func<Validation<B, A>, B> onFail() {
    return new Func<Validation<B, A>, B>() {
      public B f(final Validation<B, A> v) {
        return v.f().on( Func.this);
      }
    };
  }

  /**
   * Returns a function that returns the success side of a given Validation,
   * or this function applied to the failure side.
   *
   * @return a function that returns the success side of a given Validation,
   *         or this function applied to the failure side.
   */
  public default Func<Validation<A, B>, B> onSuccess() {
    return new Func<Validation<A, B>, B>() {
      public B f(final Validation<A, B> v) {
        return v.on( Func.this);
      }
    };
  }

  /**
   * Promotes this function to return its value in a Zipper.
   *
   * @return This function promoted to return its value in a Zipper.
   */
  public default Func<A, Zipper<B>> zipperK() {
    return streamK().andThen(new Func<Stream<B>, Zipper<B>>() {
      public Zipper<B> f(final Stream<B> stream) {
        return fromStream(stream).some();
      }
    });
  }

  /**
   * Promotes this function to map over a Zipper.
   *
   * @return This function promoted to map over a Zipper.
   */
  public default Func<Zipper<A>, Zipper<B>> mapZipper() {
    return new Func<Zipper<A>, Zipper<B>>() {
      public Zipper<B> f(final Zipper<A> zipper) {
        return zipper.map( Func.this);
      }
    };
  }

  /**
   * Promotes this function to map over an Equal as a contravariant functor.
   *
   * @return This function promoted to map over an Equal as a contravariant functor.
   */
  public default Func<Equal<B>, Equal<A>> comapEqual() {
    return new Func<Equal<B>, Equal<A>>() {
      public Equal<A> f(final Equal<B> equal) {
        return equal.comap( Func.this);
      }
    };
  }

  /**
   * Promotes this function to map over a Hash as a contravariant functor.
   *
   * @return This function promoted to map over a Hash as a contravariant functor.
   */
  public default Func<Hash<B>, Hash<A>> comapHash() {
    return new Func<Hash<B>, Hash<A>>() {
      public Hash<A> f(final Hash<B> hash) {
        return hash.comap( Func.this);
      }
    };
  }

  /**
   * Promotes this function to map over a Show as a contravariant functor.
   *
   * @return This function promoted to map over a Show as a contravariant functor.
   */
  public default Func<Show<B>, Show<A>> comapShow() {
    return new Func<Show<B>, Show<A>>() {
      public Show<A> f(final Show<B> s) {
        return s.comap( Func.this);
      }
    };
  }

  /**
   * Promotes this function to map over the first element of a pair.
   *
   * @return This function promoted to map over the first element of a pair.
   */
  public default <C> Func<P2<A, C>, P2<B, C>> mapFst() {
    return P2.map1_( Func.this);
  }

  /**
   * Promotes this function to map over the second element of a pair.
   *
   * @return This function promoted to map over the second element of a pair.
   */
  public default <C> Func<P2<C, A>, P2<C, B>> mapSnd() {
    return P2.map2_( Func.this);
  }

  /**
   * Promotes this function to map over both elements of a pair.
   *
   * @return This function promoted to map over both elements of a pair.
   */
  public default Func<P2<A, A>, P2<B, B>> mapBoth() {
    return new Func<P2<A, A>, P2<B, B>>() {
      public P2<B, B> f(final P2<A, A> aap2) {
        return P2.map( Func.this, aap2);
      }
    };
  }

  /**
   * Maps this function over a SynchronousQueue.
   *
   * @param as A SynchronousQueue to map this function over.
   * @return A new SynchronousQueue with this function applied to each element.
   */
  public default SynchronousQueue<B> mapJ(final SynchronousQueue<A> as) {
    final SynchronousQueue<B> bs = new SynchronousQueue<B>();
    bs.addAll(iterableStream(as).map(this).toCollection());
    return bs;
  }


  /**
   * Maps this function over a PriorityBlockingQueue.
   *
   * @param as A PriorityBlockingQueue to map this function over.
   * @return A new PriorityBlockingQueue with this function applied to each element.
   */
  public default PriorityBlockingQueue<B> mapJ(final PriorityBlockingQueue<A> as) {
    return new PriorityBlockingQueue<B>(iterableStream(as).map(this).toCollection());
  }

  /**
   * Maps this function over a LinkedBlockingQueue.
   *
   * @param as A LinkedBlockingQueue to map this function over.
   * @return A new LinkedBlockingQueue with this function applied to each element.
   */
  public default LinkedBlockingQueue<B> mapJ(final LinkedBlockingQueue<A> as) {
    return new LinkedBlockingQueue<B>(iterableStream(as).map(this).toCollection());
  }

  /**
   * Maps this function over a CopyOnWriteArraySet.
   *
   * @param as A CopyOnWriteArraySet to map this function over.
   * @return A new CopyOnWriteArraySet with this function applied to each element.
   */
  public default CopyOnWriteArraySet<B> mapJ(final CopyOnWriteArraySet<A> as) {
    return new CopyOnWriteArraySet<B>(iterableStream(as).map(this).toCollection());
  }

  /**
   * Maps this function over a CopyOnWriteArrayList.
   *
   * @param as A CopyOnWriteArrayList to map this function over.
   * @return A new CopyOnWriteArrayList with this function applied to each element.
   */
  public default CopyOnWriteArrayList<B> mapJ(final CopyOnWriteArrayList<A> as) {
    return new CopyOnWriteArrayList<B>(iterableStream(as).map(this).toCollection());
  }

  /**
   * Maps this function over a ConcurrentLinkedQueue.
   *
   * @param as A ConcurrentLinkedQueue to map this function over.
   * @return A new ConcurrentLinkedQueue with this function applied to each element.
   */
  public default ConcurrentLinkedQueue<B> mapJ(final ConcurrentLinkedQueue<A> as) {
    return new ConcurrentLinkedQueue<B>(iterableStream(as).map(this).toCollection());
  }

  /**
   * Maps this function over an ArrayBlockingQueue.
   *
   * @param as An ArrayBlockingQueue to map this function over.
   * @return A new ArrayBlockingQueue with this function applied to each element.
   */
  public default ArrayBlockingQueue<B> mapJ(final ArrayBlockingQueue<A> as) {
    final ArrayBlockingQueue<B> bs = new ArrayBlockingQueue<B>(as.size());
    bs.addAll(iterableStream(as).map(this).toCollection());
    return bs;
  }


  /**
   * Maps this function over a TreeSet.
   *
   * @param as A TreeSet to map this function over.
   * @return A new TreeSet with this function applied to each element.
   */
  public default TreeSet<B> mapJ(final TreeSet<A> as) {
    return new TreeSet<B>(iterableStream(as).map(this).toCollection());
  }

  /**
   * Maps this function over a PriorityQueue.
   *
   * @param as A PriorityQueue to map this function over.
   * @return A new PriorityQueue with this function applied to each element.
   */
  public default PriorityQueue<B> mapJ(final PriorityQueue<A> as) {
    return new PriorityQueue<B>(iterableStream(as).map(this).toCollection());
  }

  /**
   * Maps this function over a LinkedList.
   *
   * @param as A LinkedList to map this function over.
   * @return A new LinkedList with this function applied to each element.
   */
  public default LinkedList<B> mapJ(final LinkedList<A> as) {
    return new LinkedList<B>(iterableStream(as).map(this).toCollection());
  }

  /**
   * Maps this function over an ArrayList.
   *
   * @param as An ArrayList to map this function over.
   * @return A new ArrayList with this function applied to each element.
   */
  public default ArrayList<B> mapJ(final ArrayList<A> as) {
    return new ArrayList<B>(iterableStream(as).map(this).toCollection());
  }
}
