package fj;

import fj.data.Option;

/**
 * Transformations on functions.
 *
 * @version %build.number%
 */
public final class Function {
  private Function() {
    throw new UnsupportedOperationException();
  }

  /**
   * Function application with the arguments flipped.
   *
   * @param a The value to apply the function to.
   * @return A function that is partially-applied to the given value.
   */
  public static <A, B> Func<Func<A, B>, B> apply(final A a) {
    return new Func<Func<A, B>, B>() {
      public B f(final Func<A, B> k) {
        return k.f(a);
      }
    };
  }

  /**
   * Function composition.
   *
   * @return A function that composes two functions to produce a new function.
   */
  public static <A, B, C> Func<Func<B, C>, Func<Func<A, B>, Func<A, C>>> compose() {
    return new Func<Func<B, C>, Func<Func<A, B>, Func<A, C>>>() {
      public Func<Func<A, B>, Func<A, C>> f(final Func<B, C> f) {
        return new Func<Func<A, B>, Func<A, C>>() {
          public Func<A, C> f(final Func<A, B> g) {
            return compose(f, g);
          }
        };
      }
    };
  }

  /**
   * Function composition.
   *
   * @param f A function to compose with another.
   * @param g A function to compose with another.
   * @return A function that is the composition of the given arguments.
   */
  public static <A, B, C> Func<A, C> compose(final Func<B, C> f, final Func<A, B> g) {
    return new Func<A, C>() {
      public C f(final A a) {
        return f.f(g.f(a));
      }
    };
  }

  /**
   * Function composition.
   *
   * @param f A function to compose with another.
   * @param g A function to compose with another.
   * @return A function that is the composition of the given arguments.
   */
  public static <A, B, C, D> Func<A, Func<B, D>> compose2(final Func<C, D> f, final Func<A, Func<B, C>> g) {
    return new Func<A, Func<B, D>>() {
      public Func<B, D> f(final A a) {
        return new Func<B, D>() {
          public D f(final B b) {
            return f.f(g.f(a).f(b));
          }
        };
      }
    };
  }


  /**
   * Function composition flipped.
   *
   * @return A function that composes two functions to produce a new function.
   */
  public static <A, B, C> Func<Func<A, B>, Func<Func<B, C>, Func<A, C>>> andThen() {
    return new Func<Func<A, B>, Func<Func<B, C>, Func<A, C>>>() {
      public Func<Func<B, C>, Func<A, C>> f(final Func<A, B> g) {
        return new Func<Func<B, C>, Func<A, C>>() {
          public Func<A, C> f(final Func<B, C> f) {
            return Function.andThen(g, f);
          }
        };
      }
    };
  }

  /**
   * Function composition flipped.
   *
   * @param g A function to compose with another.
   * @param f A function to compose with another.
   * @return A function that is the composition of the given arguments.
   */
  public static <A, B, C> Func<A, C> andThen(final Func<A, B> g, final Func<B, C> f) {
    return new Func<A, C>() {
      public C f(final A a) {
        return f.f(g.f(a));
      }
    };
  }

  /**
   * The identity transformation.
   *
   * @return The identity transformation.
   */
  public static <A> Func<A, A> identity() {
    return new Func<A, A>() {
      public A f(final A a) {
        return a;
      }
    };
  }

  /**
   * Returns a function that given an argument, returns a function that ignores its argument.
   *
   * @return A function that given an argument, returns a function that ignores its argument.
   */
  public static <A, B> Func<B, Func<A, B>> constant() {
    return new Func<B, Func<A, B>>() {
      public Func<A, B> f(final B b) {
        return constant(b);
      }
    };
  }

  /**
   * Returns a function that ignores its argument to constantly produce the given value.
   *
   * @param b The value to return when the returned function is applied.
   * @return A function that ignores its argument to constantly produce the given value.
   */
  public static <A, B> Func<A, B> constant(final B b) {
    return new Func<A, B>() {
      public B f(final A a) {
        return b;
      }
    };
  }

  /**
   * Simultaneously covaries and contravaries a function.
   *
   * @param f The function to vary.
   * @return A co- and contravariant function that invokes f on its argument.
   */
  public static <A, B> Func<A, B> vary(final Func<? super A, ? extends B> f) {
    return new Func<A, B>() {
      public B f(final A a) {
        return f.f(a);
      }
    };
  }

