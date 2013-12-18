package fj.control;

import fj.Func;
import fj.Function;
import fj.data.List;
import fj.data.Option;

import java.util.concurrent.Callable;


/**
 * IO. A monadic structure that encapsulates a sideeffect.
 *
 * @param <A> The type of the result the sideeffect produces.
 * @author atlosm
 */
public abstract class Action<A> {

    /**
     * Lifts the value a into an Action A
     *
     * @param a
     * @return
     */
    public static <A> Action<A> point(final A a) {
        return new Action<A>() {
            public A unsafePerformIO() {
                return a;
            }
        };
    }

    /**
     * Lifts the lazy value a into an Action A
     *
     * @param a
     * @return
     */
    public static <A> Action<A> point(final Callable<A> a) {
        return new Action<A>() {
            public A unsafePerformIO() {
                try {
                    return a.call();
                } catch (Exception e) {
                    throw new Error(
                            "Shame on you, you threw an exception somewhere in you code expecting me to handle it? If you are a customer and see this, please report imediately at support@kantega.no, with a copy of the text below, thank you!",
                            e );
                }
            }
        };
    }

    /**
     * Removes one layer of monadic structure.
     *
     * @param a A action that results in another.
     * @return A new action equivalent to the result of the given action.
     */
    public static <A> Action<A> join(final Action<Action<A>> a) {
        return a.bind( Function.<Action<A>>identity() );
    }

    /**
     * Removes one layer of monadic structure.
     * <p/>
     * A action that results in another.
     *
     * @return A new action equivalent to the result of the given action.
     */
    public static <A> Func<Action<Action<A>>, Action<A>> join() {
        return new Func<Action<Action<A>>, Action<A>>() {
            public Action<A> f(Action<Action<A>> a) {
                return Action.join( a );
            }
        };
    }

    /**
     * Merges a list of actions into an action of list
     *
     * @param actions The actions to merge
     * @return the resulting action
     */
    public static <A> Action<List<A>> sequence(final List<Action<A>> actions) {
        return new Action<List<A>>() {
            public List<A> unsafePerformIO() {
                return actions.map( new Func<Action<A>, A>() {
                    public A f(Action<A> action) {
                        return action.unsafePerformIO();
                    }
                } );
            }
        };

    }

    /**
     * Lifts the function f to apply on Actions
     *
     * @param f
     * @return
     */
    public static <B, C> Func<Action<B>, Action<C>> lift(final Func<B, C> f) {
        return new Func<Action<B>, Action<C>>() {
            public Action<C> f(Action<B> b) {
                return b.map( f );
            }
        };
    }

    /**
     * Convenience method to flatten a nested Option within this Action
     *
     * @param action
     * @return
     */
    public static <B> Action<Option<B>> flatten(final Action<Option<Action<B>>> action) {
        return new Action<Option<B>>() {
            public Option<B> unsafePerformIO() {
                Option<Action<B>> o = action.unsafePerformIO();

                for (Action<B> a : o) {
                    return Option.some( a.unsafePerformIO() );
                }
                return Option.none();
            }
        };
    }

    /**
     * Perform the action
     *
     * @return
     */
    public abstract A unsafePerformIO();

    /**
     * Map a function over the result of this action.
     *
     * @param f The function to map over the result.
     * @return A new action that applies the given function to the result of
     *         this action.
     */
    public final <B> Action<B> map(final Func<A, B> f) {
        return new Action<B>() {
            public B unsafePerformIO() {
                return f.f( Action.this.unsafePerformIO() );
            }
        };
    }

    /**
     * Binds the given action across the result of this action.
     *
     * @param f The function to bind across the result of this action.
     * @return A new action equivalent to applying the given function to the
     *         result of this action.
     */
    public final <B> Action<B> bind(final Func<A, Action<B>> f) {
        return new Action<B>() {
            public B unsafePerformIO() {
                return f.f( Action.this.unsafePerformIO() ).unsafePerformIO();
            }
        };
    }

    /**
     * Binds the given action across the result of this action.
     *
     * @param f The function to bind across the result of this action.
     * @return A new action equivalent to applying the given function to the
     *         result of this action.
     */
    public final <B, C> Action<C> bind(final Action<B> ab, final Func<A, Func<B, C>> f) {
        return new Action<C>() {
            public C unsafePerformIO() {
                A a = Action.this.unsafePerformIO();
                B b = ab.unsafePerformIO();
                return f.f( a ).f( b );
            }
        };
    }

    /**
     * Append the other action to this action, ignoring the output from this
     * action, only returning the result from the other action
     *
     * @param other
     */
    public final <B> Action<B> andThen(final Action<B> other) {
        return new Action<B>() {
            public B unsafePerformIO() {
                Action.this.unsafePerformIO();
                return other.unsafePerformIO();
            }
        };
    }

    /**
     * RUn first the action that is supplied to the function, then run the other
     * action, ignoring the output of the first action.
     *
     * @param other The action to run after the function parameter.
     */
    public final <B> Func<Action<A>, Action<B>> andThen_(final Action<B> other) {
        return new Func<Action<A>, Action<B>>() {
            public Action<B> f(Action<A> arg0) {
                return new Action<B>() {
                    public B unsafePerformIO() {
                        Action.this.unsafePerformIO();
                        return other.unsafePerformIO();
                    }
                };
            }
        };

    }

}
