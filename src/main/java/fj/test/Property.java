package fj.test;

import fj.*;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;

import static fj.Function.compose2;
import static fj.Function.curry;
import static fj.P.p;
import static fj.P2.__2;
import static fj.data.Option.none;
import static fj.test.Arg.arg;
import static fj.test.CheckResult.*;
import static fj.test.Result.noResult;
import static java.lang.Math.round;
/**
 * Represents an algebraic property about a program that may be {@link #check(fj.test.Rand, int, int, int,
 * int) checked} for its truth value. For example, it is true that "for all integers (call it x) and
 * for all integers (call it y), then x + y is equivalent to y + x". This statement is a (algebraic)
 * property, proposition or theorem that, when checked, will at least (depending on arguments) fail
 * to be falsified &mdash; since there does not exist a counter-example to this statement.
 *
 * @version %build.number%
 */
public final class Property {
  private final Func<Integer, Func<Rand, Result>> f;

  private Property(final Func<Integer, Func<Rand, Result>> f) {
    this.f = f;
  }

  /**
   * Returns the result of applying the given size and random generator.
   *
   * @param i The size to use to obtain a result.
   * @param r The random generator to use to obtain a result.
   * @return The result of applying the given size and random generator.
   */
  public Result prop(final int i, final Rand r) {
    return f.f(i).f(r);
  }

  /**
   * Returns a generator of results from this property.
   *
   * @return A generator of results from this property.
   */
  public Gen<Result> gen() {
    return Gen.gen(new Func<Integer, Func<Rand, Result>>() {
      public Func<Rand, Result> f(final Integer i) {
        return new Func<Rand, Result>() {
          public Result f(final Rand r) {
            return f.f(i).f(r);
          }
        };
      }
    });
  }

  /**
   * Performs a conjunction of this property with the given property.
   *
   * @param p The property to perform the conjunction with.
   * @return A conjunction of this property with the given property.
   */
  public Property and(final Property p) {
    return fromGen(gen().bind(p.gen(), new Func<Result, Func<Result, Result>>() {
      public Func<Result, Result> f(final Result res1) {
        return new Func<Result, Result>() {
          public Result f(final Result res2) {
            return res1.isException() || res1.isFalsified() ? res1 : res2.isException() || res2.isFalsified() ? res2 : res1.isProven() || res1.isUnfalsified() ? res2 : res2.isProven() || res2.isUnfalsified() ? res1 : noResult();
          }
        };
      }
    }));
  }

  /**
   * Performs a disjunction of this property with the given property.
   *
   * @param p The property to perform the disjunction with.
   * @return A disjunction of this property with the given property.
   */
  public Property or(final Property p) {
    return fromGen(gen().bind(p.gen(), new Func<Result, Func<Result, Result>>() {
      public Func<Result, Result> f(final Result res1) {
        return new Func<Result, Result>() {
          public Result f(final Result res2) {
            return res1.isException() || res1.isFalsified() ? res1 : res2.isException() || res2.isFalsified() ? res2 : res1.isProven() || res1.isUnfalsified() ? res1 : res2.isProven() || res2.isUnfalsified() ? res2 : noResult();
          }
        };
      }
    }));
  }

  /**
   * Performs a sequence of this property with the given property. The returned property holds if
   * and only if this property and the given property also hold. If one property does not hold, but
   * the other does, then the returned property will produce the same result and the property that
   * holds.
   *
   * @param p The property to sequence this property with.
   * @return A sequence of this property with the given property.
   */
  public Property sequence(final Property p) {
    return fromGen(gen().bind(p.gen(), new Func<Result, Func<Result, Result>>() {
      public Func<Result, Result> f(final Result res1) {
        return new Func<Result, Result>() {
          public Result f(final Result res2) {
            return res1.isException() || res1.isProven() || res1.isUnfalsified() ? res1 : res2.isException() || res2.isProven() || res2.isUnfalsified() ? res2 : res1.isFalsified() ? res2 : res2.isFalsified() ? res1 : noResult();
          }
        };
      }
    }));
  }

  /**
   * Checks this property using the given arguments and produces a result.
   *
   * @param r             The random generator to use for checking.
   * @param minSuccessful The minimum number of successful tests before a result is reached.
   * @param maxDiscarded  The maximum number of tests discarded because they did not satisfy
   *                      pre-conditions (i.e. {@link #implies(boolean, fj.P1)}).
   * @param minSize       The minimum size to use for checking.
   * @param maxSize       The maximum size to use for checking.
   * @return A result after checking this property.
   */
  @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
  public CheckResult check(final Rand r,
                           final int minSuccessful,
                           final int maxDiscarded,
                           final int minSize,
                           final int maxSize) {
    int s = 0;
    int d = 0;
    float sz = minSize;
    CheckResult res;

    while (true) {
      final float size = s == 0 && d == 0 ? minSize : sz + (maxSize - sz) / (minSuccessful - s);
      try {
        final Result x = f.f(round(size)).f(r);
        if (x.isNoResult())
          if (d + 1 >= maxDiscarded) {
            res = exhausted(s, d + 1);
            break;
          } else {
            sz = size;
            d++;
          }
        else if (x.isProven()) {
          res = proven(x.args().some(), s + 1, d);
          break;
        } else if (x.isUnfalsified())
          if (s + 1 >= minSuccessful) {
            res = passed(s + 1, d);
            break;
          } else {
            sz = size;
            s++;
          }
        else if (x.isFalsified()) {
          res = falsified(x.args().some(), s, d);
          break;
        } else if (x.isException()) {
          res = propException(x.args().some(), x.exception().some(), s, d);
          break;
        }
      } catch (final Throwable t) {
        genException(t, s, d);
      }
    }

    return res;
  }