  /**
   * Simultaneously covaries and contravaries a function.
   *
   * @return A function that varies and covaries a function.
   */
  public static <C, A extends C, B, D extends B> Func<Func<C, D>, Func<A, B>> vary() {
    return new Func<Func<C, D>, Func<A, B>>() {
      public Func<A, B> f(final Func<C, D> f) {
        return Function.<A, B>vary(f);
      }
    };
  }

  /**
   * Function argument flipping.
   *
   * @return A function that takes a function and flips its arguments.
   */
  public static <A, B, C> Func<Func<A, Func<B, C>>, Func<B, Func<A, C>>> flip() {
    return new Func<Func<A, Func<B, C>>, Func<B, Func<A, C>>>() {
      public Func<B, Func<A, C>> f(final Func<A, Func<B, C>> f) {
        return flip(f);
      }
    };
  }

  /**
   * Function argument flipping.
   *
   * @param f The function to flip.
   * @return The given function flipped.
   */
  public static <A, B, C> Func<B, Func<A, C>> flip(final Func<A, Func<B, C>> f) {
    return new Func<B, Func<A, C>>() {
      public Func<A, C> f(final B b) {
        return new Func<A, C>() {
          public C f(final A a) {
            return f.f(a).f(b);
          }
        };
      }
    };
  }

  /**
   * Function argument flipping.
   *
   * @param f The function to flip.
   * @return The given function flipped.
   */
  public static <A, B, C> Func2<B, A, C> flip(final Func2<A, B, C> f) {
    return new Func2<B, A, C>() {
      public C f(final B b, final A a) {
        return f.f(a, b);
      }
    };
  }

  /**
   * Function argument flipping.
   *
   * @return A function that flips the arguments of a given function.
   */
  public static <A, B, C> Func<Func2<A, B, C>, Func2<B, A, C>> flip2() {
    return new Func<Func2<A, B, C>, Func2<B, A, C>>() {
      public Func2<B, A, C> f(final Func2<A, B, C> f) {
        return flip(f);
      }
    };
  }

  /**
   * Return a function that inspects the argument of the given function for a <code>null</code> value and if so, does
   * not apply the value, instead returning an empty optional value.
   *
   * @param f The function to check for a <code>null</code> argument.
   * @return A function that inspects the argument of the given function for a <code>null</code> value and if so, does
   * not apply the value, instead returning an empty optional value.
   */
  public static <A, B> Func<A, Option<B>> nullable(final Func<A, B> f) {
    return new Func<A, Option<B>>() {
      public Option<B> f(final A a) {
        return a == null ? Option.<B>none() : Option.some(f.f(a));
      }
    };
  }

  /**
   * Curry a function of arity-2.
   *
   * @param f The function to curry.
   * @return A curried form of the given function.
   */
  public static <A, B, C> Func<A, Func<B, C>> curry(final Func2<A, B, C> f) {
    return new Func<A, Func<B, C>>() {
      public Func<B, C> f(final A a) {
        return new Func<B, C>() {
          public C f(final B b) {
            return f.f(a, b);
          }
        };
      }
    };
  }

  /**
   * Curry a function of arity-2.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C> Func<B, C> curry(final Func2<A, B, C> f, final A a) {
    return curry(f).f(a);
  }

  /**
   * Uncurry a function of arity-2.
   *
   * @return An uncurried function.
   */
  public static <A, B, C> Func<Func<A, Func<B, C>>, Func2<A, B, C>> uncurryF2() {
    return new Func<Func<A, Func<B, C>>, Func2<A, B, C>>() {
      public Func2<A, B, C> f(final Func<A, Func<B, C>> f) {
        return uncurryF2(f);
      }
    };
  }

  /**
   * Uncurry a function of arity-2.
   *
   * @param f The function to uncurry.
   * @return An uncurried function.
   */
  public static <A, B, C> Func2<A, B, C> uncurryF2(final Func<A, Func<B, C>> f) {
    return new Func2<A, B, C>() {
      public C f(final A a, final B b) {
        return f.f(a).f(b);
      }
    };
  }

