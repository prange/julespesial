package peak;


import peak.request.Request;
import peak.response.ResponseBuilder;


/**
 * Responders handle the requests, and produce ResponseBuilders.
 */
public interface Responder
{



    /**
     * An application that creates a responsebuilder based on the HandlerContext, which contains the httprequest and the
     *         servletcontext.
     * @return a ResponseBuilder that mutates the HttpServletResponse
     */
    public ResponseBuilder run(final Request context);


}