  /**
   * Checks this property using a {@link fj.test.Rand#Rand(Func, Func) standard random generator} and the given
   * arguments to produce a result.
   *
   * @param minSuccessful The minimum number of successful tests before a result is reached.
   * @param maxDiscarded  The maximum number of tests discarded because they did not satisfy
   *                      pre-conditions (i.e. {@link #implies(boolean, fj.P1)}).
   * @param minSize       The minimum size to use for checking.
   * @param maxSize       The maximum size to use for checking.
   * @return A result after checking this property.
   */
  public CheckResult check(final int minSuccessful,
                           final int maxDiscarded,
                           final int minSize,
                           final int maxSize) {
    return check(Rand.standard, minSuccessful, maxDiscarded, minSize, maxSize);
  }

  /**
   * Checks this property using the given random generator, 100 minimum successful checks, 500
   * maximum discarded tests, minimum size of 0, maximum size of 100.
   *
   * @param r The random generator.
   * @return A result after checking this property.
   */
  public CheckResult check(final Rand r) {
    return check(r, 100, 500, 0, 100);
  }

  /**
   * Checks this property using the given random generator, 100 minimum successful checks, 500
   * maximum discarded tests, the given minimum size and the given maximum size.
   *
   * @param r       The random generator.
   * @param minSize The minimum size to use for checking.
   * @param maxSize The maximum size to use for checking.
   * @return A result after checking this property.
   */
  public CheckResult check(final Rand r, final int minSize, final int maxSize) {
    return check(r, 100, 500, minSize, maxSize);
  }

  /**
   * Checks this property using a {@link fj.test.Rand#Rand(Func, Func) standard random generator}, 100 minimum
   * successful checks, 500 maximum discarded tests and the given arguments to produce a result.
   *
   * @param minSize The minimum size to use for checking.
   * @param maxSize The maximum size to use for checking.
   * @return A result after checking this property.
   */
  public CheckResult check(final int minSize,
                           final int maxSize) {
    return check(100, 500, minSize, maxSize);
  }

  /**
   * Checks this property using a {@link fj.test.Rand#Rand(Func, Func) standard random generator}, 100 minimum
   * successful checks, 500 maximum discarded tests, minimum size of 0, maximum size of 100.
   *
   * @return A result after checking this property.
   */
  public CheckResult check() {
    return check(0, 100);
  }

  /**
   * Checks this property using a {@link fj.test.Rand#Rand(Func, Func) standard random generator}, the given minimum
   * successful checks, 500 maximum discarded tests, minimum size of 0, maximum size of 100.
   *
   * @param minSuccessful The minimum number of successful tests before a result is reached.
   * @return A result after checking this property.
   */
  public CheckResult minSuccessful(final int minSuccessful) {
    return check(minSuccessful, 500, 0, 100);
  }

  /**
   * Checks this property using the given random generator, the given minimum
   * successful checks, 500 maximum discarded tests, minimum size of 0, maximum size of 100.
   *
   * @param r             The random generator.
   * @param minSuccessful The minimum number of successful tests before a result is reached.
   * @return A result after checking this property.
   */
  public CheckResult minSuccessful(final Rand r, final int minSuccessful) {
    return check(r, minSuccessful, 500, 0, 100);
  }

  /**
   * Checks this property using a {@link fj.test.Rand#Rand(Func, Func) standard random generator}, 100 minimum
   * successful checks, the given maximum discarded tests, minimum size of 0, maximum size of 100.
   *
   * @param maxDiscarded The maximum number of tests discarded because they did not satisfy
   *                     pre-conditions (i.e. {@link #implies(boolean, fj.P1)}).
   * @return A result after checking this property.
   */
  public CheckResult maxDiscarded(final int maxDiscarded) {
    return check(100, maxDiscarded, 0, 100);
  }

  /**
   * Checks this property using a the given random generator}, 100 minimum
   * successful checks, the given maximum discarded tests, minimum size of 0, maximum size of 100.
   *
   * @param r            The random generator.
   * @param maxDiscarded The maximum number of tests discarded because they did not satisfy
   *                     pre-conditions (i.e. {@link #implies(boolean, fj.P1)}).
   * @return A result after checking this property.
   */
  public CheckResult maxDiscarded(final Rand r, final int maxDiscarded) {
    return check(r, 100, maxDiscarded, 0, 100);
  }