  /**
   * Curry a function of arity-3.
   *
   * @param f The function to curry.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D> Func<A, Func<B, Func<C, D>>> curry(final Func3<A, B, C, D> f) {
    return new Func<A, Func<B, Func<C, D>>>() {
      public Func<B, Func<C, D>> f(final A a) {
        return new Func<B, Func<C, D>>() {
          public Func<C, D> f(final B b) {
            return new Func<C, D>() {
              public D f(final C c) {
                return f.f(a, b, c);
              }
            };
          }
        };
      }
    };
  }

  /**
   * Curry a function of arity-3.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D> Func<B, Func<C, D>> curry(final Func3<A, B, C, D> f, final A a) {
    return curry(f).f(a);
  }

  /**
   * Curry a function of arity-3.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D> Func<C, D> curry(final Func3<A, B, C, D> f, final A a, final B b) {
    return curry(f, a).f(b);
  }

  /**
   * Uncurry a function of arity-3.
   *
   * @return An uncurried function.
   */
  public static <A, B, C, D> Func<Func<A, Func<B, Func<C, D>>>, Func3<A, B, C, D>> uncurryF3() {
    return new Func<Func<A, Func<B, Func<C, D>>>, Func3<A, B, C, D>>() {
      public Func3<A, B, C, D> f(final Func<A, Func<B, Func<C, D>>> f) {
        return uncurryF3(f);
      }
    };
  }

  /**
   * Uncurry a function of arity-3.
   *
   * @param f The function to uncurry.
   * @return An uncurried function.
   */
  public static <A, B, C, D> Func3<A, B, C, D> uncurryF3(final Func<A, Func<B, Func<C, D>>> f) {
    return new Func3<A, B, C, D>() {
      public D f(final A a, final B b, final C c) {
        return f.f(a).f(b).f(c);
      }
    };
  }

  /**
   * Curry a function of arity-4.
   *
   * @param f The function to curry.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E> Func<A, Func<B, Func<C, Func<D, E>>>> curry(final Func4<A, B, C, D, E> f) {
    return new Func<A, Func<B, Func<C, Func<D, E>>>>() {
      public Func<B, Func<C, Func<D, E>>> f(final A a) {
        return new Func<B, Func<C, Func<D, E>>>() {
          public Func<C, Func<D, E>> f(final B b) {
            return new Func<C, Func<D, E>>() {
              public Func<D, E> f(final C c) {
                return new Func<D, E>() {
                  public E f(final D d) {
                    return f.f(a, b, c, d);
                  }
                };
              }
            };
          }
        };
      }
    };
  }

  /**
   * Curry a function of arity-4.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E> Func<B, Func<C, Func<D, E>>> curry(final Func4<A, B, C, D, E> f, final A a) {
    return curry(f).f(a);
  }

  /**
   * Curry a function of arity-4.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E> Func<C, Func<D, E>> curry(final Func4<A, B, C, D, E> f, final A a, final B b) {
    return curry(f).f(a).f(b);
  }

  /**
   * Curry a function of arity-4.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E> Func<D, E> curry(final Func4<A, B, C, D, E> f, final A a, final B b, final C c) {
    return curry(f).f(a).f(b).f(c);
  }

  /**
   * Uncurry a function of arity-4.
   *
   * @return An uncurried function.
   */
  public static <A, B, C, D, E> Func<Func<A, Func<B, Func<C, Func<D, E>>>>, Func4<A, B, C, D, E>> uncurryF4() {
    return new Func<Func<A, Func<B, Func<C, Func<D, E>>>>, Func4<A, B, C, D, E>>() {
      public Func4<A, B, C, D, E> f(final Func<A, Func<B, Func<C, Func<D, E>>>> f) {
        return uncurryF4(f);
      }
    };
  }

  /**
   * Uncurry a function of arity-4.
   *
   * @param f The function to uncurry.
   * @return An uncurried function.
   */
  public static <A, B, C, D, E> Func4<A, B, C, D, E> uncurryF4(final Func<A, Func<B, Func<C, Func<D, E>>>> f) {
    return new Func4<A, B, C, D, E>() {
      public E f(final A a, final B b, final C c, final D d) {
        return f.f(a).f(b).f(c).f(d);
      }
    };
  }

