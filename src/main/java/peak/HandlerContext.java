package peak;

import fj.Func;
import fj.data.Option;
import peak.request.Request;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * The context for the web handlers. It contains typesafe getters and setter to the servlet context, the httprequest wrapped in
 * a typesafe interface and convenience functions for getting from and setting to the context.
 *
 * @author atlosm
 */
public class HandlerContext {

    public final HttpServletRequest request;

    public HandlerContext(HttpServletRequest request) {
        this.request = request;
    }

    public static <T> Func<HandlerContext, Option<T>> get_(final Class<T> c) {
        return new Func<HandlerContext, Option<T>>() {
            public Option<T> f(HandlerContext ac) {
                return ac.get( c );
            }
        };
    }

    public static void putInServletContext(Object value, ServletContext context) {
        putInServletContext( value.getClass().getName(), value, context );
    }

    public static void putInServletContext(String name, Object value, ServletContext context) {
        context.setAttribute( name, value );
    }

    public static <T> Option<T> getFromServletContext(Class<T> c, ServletContext sc) {
        return getFromServletContext( c, c.getName(), sc );
    }

    public static <T> Option<T> getFromServletContext(final Class<T> c, String name, ServletContext sc) {
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

    public <T> Option<T> get(final Class<T> c) {
        return HandlerContext.getFromServletContext( c, request.getServletContext() );
    }

    public Request getRequest() {
        return new Request( request );
    }
}