  /**
   * Checks this property using a {@link fj.test.Rand#Rand(Func, Func) standard random generator}, 100 minimum
   * successful checks, 500 maximum discarded tests, the given minimum size, maximum size of 100.
   *
   * @param minSize The minimum size to use for checking.
   * @return A result after checking this property.
   */
  public CheckResult minSize(final int minSize) {
    return check(100, 500, minSize, 100);
  }

  /**
   * Checks this property using the given random generator, 100 minimum
   * successful checks, 500 maximum discarded tests, the given minimum size, maximum size of 100.
   *
   * @param r       The random generator.
   * @param minSize The minimum size to use for checking.
   * @return A result after checking this property.
   */
  public CheckResult minSize(final Rand r, final int minSize) {
    return check(r, 100, 500, minSize, 100);
  }

  /**
   * Checks this property using a {@link fj.test.Rand#Rand(Func, Func) standard random generator}, 100 minimum
   * successful checks, 500 maximum discarded tests, minimum size of 0, the given maximum size.
   *
   * @param maxSize The maximum size to use for checking.
   * @return A result after checking this property.
   */
  public CheckResult maxSize(final int maxSize) {
    return check(100, 500, 0, maxSize);
  }

  /**
   * Checks this property using the given random generator, 100 minimum
   * successful checks, 500 maximum discarded tests, minimum size of 0, the given maximum size.
   *
   * @param r       The random generator.
   * @param maxSize The maximum size to use for checking.
   * @return A result after checking this property.
   */
  public CheckResult maxSize(final Rand r, final int maxSize) {
    return check(r, 100, 500, 0, maxSize);
  }

  /**
   * Returns a property that produces a result only if the given condition satisfies. The result
   * will be taken from the given property.
   *
   * @param b The condition that, if satisfied, produces the given property.
   * @param p The property to return if the condition satisfies.
   * @return A property that produces a result only if the given condition satisfies.
   */
  public static Property implies(final boolean b, final P1<Property> p) {
    return b ? p._1() : new Property(new Func<Integer, Func<Rand, Result>>() {
      public Func<Rand, Result> f(final Integer i) {
        return new Func<Rand, Result>() {
          public Result f(final Rand r) {
            return noResult();
          }
        };
      }
    });
  }

  /**
   * Returns a property from the given function.
   *
   * @param f The function to construct the returned property with.
   * @return A property from the given function.
   */
  public static Property prop(final Func<Integer, Func<Rand, Result>> f) {
    return new Property(f);
  }

  /**
   * Returns a property that always has the given result.
   *
   * @param r The result of the returned property.
   * @return A property that always has the given result.
   */
  public static Property prop(final Result r) {
    return new Property(new Func<Integer, Func<Rand, Result>>() {
      public Func<Rand, Result> f(final Integer integer) {
        return new Func<Rand, Result>() {
          public Result f(final Rand x) {
            return r;
          }
        };
      }
    });
  }

  /**
   * Returns a property that is either proven (the given condition satsifies) or falsified
   * otherwise.
   *
   * @param b The condition that, if satisfied, returns a property that is proven; otherwise, the
   *          property is falsified.
   * @return A property that is either proven (the given condition satsifies) or falsified
   *         otherwise.
   */
  public static Property prop(final boolean b) {
    return b ? prop(Result.proven(List.<Arg<?>>nil())) : prop(Result.falsified(List.<Arg<?>>nil()));
  }