  /**
   * Curry a function of arity-5.
   *
   * @param f The function to curry.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$> Func<A, Func<B, Func<C, Func<D, Func<E, F$>>>>> curry(final Func5<A, B, C, D, E, F$> f) {
    return new Func<A, Func<B, Func<C, Func<D, Func<E, F$>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, F$>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, F$>>>>() {
          public Func<C, Func<D, Func<E, F$>>> f(final B b) {
            return new Func<C, Func<D, Func<E, F$>>>() {
              public Func<D, Func<E, F$>> f(final C c) {
                return new Func<D, Func<E, F$>>() {
                  public Func<E, F$> f(final D d) {
                    return new Func<E, F$>() {
                      public F$ f(final E e) {
                        return f.f(a, b, c, d, e);
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

  /**
   * Curry a function of arity-5.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$> Func<B, Func<C, Func<D, Func<E, F$>>>> curry(final Func5<A, B, C, D, E, F$> f, final A a) {
    return curry(f).f(a);
  }

  /**
   * Curry a function of arity-5.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$> Func<C, Func<D, Func<E, F$>>> curry(final Func5<A, B, C, D, E, F$> f, final A a, final B b) {
    return curry(f).f(a).f(b);
  }

  /**
   * Curry a function of arity-5.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$> Func<D, Func<E, F$>> curry(final Func5<A, B, C, D, E, F$> f, final A a, final B b,
                                                         final C c) {
    return curry(f).f(a).f(b).f(c);
  }

  /**
   * Curry a function of arity-5.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @param d An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$> Func<E, F$> curry(final Func5<A, B, C, D, E, F$> f, final A a, final B b, final C c,
                                                   final D d) {
    return curry(f).f(a).f(b).f(c).f(d);
  }

  /**
   * Uncurry a function of arity-5.
   *
   * @return An uncurried function.
   */
  public static <A, B, C, D, E, F$> Func<Func<A, Func<B, Func<C, Func<D, Func<E, F$>>>>>, Func5<A, B, C, D, E, F$>> uncurryF5() {
    return new Func<Func<A, Func<B, Func<C, Func<D, Func<E, F$>>>>>, Func5<A, B, C, D, E, F$>>() {
      public Func5<A, B, C, D, E, F$> f(final Func<A, Func<B, Func<C, Func<D, Func<E, F$>>>>> f) {
        return uncurryF5(f);
      }
    };
  }

  /**
   * Uncurry a function of arity-6.
   *
   * @param f The function to uncurry.
   * @return An uncurried function.
   */
  public static <A, B, C, D, E, F$> Func5<A, B, C, D, E, F$> uncurryF5(final Func<A, Func<B, Func<C, Func<D, Func<E, F$>>>>> f) {
    return new Func5<A, B, C, D, E, F$>() {
      public F$ f(final A a, final B b, final C c, final D d, final E e) {
        return f.f(a).f(b).f(c).f(d).f(e);
      }
    };
  }

  /**
   * Curry a function of arity-6.
   *
   * @param f The function to curry.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G> Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>>> curry(final Func6<A, B, C, D, E, F$, G> f) {
    return new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, G>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, G>>>>() {
              public Func<D, Func<E, Func<F$, G>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, G>>>() {
                  public Func<E, Func<F$, G>> f(final D d) {
                    return new Func<E, Func<F$, G>>() {
                      public Func<F$, G> f(final E e) {
                        return new Func<F$, G>() {
                          public G f(final F$ f$) {
                            return f.f(a, b, c, d, e, f$);
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

  /**
   * Uncurry a function of arity-6.
   *
   * @return An uncurried function.
   */
  public static <A, B, C, D, E, F$, G> Func<Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>>>, Func6<A, B, C, D, E, F$, G>> uncurryF6() {
    return new Func<Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>>>, Func6<A, B, C, D, E, F$, G>>() {
      public Func6<A, B, C, D, E, F$, G> f(final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>>> f) {
        return uncurryF6(f);
      }
    };
  }

  /**
   * Uncurry a function of arity-6.
   *
   * @param f The function to uncurry.
   * @return An uncurried function.
   */
  public static <A, B, C, D, E, F$, G> Func6<A, B, C, D, E, F$, G> uncurryF6(
      final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>>> f) {
    return new Func6<A, B, C, D, E, F$, G>() {
      public G f(final A a, final B b, final C c, final D d, final E e, final F$ f$) {
        return f.f(a).f(b).f(c).f(d).f(e).f(f$);
      }
    };
  }

