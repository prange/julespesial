package peak;

import fj.P;
import fj.P2;
import fj.data.List;
import peak.request.*;

/**
 * DSL for building routes
 *
 * @author atlosm
 */
public class RouteBuilder
{
    private final List<P2<RequestMatcher, Responder>> ehs;

    public RouteBuilder(List<P2<RequestMatcher, Responder>> ehs)
    {
        this.ehs = ehs;
    }

    public static RouteBuilder create()
    {
        return new RouteBuilder(List.<P2<RequestMatcher, Responder>>nil());
    }

    public List<P2<RequestMatcher, Responder>> getHandlers()
    {
        return ehs;
    }

    public HandleType match(RequestMatcher matcher)
    {
        return new HandleType(matcher);
    }

    public HandleType byMethod(HttpMethod method, String route)
    {
        return match( CompositeMatcher.and( new MethodMatcher( method ), new RouteMatcher( new Route( route ) ) ));
    }

    public HandleType get(String route)
    {
        return byMethod(HttpMethod.GET, route);
    }

    public HandleType post(String route)
    {
        return byMethod(HttpMethod.POST, route);
    }

    public RouteBuilder get(String route, Responder app)
    {
        return get(route).handle(app);
    }

    public RouteBuilder post(String route, Responder app)
    {
        return post(route).handle(app);
    }

    public class HandleType
    {
        private final RequestMatcher matcher;

        public HandleType(RequestMatcher matcher)
        {
            this.matcher = matcher;
        }

        public RouteBuilder handle(Responder app)
        {
            return new RouteBuilder(ehs.cons(P.p(matcher, app)));
        }
    }
}