  /**
   * Constructs a property from a generator of results.
   *
   * @param g The generator of results to constructor a property with.
   * @return A property from a generator of results.
   */
  public static Property fromGen(final Gen<Result> g) {
    return prop(new Func<Integer, Func<Rand, Result>>() {
      public Func<Rand, Result> f(final Integer i) {
        return new Func<Rand, Result>() {
          public Result f(final Rand r) {
            return g.gen(i, r);
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param g      The generator to produces values from to produce the property with.
   * @param shrink The shrink strategy to use upon falsification.
   * @param f      The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A> Property forall(final Gen<A> g, final Shrink<A> shrink, final Func<A, P1<Property>> f) {
    return prop(new Func<Integer, Func<Rand, Result>>() {
      public Func<Rand, Result> f(final Integer i) {
        return new Func<Rand, Result>() {
          public Result f(final Rand r) {
            final class Util {
              @SuppressWarnings({"IfMayBeConditional"})
              Option<P2<A, Result>> first(final Stream<A> as, final int shrinks) {
                final Stream<Option<P2<A, Result>>> results = as.map(new Func<A, Option<P2<A, Result>>>() {
                  public Option<P2<A, Result>> f(final A a) {
                    final Result result = exception(f.f(a)).prop(i, r);

                    return result.toOption().map(new Func<Result, P2<A, Result>>() {
                      public P2<A, Result> f(final Result result) {
                        return p(a, result.provenAsUnfalsified().addArg(arg(a, shrinks)));
                      }
                    });
                  }
                });

                if (results.isEmpty())
                  return none();
                else return results.find(new Func<Option<P2<A, Result>>, Boolean>() {
                  public Boolean f(final Option<P2<A, Result>> o) {
                    return failed(o);
                  }
                }).orSome(new P1<Option<P2<A, Result>>>() {
                  public Option<P2<A, Result>> _1() {
                    return results.head();
                  }
                });
              }

              public boolean failed(final Option<P2<A, Result>> o) {
                return o.isSome() && o.some()._2().failed();
              }
            }

            final Util u = new Util();

            Option<P2<A, Result>> x = u.first(Stream.single(g.gen(i, r)), 0);
            final Func<P2<A, Result>, Result> __2 = __2();
            if (u.failed(x)) {
              Option<Result> or;
              int shrinks = 0;

              do {
                shrinks++;
                or = x.map(__2);
                x = u.first(shrink.shrink(x.some()._1()), shrinks);
              }
              while (u.failed(x));

              return noResult(or);
            } else
              return noResult(x.map(__2));
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A> Property propertyP(final Arbitrary<A> aa, final Shrink<A> sa, final Func<A, P1<Property>> f) {
    return forall(aa.gen, sa, f);
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A> Property property(final Arbitrary<A> aa, final Shrink<A> sa, final Func<A, Property> f) {
    return propertyP(aa, sa, P1.curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A> Property propertyP(final Arbitrary<A> aa, final Func<A, P1<Property>> f) {
    return propertyP(aa, Shrink.<A>empty(), f);
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A> Property property(final Arbitrary<A> aa, final Func<A, Property> f) {
    return propertyP(aa, P1.curry(f));
  }


  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B> Property propertyP(final Arbitrary<A> aa, final Arbitrary<B> ab, final Shrink<A> sa, final Shrink<B> sb, final Func<A, Func<B, P1<Property>>> f) {
    return property(aa, sa, new Func<A, Property>() {
      public Property f(final A a) {
        return propertyP(ab, sb, new Func<B, P1<Property>>() {
          public P1<Property> f(final B b) {
            return f.f(a).f(b);
          }
        });
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B> Property property(final Arbitrary<A> aa, final Arbitrary<B> ab, final Shrink<A> sa, final Shrink<B> sb, final Func<A, Func<B, Property>> f) {
    return propertyP(aa, ab, sa, sb, compose2(P.<Property>p1(), f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B> Property propertyP(final Arbitrary<A> aa, final Arbitrary<B> ab, final Func<A, Func<B, P1<Property>>> f) {
    return property(aa, new Func<A, Property>() {
      public Property f(final A a) {
        return propertyP(ab, new Func<B, P1<Property>>() {
          public P1<Property> f(final B b) {
            return f.f(a).f(b);
          }
        });
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B> Property property(final Arbitrary<A> aa, final Arbitrary<B> ab, final Func<A, Func<B, Property>> f) {
    return propertyP(aa, ab, compose2(P.<Property>p1(), f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B> Property propertyP(final Arbitrary<A> aa, final Arbitrary<B> ab, final Shrink<A> sa, final Shrink<B> sb, final Func2<A, B, P1<Property>> f) {
    return propertyP(aa, ab, sa, sb, curry( f ));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B> Property property(final Arbitrary<A> aa, final Arbitrary<B> ab, final Shrink<A> sa, final Shrink<B> sb, final Func2<A, B, Property> f) {
    return propertyP(aa, ab, sa, sb, compose2(P.<Property>p1(), curry(f)));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B> Property propertyP(final Arbitrary<A> aa, final Arbitrary<B> ab, final Func2<A, B, P1<Property>> f) {
    return propertyP(aa, ab, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B> Property property(final Arbitrary<A> aa, final Arbitrary<B> ab, final Func2<A, B, Property> f) {
    return propertyP(aa, ab, compose2(P.<Property>p1(), curry(f)));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C> Property property(final Arbitrary<A> aa,
                                            final Arbitrary<B> ab,
                                            final Arbitrary<C> ac,
                                            final Shrink<A> sa,
                                            final Shrink<B> sb,
                                            final Shrink<C> sc,
                                            final Func<A, Func<B, Func<C, Property>>> f) {
    return property(aa, ab, sa, sb, new Func<A, Func<B, Property>>() {
      public Func<B, Property> f(final A a) {
        return new Func<B, Property>() {
          public Property f(final B b) {
            return property(ac, sc, new Func<C, Property>() {
              public Property f(final C c) {
                return f.f(a).f(b).f(c);
              }
            });
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C> Property property(final Arbitrary<A> aa,
                                            final Arbitrary<B> ab,
                                            final Arbitrary<C> ac,
                                            final Func<A, Func<B, Func<C, Property>>> f) {
    return property(aa, ab, new Func<A, Func<B, Property>>() {
      public Func<B, Property> f(final A a) {
        return new Func<B, Property>() {
          public Property f(final B b) {
            return property(ac, new Func<C, Property>() {
              public Property f(final C c) {
                return f.f(a).f(b).f(c);
              }
            });
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C> Property property(final Arbitrary<A> aa,
                                            final Arbitrary<B> ab,
                                            final Arbitrary<C> ac,
                                            final Shrink<A> sa,
                                            final Shrink<B> sb,
                                            final Shrink<C> sc,
                                            final Func3<A, B, C, Property> f) {
    return property(aa, ab, ac, sa, sb, sc, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C> Property property(final Arbitrary<A> aa,
                                            final Arbitrary<B> ab,
                                            final Arbitrary<C> ac,
                                            final Func3<A, B, C, Property> f) {
    return property(aa, ab, ac, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D> Property property(final Arbitrary<A> aa,
                                               final Arbitrary<B> ab,
                                               final Arbitrary<C> ac,
                                               final Arbitrary<D> ad,
                                               final Shrink<A> sa,
                                               final Shrink<B> sb,
                                               final Shrink<C> sc,
                                               final Shrink<D> sd,
                                               final Func<A, Func<B, Func<C, Func<D, Property>>>> f) {
    return property(aa, ab, ac, sa, sb, sc, new Func<A, Func<B, Func<C, Property>>>() {
      public Func<B, Func<C, Property>> f(final A a) {
        return new Func<B, Func<C, Property>>() {
          public Func<C, Property> f(final B b) {
            return new Func<C, Property>() {
              public Property f(final C c) {
                return property(ad, sd, new Func<D, Property>() {
                  public Property f(final D d) {
                    return f.f(a).f(b).f(c).f(d);
                  }
                });
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D> Property property(final Arbitrary<A> aa,
                                               final Arbitrary<B> ab,
                                               final Arbitrary<C> ac,
                                               final Arbitrary<D> ad,
                                               final Func<A, Func<B, Func<C, Func<D, Property>>>> f) {
    return property(aa, ab, ac, new Func<A, Func<B, Func<C, Property>>>() {
      public Func<B, Func<C, Property>> f(final A a) {
        return new Func<B, Func<C, Property>>() {
          public Func<C, Property> f(final B b) {
            return new Func<C, Property>() {
              public Property f(final C c) {
                return property(ad, new Func<D, Property>() {
                  public Property f(final D d) {
                    return f.f(a).f(b).f(c).f(d);
                  }
                });
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D> Property property(final Arbitrary<A> aa,
                                               final Arbitrary<B> ab,
                                               final Arbitrary<C> ac,
                                               final Arbitrary<D> ad,
                                               final Shrink<A> sa,
                                               final Shrink<B> sb,
                                               final Shrink<C> sc,
                                               final Shrink<D> sd,
                                               final Func4<A, B, C, D, Property> f) {
    return property(aa, ab, ac, ad, sa, sb, sc, sd, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D> Property property(final Arbitrary<A> aa,
                                               final Arbitrary<B> ab,
                                               final Arbitrary<C> ac,
                                               final Arbitrary<D> ad,
                                               final Func4<A, B, C, D, Property> f) {
    return property(aa, ab, ac, ad, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param se The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E> Property property(final Arbitrary<A> aa,
                                                  final Arbitrary<B> ab,
                                                  final Arbitrary<C> ac,
                                                  final Arbitrary<D> ad,
                                                  final Arbitrary<E> ae,
                                                  final Shrink<A> sa,
                                                  final Shrink<B> sb,
                                                  final Shrink<C> sc,
                                                  final Shrink<D> sd,
                                                  final Shrink<E> se,
                                                  final Func<A, Func<B, Func<C, Func<D, Func<E, Property>>>>> f) {
    return property(aa, ab, ac, ad, sa, sb, sc, sd, new Func<A, Func<B, Func<C, Func<D, Property>>>>() {
      public Func<B, Func<C, Func<D, Property>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Property>>>() {
          public Func<C, Func<D, Property>> f(final B b) {
            return new Func<C, Func<D, Property>>() {
              public Func<D, Property> f(final C c) {
                return new Func<D, Property>() {
                  public Property f(final D d) {
                    return property(ae, se, new Func<E, Property>() {
                      public Property f(final E e) {
                        return f.f(a).f(b).f(c).f(d).f(e);
                      }
                    });
                  }
                };
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E> Property property(final Arbitrary<A> aa,
                                                  final Arbitrary<B> ab,
                                                  final Arbitrary<C> ac,
                                                  final Arbitrary<D> ad,
                                                  final Arbitrary<E> ae,
                                                  final Func<A, Func<B, Func<C, Func<D, Func<E, Property>>>>> f) {
    return property(aa, ab, ac, ad, new Func<A, Func<B, Func<C, Func<D, Property>>>>() {
      public Func<B, Func<C, Func<D, Property>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Property>>>() {
          public Func<C, Func<D, Property>> f(final B b) {
            return new Func<C, Func<D, Property>>() {
              public Func<D, Property> f(final C c) {
                return new Func<D, Property>() {
                  public Property f(final D d) {
                    return property(ae, new Func<E, Property>() {
                      public Property f(final E e) {
                        return f.f(a).f(b).f(c).f(d).f(e);
                      }
                    });
                  }
                };
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param se The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E> Property property(final Arbitrary<A> aa,
                                                  final Arbitrary<B> ab,
                                                  final Arbitrary<C> ac,
                                                  final Arbitrary<D> ad,
                                                  final Arbitrary<E> ae,
                                                  final Shrink<A> sa,
                                                  final Shrink<B> sb,
                                                  final Shrink<C> sc,
                                                  final Shrink<D> sd,
                                                  final Shrink<E> se,
                                                  final Func5<A, B, C, D, E, Property> f) {
    return property(aa, ab, ac, ad, ae, sa, sb, sc, sd, se, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E> Property property(final Arbitrary<A> aa,
                                                  final Arbitrary<B> ab,
                                                  final Arbitrary<C> ac,
                                                  final Arbitrary<D> ad,
                                                  final Arbitrary<E> ae,
                                                  final Func5<A, B, C, D, E, Property> f) {
    return property(aa, ab, ac, ad, ae, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param se The shrink strategy to use upon falsification.
   * @param sf The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$> Property property(final Arbitrary<A> aa,
                                                      final Arbitrary<B> ab,
                                                      final Arbitrary<C> ac,
                                                      final Arbitrary<D> ad,
                                                      final Arbitrary<E> ae,
                                                      final Arbitrary<F$> af,
                                                      final Shrink<A> sa,
                                                      final Shrink<B> sb,
                                                      final Shrink<C> sc,
                                                      final Shrink<D> sd,
                                                      final Shrink<E> se,
                                                      final Shrink<F$> sf,
                                                      final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Property>>>>>> f) {
    return property(aa, ab, ac, ad, ae, sa, sb, sc, sd, se, new Func<A, Func<B, Func<C, Func<D, Func<E, Property>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Property>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Property>>>>() {
          public Func<C, Func<D, Func<E, Property>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Property>>>() {
              public Func<D, Func<E, Property>> f(final C c) {
                return new Func<D, Func<E, Property>>() {
                  public Func<E, Property> f(final D d) {
                    return new Func<E, Property>() {
                      public Property f(final E e) {
                        return property(af, sf, new Func<F$, Property>() {
                          public Property f(final F$ f$) {
                            return f.f(a).f(b).f(c).f(d).f(e).f(f$);
                          }
                        });
                      }
                    };
                  }
                };
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$> Property property(final Arbitrary<A> aa,
                                                      final Arbitrary<B> ab,
                                                      final Arbitrary<C> ac,
                                                      final Arbitrary<D> ad,
                                                      final Arbitrary<E> ae,
                                                      final Arbitrary<F$> af,
                                                      final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Property>>>>>> f) {
    return property(aa, ab, ac, ad, ae, new Func<A, Func<B, Func<C, Func<D, Func<E, Property>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Property>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Property>>>>() {
          public Func<C, Func<D, Func<E, Property>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Property>>>() {
              public Func<D, Func<E, Property>> f(final C c) {
                return new Func<D, Func<E, Property>>() {
                  public Func<E, Property> f(final D d) {
                    return new Func<E, Property>() {
                      public Property f(final E e) {
                        return property(af, new Func<F$, Property>() {
                          public Property f(final F$ f$) {
                            return f.f(a).f(b).f(c).f(d).f(e).f(f$);
                          }
                        });
                      }
                    };
                  }
                };
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param se The shrink strategy to use upon falsification.
   * @param sf The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$> Property property(final Arbitrary<A> aa,
                                                      final Arbitrary<B> ab,
                                                      final Arbitrary<C> ac,
                                                      final Arbitrary<D> ad,
                                                      final Arbitrary<E> ae,
                                                      final Arbitrary<F$> af,
                                                      final Shrink<A> sa,
                                                      final Shrink<B> sb,
                                                      final Shrink<C> sc,
                                                      final Shrink<D> sd,
                                                      final Shrink<E> se,
                                                      final Shrink<F$> sf,
                                                      final Func6<A, B, C, D, E, F$, Property> f) {
    return property(aa, ab, ac, ad, ae, af, sa, sb, sc, sd, se, sf, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$> Property property(final Arbitrary<A> aa,
                                                      final Arbitrary<B> ab,
                                                      final Arbitrary<C> ac,
                                                      final Arbitrary<D> ad,
                                                      final Arbitrary<E> ae,
                                                      final Arbitrary<F$> af,
                                                      final Func6<A, B, C, D, E, F$, Property> f) {
    return property(aa, ab, ac, ad, ae, af, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param ag The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param se The shrink strategy to use upon falsification.
   * @param sf The shrink strategy to use upon falsification.
   * @param sg The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$, G> Property property(final Arbitrary<A> aa,
                                                         final Arbitrary<B> ab,
                                                         final Arbitrary<C> ac,
                                                         final Arbitrary<D> ad,
                                                         final Arbitrary<E> ae,
                                                         final Arbitrary<F$> af,
                                                         final Arbitrary<G> ag,
                                                         final Shrink<A> sa,
                                                         final Shrink<B> sb,
                                                         final Shrink<C> sc,
                                                         final Shrink<D> sd,
                                                         final Shrink<E> se,
                                                         final Shrink<F$> sf,
                                                         final Shrink<G> sg,
                                                         final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>>> f) {
    return property(aa, ab, ac, ad, ae, af, sa, sb, sc, sd, se, sf, new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Property>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, Property>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, Property>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, Property>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, Property>>>>() {
              public Func<D, Func<E, Func<F$, Property>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, Property>>>() {
                  public Func<E, Func<F$, Property>> f(final D d) {
                    return new Func<E, Func<F$, Property>>() {
                      public Func<F$, Property> f(final E e) {
                        return new Func<F$, Property>() {
                          public Property f(final F$ f$) {
                            return property(ag, sg, new Func<G, Property>() {
                              public Property f(final G g) {
                                return f.f(a).f(b).f(c).f(d).f(e).f(f$).f(g);
                              }
                            });
                          }
                        };
                      }
                    };
                  }
                };
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param ag The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$, G> Property property(final Arbitrary<A> aa,
                                                         final Arbitrary<B> ab,
                                                         final Arbitrary<C> ac,
                                                         final Arbitrary<D> ad,
                                                         final Arbitrary<E> ae,
                                                         final Arbitrary<F$> af,
                                                         final Arbitrary<G> ag,
                                                         final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>>> f) {
    return property(aa, ab, ac, ad, ae, af, new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Property>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, Property>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, Property>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, Property>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, Property>>>>() {
              public Func<D, Func<E, Func<F$, Property>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, Property>>>() {
                  public Func<E, Func<F$, Property>> f(final D d) {
                    return new Func<E, Func<F$, Property>>() {
                      public Func<F$, Property> f(final E e) {
                        return new Func<F$, Property>() {
                          public Property f(final F$ f$) {
                            return property(ag, new Func<G, Property>() {
                              public Property f(final G g) {
                                return f.f(a).f(b).f(c).f(d).f(e).f(f$).f(g);
                              }
                            });
                          }
                        };
                      }
                    };
                  }
                };
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param ag The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param se The shrink strategy to use upon falsification.
   * @param sf The shrink strategy to use upon falsification.
   * @param sg The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$, G> Property property(final Arbitrary<A> aa,
                                                         final Arbitrary<B> ab,
                                                         final Arbitrary<C> ac,
                                                         final Arbitrary<D> ad,
                                                         final Arbitrary<E> ae,
                                                         final Arbitrary<F$> af,
                                                         final Arbitrary<G> ag,
                                                         final Shrink<A> sa,
                                                         final Shrink<B> sb,
                                                         final Shrink<C> sc,
                                                         final Shrink<D> sd,
                                                         final Shrink<E> se,
                                                         final Shrink<F$> sf,
                                                         final Shrink<G> sg,
                                                         final Func7<A, B, C, D, E, F$, G, Property> f) {
    return property(aa, ab, ac, ad, ae, af, ag, sa, sb, sc, sd, se, sf, sg, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param ag The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$, G> Property property(final Arbitrary<A> aa,
                                                         final Arbitrary<B> ab,
                                                         final Arbitrary<C> ac,
                                                         final Arbitrary<D> ad,
                                                         final Arbitrary<E> ae,
                                                         final Arbitrary<F$> af,
                                                         final Arbitrary<G> ag,
                                                         final Func7<A, B, C, D, E, F$, G, Property> f) {
    return property(aa, ab, ac, ad, ae, af, ag, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param ag The arbitrrary to produces values from to produce the property with.
   * @param ah The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param se The shrink strategy to use upon falsification.
   * @param sf The shrink strategy to use upon falsification.
   * @param sg The shrink strategy to use upon falsification.
   * @param sh The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$, G, H> Property property(final Arbitrary<A> aa,
                                                            final Arbitrary<B> ab,
                                                            final Arbitrary<C> ac,
                                                            final Arbitrary<D> ad,
                                                            final Arbitrary<E> ae,
                                                            final Arbitrary<F$> af,
                                                            final Arbitrary<G> ag,
                                                            final Arbitrary<H> ah,
                                                            final Shrink<A> sa,
                                                            final Shrink<B> sb,
                                                            final Shrink<C> sc,
                                                            final Shrink<D> sd,
                                                            final Shrink<E> se,
                                                            final Shrink<F$> sf,
                                                            final Shrink<G> sg,
                                                            final Shrink<H> sh,
                                                            final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, Property>>>>>>>> f) {
    return property(aa, ab, ac, ad, ae, af, ag, sa, sb, sc, sd, se, sf, sg, new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>() {
              public Func<D, Func<E, Func<F$, Func<G, Property>>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, Func<G, Property>>>>() {
                  public Func<E, Func<F$, Func<G, Property>>> f(final D d) {
                    return new Func<E, Func<F$, Func<G, Property>>>() {
                      public Func<F$, Func<G, Property>> f(final E e) {
                        return new Func<F$, Func<G, Property>>() {
                          public Func<G, Property> f(final F$ f$) {
                            return new Func<G, Property>() {
                              public Property f(final G g) {
                                return property(ah, sh, new Func<H, Property>() {
                                  public Property f(final H h) {
                                    return f.f(a).f(b).f(c).f(d).f(e).f(f$).f(g).f(h);
                                  }
                                });
                              }
                            };
                          }
                        };
                      }
                    };
                  }
                };
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param ag The arbitrrary to produces values from to produce the property with.
   * @param ah The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$, G, H> Property property(final Arbitrary<A> aa,
                                                            final Arbitrary<B> ab,
                                                            final Arbitrary<C> ac,
                                                            final Arbitrary<D> ad,
                                                            final Arbitrary<E> ae,
                                                            final Arbitrary<F$> af,
                                                            final Arbitrary<G> ag,
                                                            final Arbitrary<H> ah,
                                                            final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, Property>>>>>>>> f) {
    return property(aa, ab, ac, ad, ae, af, ag, new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, Func<G, Property>>>>>() {
              public Func<D, Func<E, Func<F$, Func<G, Property>>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, Func<G, Property>>>>() {
                  public Func<E, Func<F$, Func<G, Property>>> f(final D d) {
                    return new Func<E, Func<F$, Func<G, Property>>>() {
                      public Func<F$, Func<G, Property>> f(final E e) {
                        return new Func<F$, Func<G, Property>>() {
                          public Func<G, Property> f(final F$ f$) {
                            return new Func<G, Property>() {
                              public Property f(final G g) {
                                return property(ah, new Func<H, Property>() {
                                  public Property f(final H h) {
                                    return f.f(a).f(b).f(c).f(d).f(e).f(f$).f(g).f(h);
                                  }
                                });
                              }
                            };
                          }
                        };
                      }
                    };
                  }
                };
              }
            };
          }
        };
      }
    });
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param ag The arbitrrary to produces values from to produce the property with.
   * @param ah The arbitrrary to produces values from to produce the property with.
   * @param sa The shrink strategy to use upon falsification.
   * @param sb The shrink strategy to use upon falsification.
   * @param sc The shrink strategy to use upon falsification.
   * @param sd The shrink strategy to use upon falsification.
   * @param se The shrink strategy to use upon falsification.
   * @param sf The shrink strategy to use upon falsification.
   * @param sg The shrink strategy to use upon falsification.
   * @param sh The shrink strategy to use upon falsification.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$, G, H> Property property(final Arbitrary<A> aa,
                                                            final Arbitrary<B> ab,
                                                            final Arbitrary<C> ac,
                                                            final Arbitrary<D> ad,
                                                            final Arbitrary<E> ae,
                                                            final Arbitrary<F$> af,
                                                            final Arbitrary<G> ag,
                                                            final Arbitrary<H> ah,
                                                            final Shrink<A> sa,
                                                            final Shrink<B> sb,
                                                            final Shrink<C> sc,
                                                            final Shrink<D> sd,
                                                            final Shrink<E> se,
                                                            final Shrink<F$> sf,
                                                            final Shrink<G> sg,
                                                            final Shrink<H> sh,
                                                            final Func8<A, B, C, D, E, F$, G, H, Property> f) {
    return property(aa, ab, ac, ad, ae, af, ag, ah, sa, sb, sc, sd, se, sf, sg, sh, curry(f));
  }

  /**
   * Returns a property where its result is derived from universal quantification across the
   * application of its arguments. No shrinking occurs upon falsification.
   *
   * @param aa The arbitrrary to produces values from to produce the property with.
   * @param ab The arbitrrary to produces values from to produce the property with.
   * @param ac The arbitrrary to produces values from to produce the property with.
   * @param ad The arbitrrary to produces values from to produce the property with.
   * @param ae The arbitrrary to produces values from to produce the property with.
   * @param af The arbitrrary to produces values from to produce the property with.
   * @param ag The arbitrrary to produces values from to produce the property with.
   * @param ah The arbitrrary to produces values from to produce the property with.
   * @param f  The function to produce properties with results.
   * @return A property where its result is derived from universal quantification across the
   *         application of its arguments.
   */
  public static <A, B, C, D, E, F$, G, H> Property property(final Arbitrary<A> aa,
                                                            final Arbitrary<B> ab,
                                                            final Arbitrary<C> ac,
                                                            final Arbitrary<D> ad,
                                                            final Arbitrary<E> ae,
                                                            final Arbitrary<F$> af,
                                                            final Arbitrary<G> ag,
                                                            final Arbitrary<H> ah,
                                                            final Func8<A, B, C, D, E, F$, G, H, Property> f) {
    return property(aa, ab, ac, ad, ae, af, ag, ah, curry(f));
  }

  /**
   * Returns a property that has a result of exception, if the evaluation of the given property
   * throws an exception; otherwise, the given property is returned.
   *
   * @param p A property to evaluate to check for an exception.
   * @return A property that has a result of exception, if the evaluation of the given property
   *         throws an exception; otherwise, the given property is returned.
   */
  public static Property exception(final P1<Property> p) {
    try {
      return p._1();
    } catch (final Throwable t) {
      return new Property(new Func<Integer, Func<Rand, Result>>() {
        public Func<Rand, Result> f(final Integer i) {
          return new Func<Rand, Result>() {
            public Result f(final Rand r) {
              return Result.exception(List.<Arg<?>>nil(), t);
            }
          };
        }
      });
    }
  }
}