  /**
   * Curry a function of arity-7.
   *
   * @param f The function to curry.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H> Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>>> curry(
      final Func7<A, B, C, D, E, F$, G, H> f) {
    return new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>() {
              public Func<D, Func<E, Func<F$, Func<G, H>>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, Func<G, H>>>>() {
                  public Func<E, Func<F$, Func<G, H>>> f(final D d) {
                    return new Func<E, Func<F$, Func<G, H>>>() {
                      public Func<F$, Func<G, H>> f(final E e) {
                        return new Func<F$, Func<G, H>>() {
                          public Func<G, H> f(final F$ f$) {
                            return new Func<G, H>() {
                              public H f(final G g) {
                                return f.f(a, b, c, d, e, f$, g);
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
    };
  }

  /**
   * Curry a function of arity-7.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H> Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>> curry(
      final Func7<A, B, C, D, E, F$, G, H> f, final A a) {
    return curry(f).f(a);
  }

  /**
   * Curry a function of arity-7.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H> Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>> curry(final Func7<A, B, C, D, E, F$, G, H> f,
                                                                                 final A a, final B b) {
    return curry(f).f(a).f(b);
  }

  /**
   * Curry a function of arity-7.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H> Func<D, Func<E, Func<F$, Func<G, H>>>> curry(final Func7<A, B, C, D, E, F$, G, H> f,
                                                                           final A a, final B b, final C c) {
    return curry(f).f(a).f(b).f(c);
  }

  /**
   * Curry a function of arity-7.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @param d An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H> Func<E, Func<F$, Func<G, H>>> curry(final Func7<A, B, C, D, E, F$, G, H> f, final A a,
                                                                     final B b, final C c, final D d) {
    return curry(f).f(a).f(b).f(c).f(d);
  }

  /**
   * Curry a function of arity-7.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @param d An argument to the curried function.
   * @param e An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H> Func<F$, Func<G, H>> curry(final Func7<A, B, C, D, E, F$, G, H> f, final A a,
                                                               final B b, final C c, final D d, final E e) {
    return curry(f).f(a).f(b).f(c).f(d).f(e);
  }

  /**
   * Curry a function of arity-7.
   *
   * @param f  The function to curry.
   * @param a  An argument to the curried function.
   * @param b  An argument to the curried function.
   * @param c  An argument to the curried function.
   * @param d  An argument to the curried function.
   * @param e  An argument to the curried function.
   * @param f$ An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H> Func<G, H> curry(final Func7<A, B, C, D, E, F$, G, H> f, final A a, final B b,
                                                        final C c, final D d, final E e, final F$ f$) {
    return curry(f).f(a).f(b).f(c).f(d).f(e).f(f$);
  }

  /**
   * Uncurry a function of arity-7.
   *
   * @return An uncurried function.
   */
  public static <A, B, C, D, E, F$, G, H> Func<Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>>>, Func7<A, B, C, D, E, F$, G, H>> uncurryF7() {
    return new Func<Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>>>, Func7<A, B, C, D, E, F$, G, H>>() {
      public Func7<A, B, C, D, E, F$, G, H> f(final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>>> f) {
        return uncurryF7(f);
      }
    };
  }

  /**
   * Uncurry a function of arity-7.
   *
   * @param f The function to uncurry.
   * @return An uncurried function.
   */
  public static <A, B, C, D, E, F$, G, H> Func7<A, B, C, D, E, F$, G, H> uncurryF7(
      final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>>> f) {
    return new Func7<A, B, C, D, E, F$, G, H>() {
      public H f(final A a, final B b, final C c, final D d, final E e, final F$ f$, final G g) {
        return f.f(a).f(b).f(c).f(d).f(e).f(f$).f(g);
      }
    };
  }

