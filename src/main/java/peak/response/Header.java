package peak.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Header extends ResponseBuilder {

    public static final Header allowCrossDomain = new Header( "Access-Control-Allow-Origin","*" );

    public final String name;

    public final String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader( name, value );
    }
}
