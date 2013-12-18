package peak;

import fj.Func;
import fj.Func2;
import fj.P2;
import fj.Unit;
import fj.data.List;
import fj.data.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import peak.request.Request;
import peak.request.RequestMatcher;
import peak.request.UrlParam;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * The routefilter is a superclass/DSL for servletfilters that answer to some httpservletrequest.
 *
 * @author atlosm
 */
public abstract class RouteFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger( RouteFilter.class );

    private List<P2<RequestMatcher, Responder>> handlers;

    public void destroy() {

    }

    /**
     * Part of the servlet API
     */
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpReq = (HttpServletRequest) request;
        final HttpServletResponse httpRes = (HttpServletResponse) response;

        logger.debug( "Matching request " + httpReq.getMethod() + " - " + httpReq.getRequestURL() );

        Option<Responder> handler =

                handlers.foldLeft(
                        new Func2<Option<Responder>, P2<RequestMatcher, Responder>, Option<Responder>>() {
                            public Option<Responder> f(Option<Responder> found, P2<RequestMatcher, Responder> current) {
                                if (found.isSome())
                                    return found;
                                else if (current._1().matches( httpReq ))
                                    return Option.some( current._2() );
                                else
                                    return Option.none();
                            }
                        }, Option.<Responder>none() );

        Action<Unit> action =
                handler.option( invokeChain( request, response, chain ), runResponder( httpReq, httpRes ) );

        action.execute();
    }

    /**
     * Part of the servlet API
     */
    public void init(FilterConfig fc) throws ServletException {
        RouteBuilder builder =
                init( RouteBuilder.create(), fc );

        handlers =
                builder.getHandlers().reverse();

        for (P2<RequestMatcher, Responder> handler : handlers) {
            logger.info( "Registered " + handler._1() +":"+handler._2().getClass().getName() );
        }
    }

    /**
     * Creates an url param with the given name. The UrlParam object contains functions to extract a named param from the url when give a context.
     * Part of the DSL
     *
     * @param name
     * @return
     */
    public UrlParam param(String name) {
        return new UrlParam( name );
    }

    /**
     * Action that calls invokeChain
     *
     * @param request
     * @param response
     * @param chain
     * @return
     */
    private Action<Unit> invokeChain(final ServletRequest request, final ServletResponse response, final FilterChain chain) {
        return new Action<Unit>() {
            public Unit execute() {
                try {
                    chain.doFilter( request, response );
                } catch (Exception e) {
                    e.printStackTrace( System.err );
                }
                return Unit.unit();
            }
        };
    }

    /**
     * Action that executes the Responder that responds to the request.
     *
     * @param request
     * @param response
     * @return
     */
    private Func<Responder, Action<Unit>> runResponder(final HttpServletRequest request, final HttpServletResponse response) {
        return new Func<Responder, Action<Unit>>() {
            public Action<Unit> f(final Responder responder) {
                return new Action<Unit>() {
                    public Unit execute() {
                        try {
                            request.setCharacterEncoding( "UTF-8" );
                            response.setCharacterEncoding( "UTF-8" );
                            response.setContentType( "application/json" );//Default encoding
                            logger.debug( "Invoking application " + responder.getClass().getName() );
                            responder.run( new Request( request, new HandlerContext( request.getServletContext() ) ) ).handle( request, response );

                        } catch (UnsupportedEncodingException e) {
                            logger.error( "Unsupported encoding", e );
                        } catch (Exception e) {
                            logger.error("Unhandled exception in responder for"+request.getRequestURL(),e);
                        }
                        return Unit.unit();
                    }
                };
            }
        };
    }

    /**
     * The DSL entry point.
     *
     * @param builder
     * @param config
     * @return
     */
    protected abstract RouteBuilder init(RouteBuilder builder, FilterConfig config);

    private static abstract class Action<A>{
        public abstract A execute();
    }

}