  /**
   * Curry a function of arity-8.
   *
   * @param f The function to curry.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>>> curry(
      final Func8<A, B, C, D, E, F$, G, H, I> f) {
    return new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>() {
              public Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>() {
                  public Func<E, Func<F$, Func<G, Func<H, I>>>> f(final D d) {
                    return new Func<E, Func<F$, Func<G, Func<H, I>>>>() {
                      public Func<F$, Func<G, Func<H, I>>> f(final E e) {
                        return new Func<F$, Func<G, Func<H, I>>>() {
                          public Func<G, Func<H, I>> f(final F$ f$) {
                            return new Func<G, Func<H, I>>() {
                              public Func<H, I> f(final G g) {
                                return new Func<H, I>() {
                                  public I f(final H h) {
                                    return f.f(a, b, c, d, e, f$, g, h);
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
        };
      }
    };
  }

  /**
   * Curry a function of arity-8.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>> curry(
      final Func8<A, B, C, D, E, F$, G, H, I> f, final A a) {
    return curry(f).f(a);
  }

  /**
   * Curry a function of arity-8.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>> curry(
      final Func8<A, B, C, D, E, F$, G, H, I> f, final A a, final B b) {
    return curry(f).f(a).f(b);
  }

  /**
   * Curry a function of arity-8.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>> curry(
      final Func8<A, B, C, D, E, F$, G, H, I> f, final A a, final B b, final C c) {
    return curry(f).f(a).f(b).f(c);
  }

  /**
   * Curry a function of arity-8.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @param d An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<E, Func<F$, Func<G, Func<H, I>>>> curry(final Func8<A, B, C, D, E, F$, G, H, I> f,
                                                                              final A a, final B b, final C c,
                                                                              final D d) {
    return curry(f).f(a).f(b).f(c).f(d);
  }

  /**
   * Curry a function of arity-8.
   *
   * @param f The function to curry.
   * @param a An argument to the curried function.
   * @param b An argument to the curried function.
   * @param c An argument to the curried function.
   * @param d An argument to the curried function.
   * @param e An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<F$, Func<G, Func<H, I>>> curry(final Func8<A, B, C, D, E, F$, G, H, I> f,
                                                                        final A a, final B b, final C c, final D d,
                                                                        final E e) {
    return curry(f).f(a).f(b).f(c).f(d).f(e);
  }

  /**
   * Curry a function of arity-8.
   *
   * @param f  The function to curry.
   * @param a  An argument to the curried function.
   * @param b  An argument to the curried function.
   * @param c  An argument to the curried function.
   * @param d  An argument to the curried function.
   * @param e  An argument to the curried function.
   * @param f$ An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<G, Func<H, I>> curry(final Func8<A, B, C, D, E, F$, G, H, I> f, final A a,
                                                                 final B b, final C c, final D d, final E e,
                                                                 final F$ f$) {
    return curry(f).f(a).f(b).f(c).f(d).f(e).f(f$);
  }

  /**
   * Curry a function of arity-7.
   *
   * @param f  The function to curry.
   * @param a  An argument to the curried function.
   * @param b  An argument to the curried function.
   * @param c  An argument to the curried function.
   * @param d  An argument to the curried function.
   * @param e  An argument to the curried function.
   * @param f$ An argument to the curried function.
   * @param g  An argument to the curried function.
   * @return A curried form of the given function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<H, I> curry(final Func8<A, B, C, D, E, F$, G, H, I> f, final A a, final B b,
                                                           final C c, final D d, final E e, final F$ f$, final G g) {
    return curry(f).f(a).f(b).f(c).f(d).f(e).f(f$).f(g);
  }

  /**
   * Uncurry a function of arity-8.
   *
   * @return An uncurried function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>>>, Func8<A, B, C, D, E, F$, G, H, I>> uncurryF8() {
    return new Func<Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>>>, Func8<A, B, C, D, E, F$, G, H, I>>() {
      public Func8<A, B, C, D, E, F$, G, H, I> f(final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>>> f) {
        return uncurryF8(f);
      }
    };
  }

  /**
   * Uncurry a function of arity-8.
   *
   * @param f The function to uncurry.
   * @return An uncurried function.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func8<A, B, C, D, E, F$, G, H, I> uncurryF8(
      final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>>> f) {
    return new Func8<A, B, C, D, E, F$, G, H, I>() {
      public I f(final A a, final B b, final C c, final D d, final E e, final F$ f$, final G g, final H h) {
        return f.f(a).f(b).f(c).f(d).f(e).f(f$).f(g).f(h);
      }
    };
  }

  /**
   * Binds the function in the second argument to the function in the first argument.
   *
   * @param ma A function whose argument type is the same as the argument type of the return value.
   * @param f  A function whose argument type is the same as the return type of <em>ma</em>,
   *           and yields the return value.
   * @return A function that chains the given functions together such that the result of applying
   *         <em>ma</em> to the argument is given to <i>f</i>, yielding a function
   *         that is applied to the argument again.
   */
  public static <A, B, C> Func<C, B> bind(final Func<C, A> ma, final Func<A, Func<C, B>> f) {
    return new Func<C, B>() {
      public B f(final C m) {
        return f.f(ma.f(m)).f(m);
      }
    };
  }

  /**
   * Performs function application within a higher-order function (applicative functor pattern).
   *
   * @param cab The higher-order function to apply a function to.
   * @param ca  A function to apply within a higher-order function.
   * @return A new function after applying the given higher-order function to the given function.
   */
  public static <A, B, C> Func<C, B> apply(final Func<C, Func<A, B>> cab, final Func<C, A> ca) {
    return bind(cab, new Func<Func<A, B>, Func<C, B>>() {
      public Func<C, B> f(final Func<A, B> f) {
        return compose(new Func<A, B>() {
          public B f(final A a) {
            return f.f(a);
          }
        }, ca);
      }
    });
  }

