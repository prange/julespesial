package peak.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the response code in the response to the given code.
 *
 * @author atlosm
 */
public class ResponseCode extends ResponseBuilder {
    public static final ResponseCode OK = new ResponseCode( 200 );

    public static final ResponseCode internal_server_error = new ResponseCode( 500 );

    private final int code;

    public ResponseCode(int code) {
        this.code = code;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus( code );
    }

}
