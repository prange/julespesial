package peak;


import peak.request.Request;
import peak.response.ResponseBuilder;


/**
 * Responders handle the requests, and produce ResponseBuilders.
 */
public interface Responder
{

    public static Responder respondWith(ResponseBuilder builder){
        return new SimpleResponse(builder);
    }

    /**
     * An application that creates a responsebuilder based on the HandlerContext, which contains the httprequest and the
     *         servletcontext.
     * @return a ResponseBuilder that mutates the HttpServletResponse
     */
    public abstract ResponseBuilder run(final Request context);

    /**
     * A simple responder wrapping a responsehandler. Does not use any information in the context.
     * @author atlosm
     *
     */
    public static class SimpleResponse implements Responder
    {

        private final ResponseBuilder handler;

        public SimpleResponse(ResponseBuilder handler) {
            this.handler = handler;
        }

        @Override
        public ResponseBuilder run(Request context) {
            return handler;
        }

        @Override
        public String toString() {
            return "QuickResponse [handler=" + handler + "]";
        }

    }
}