  /**
   * Binds the given function <em>f</em> to the values of the given functions, with a final join.
   *
   * @param ca A function to bind <em>f</em> function to.
   * @param cb A function to bind <em>f</em> function to.
   * @param f  The bound function to be composed with <em>ca</em> and then applied with <em>cb</em>
   * @return A new function after performing the composition, then application.
   */
  public static <A, B, C, D> Func<D, C> bind(final Func<D, A> ca, final Func<D, B> cb, final Func<A, Func<B, C>> f) {
    return apply(compose(f, ca), cb);
  }

  /**
   * Applies a given function over the arguments of another function of arity-2.
   *
   * @param a The function whose arguments to apply another function over.
   * @param f The function to apply over the arguments of another function.
   * @return A function whose arguments are fed through function f, before being passed to function a.
   */
  public static <A, B, C> Func<B, Func<B, C>> on(final Func<A, Func<A, C>> a, final Func<B, A> f) {
    return compose(compose( Function.<B, A, C>andThen().f( f ), a ), f);
  }

  /**
   * Promotes a function of arity-2 to a higher-order function.
   *
   * @param f The function to promote.
   * @return A function of arity-2 promoted to compose with two functions.
   */
  public static <A, B, C, D> Func<Func<D, A>, Func<Func<D, B>, Func<D, C>>> lift(final Func<A, Func<B, C>> f) {
    return curry(new Func2<Func<D, A>, Func<D, B>, Func<D, C>>() {
      public Func<D, C> f(final Func<D, A> ca, final Func<D, B> cb) {
        return bind(ca, cb, f);
      }
    });
  }

  /**
   * Joins two arguments of a function of arity-2 into one argument, yielding a function of arity-1.
   *
   * @param f A function whose arguments to join.
   * @return A function of arity-1 whose argument is substituted for both parameters of <em>f</em>.
   */
  public static <A, B> Func<B, A> join(final Func<B, Func<B, A>> f) {
    return bind( f, Function.<Func<B, A>>identity() );
  }


  /**
   * Partial application of the second argument to the supplied function to get a function of type
   * <tt>A -> C</tt>. Same as <tt>flip(f).f(b)</tt>.
   *
   * @param f The function to partially apply.
   * @param b The value to apply to the function.
   * @return A new function based on <tt>f</tt> with its second argument applied.
   */
  public static <A, B, C> Func<A, C> partialApply2(final Func<A, Func<B, C>> f, final B b) {
    return new Func<A, C>() {
      public C f(final A a) {
        return uncurryF2(f).f(a, b);
      }
    };
  }

  /**
   * Partial application of the third argument to the supplied function to get a function of type
   * <tt>A -> B -> D</tt>.
   *
   * @param f The function to partially apply.
   * @param c The value to apply to the function.
   * @return A new function based on <tt>f</tt> with its third argument applied.
   */
  public static <A, B, C, D> Func<A, Func<B, D>> partialApply3(final Func<A, Func<B, Func<C, D>>> f, final C c) {
    return new Func<A, Func<B, D>>() {
      public Func<B, D> f(final A a) {
        return new Func<B, D>() {
          public D f(final B b) {
            return uncurryF3(f).f(a, b, c);
          }
        };
      }
    };
  }

  /**
   * Partial application of the fourth argument to the supplied function to get a function of type
   * <tt>A -> B -> C -> E</tt>.
   *
   * @param f The function to partially apply.
   * @param d The value to apply to the function.
   * @return A new function based on <tt>f</tt> with its fourth argument applied.
   */
  public static <A, B, C, D, E> Func<A, Func<B, Func<C, E>>> partialApply4(final Func<A, Func<B, Func<C, Func<D, E>>>> f, final D d) {
    return new Func<A, Func<B, Func<C, E>>>() {
      public Func<B, Func<C, E>> f(final A a) {
        return new Func<B, Func<C, E>>() {
          public Func<C, E> f(final B b) {
            return new Func<C, E>() {
              public E f(final C c) {
                return uncurryF4(f).f(a, b, c, d);
              }
            };
          }
        };
      }
    };
  }

