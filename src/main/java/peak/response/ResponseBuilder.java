package peak.response;

import fj.Func;
import fj.Function;
import fj.data.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ResponseBuilder {

    /**
     * Invoked when a request is made on this route's corresponding path e.g.
     * '/hello'
     * <p/>
     * Use and() to concatenate handlers:
     * <code>
     * ResponseHandler response = new SetResponseCode(200).and(new SetContentType("text/mustache")).and(new StringResponse("This is a string"));
     * </code>
     *
     * @param request  The request object providing information about the HTTP
     *                 request
     * @param response The response object providing functionality for modifying the
     *                 response
     */
    public abstract void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public ResponseBuilder and(ResponseBuilder other) {
        return new CompositeResponseBuilder( List.list( other, this ) );
    }

    public ResponseBuilder or(ResponseBuilder other) {
        return new OnExceptionResponseBuilder( this, Function.<Exception, ResponseBuilder>constant( other ) );
    }

    public ResponseBuilder or(Func<Exception,ResponseBuilder> other) {
        return new OnExceptionResponseBuilder( this, other );
    }

    static class CompositeResponseBuilder extends ResponseBuilder {
        private final List<ResponseBuilder> handlers;

        public CompositeResponseBuilder(List<ResponseBuilder> handlers) {
            this.handlers = handlers;
        }

        @Override
        public void handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            for (ResponseBuilder handler : handlers.reverse()) {
                handler.handle( request, response );
            }

        }

        public ResponseBuilder and(ResponseBuilder other) {
            return new CompositeResponseBuilder( handlers.cons( other ) );
        }
    }

    static class OnExceptionResponseBuilder extends ResponseBuilder {

        private final ResponseBuilder wrapped;

        private final Func<Exception, ResponseBuilder> errorHandler;

        OnExceptionResponseBuilder(ResponseBuilder wrapped, Func<Exception, ResponseBuilder> errorHandler) {
            this.wrapped = wrapped;
            this.errorHandler = errorHandler;
        }

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
            try {
                wrapped.handle( request, response );
            } catch (Exception e) {
                errorHandler.f( e ).handle( request, response );
            }
        }
    }
}
