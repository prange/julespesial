package peak;

import fj.Func;
import fj.data.Option;

import javax.servlet.ServletContext;

/**
 * The context for the web handlers. It contains typesafe getters and setter to the servlet context, the httprequest wrapped in
 * a typesafe interface and convenience functions for getting from and setting to the context.
 *
 * @author atlosm
 */
public class HandlerContext {

    private final ServletContext servletContext;

    public HandlerContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void set(Object value) {
        set( value.getClass().getName(), value );
    }

    public void set(String name, Object value) {
        servletContext.setAttribute( name, value );
    }

    public <T> Option<T> get(Class<T> c) {
        return get( c, c.getName(), servletContext );
    }

    public <T> Option<T> get(final Class<T> c, String name, ServletContext sc) {
        return Option
                .fromNull( sc.getAttribute( name ) )
                .bind( new Func<Object, Option<T>>() {
                    @Override public Option<T> f(Object o) {
                        try {
                            return Option.some( c.cast( o ) );
                        } catch (Exception e) {
                            return Option.none();
                        }
                    }
                } );
    }


}
