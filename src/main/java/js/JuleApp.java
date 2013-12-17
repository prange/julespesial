package js;


import peak.RouteBuilder;
import peak.WebApplication;
import peak.response.StringResponse;

public class JuleApp extends WebApplication {

    @Override
    public RouteBuilder init(RouteBuilder builder) {
        return builder
                .get( "/", (request) -> new StringResponse( "Julespesial" ) )
                .get( "/hello", (request) -> new StringResponse( "Hello" ) )
                .get( "/godjul", new GodJulHandler() );
    }
}
