package js.util;

import fj.Effect;
import fj.Show;
import org.slf4j.Logger;

public class Log {

    public static <A> Effect<A> toSystemOut(final Show<A> show) {
        return new Effect<A>() {
            @Override public void e(A a) {
                show.println( a );
            }
        };
    }

    public static <A> Effect<A> info(final Logger logger, Show<A> show) {
        return new Effect<A>() {
            @Override public void e(A a) {
                logger.warn( show.showS( a ) );
            }
        };
    }

}