  /**
   * Partial application of the fifth argument to the supplied function to get a function of type
   * <tt>A -> B -> C -> D -> F$</tt>.
   *
   * @param f The function to partially apply.
   * @param e The value to apply to the function.
   * @return A new function based on <tt>f</tt> with its fifth argument applied.
   */
  public static <A, B, C, D, E, F$> Func<A, Func<B, Func<C, Func<D, F$>>>> partialApply5(final Func<A, Func<B, Func<C, Func<D, Func<E, F$>>>>> f,
                                                                             final E e) {
    return new Func<A, Func<B, Func<C, Func<D, F$>>>>() {
      public Func<B, Func<C, Func<D, F$>>> f(final A a) {
        return new Func<B, Func<C, Func<D, F$>>>() {
          public Func<C, Func<D, F$>> f(final B b) {
            return new Func<C, Func<D, F$>>() {
              public Func<D, F$> f(final C c) {
                return new Func<D, F$>() {
                  public F$ f(final D d) {
                    return uncurryF5(f).f(a, b, c, d, e);
                  }
                };
              }
            };
          }
        };
      }
    };
  }

  /**
   * Partial application of the sixth argument to the supplied function to get a function of type
   * <tt>A -> B -> C -> D -> E -> G</tt>.
   *
   * @param f  The function to partially apply.
   * @param f$ The value to apply to the function.
   * @return A new function based on <tt>f</tt> with its sixth argument applied.
   */
  public static <A, B, C, D, E, F$, G> Func<A, Func<B, Func<C, Func<D, Func<E, G>>>>> partialApply6(
      final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, G>>>>>> f, final F$ f$) {
    return new Func<A, Func<B, Func<C, Func<D, Func<E, G>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, G>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, G>>>>() {
          public Func<C, Func<D, Func<E, G>>> f(final B b) {
            return new Func<C, Func<D, Func<E, G>>>() {
              public Func<D, Func<E, G>> f(final C c) {
                return new Func<D, Func<E, G>>() {
                  public Func<E, G> f(final D d) {
                    return new Func<E, G>() {
                      public G f(final E e) {
                        return uncurryF6(f).f(a, b, c, d, e, f$);
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

  /**
   * Partial application of the seventh argument to the supplied function to get a function of type
   * <tt>A -> B -> C -> D -> E -> F$ -> H</tt>.
   *
   * @param f The function to partially apply.
   * @param g The value to apply to the function.
   * @return A new function based on <tt>f</tt> with its seventh argument applied.
   */
  public static <A, B, C, D, E, F$, G, H> Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, H>>>>>> partialApply7(
      final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, H>>>>>>> f, final G g) {
    return new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, H>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, H>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, H>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, H>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, H>>>>() {
              public Func<D, Func<E, Func<F$, H>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, H>>>() {
                  public Func<E, Func<F$, H>> f(final D d) {
                    return new Func<E, Func<F$, H>>() {
                      public Func<F$, H> f(final E e) {
                        return new Func<F$, H>() {
                          public H f(final F$ f$) {
                            return uncurryF7(f).f(a, b, c, d, e, f$, g);
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

  /**
   * Partial application of the eigth argument to the supplied function to get a function of type
   * <tt>A -> B -> C -> D -> E -> F$ -> G -> I</tt>.
   *
   * @param f The function to partially apply.
   * @param h The value to apply to the function.
   * @return A new function based on <tt>f</tt> with its eigth argument applied.
   */
  public static <A, B, C, D, E, F$, G, H, I> Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, I>>>>>>> partialApply8(
      final Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, Func<H, I>>>>>>>> f, final H h) {
    return new Func<A, Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, I>>>>>>>() {
      public Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, I>>>>>> f(final A a) {
        return new Func<B, Func<C, Func<D, Func<E, Func<F$, Func<G, I>>>>>>() {
          public Func<C, Func<D, Func<E, Func<F$, Func<G, I>>>>> f(final B b) {
            return new Func<C, Func<D, Func<E, Func<F$, Func<G, I>>>>>() {
              public Func<D, Func<E, Func<F$, Func<G, I>>>> f(final C c) {
                return new Func<D, Func<E, Func<F$, Func<G, I>>>>() {
                  public Func<E, Func<F$, Func<G, I>>> f(final D d) {
                    return new Func<E, Func<F$, Func<G, I>>>() {
                      public Func<F$, Func<G, I>> f(final E e) {
                        return new Func<F$, Func<G, I>>() {
                          public Func<G, I> f(final F$ f$) {
                            return new Func<G, I>() {
                              public I f(final G g) {
                                return uncurryF8(f).f(a, b, c, d, e, f$, g, h);
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
    };
  }
}
