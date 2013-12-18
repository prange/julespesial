package js;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;
import fj.Show;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

public class TestWebServer {


    public static AsyncHttpClient client = new AsyncHttpClient();

    public static Server start(int port) throws Exception {
        Server server = new Server( port );
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath( "/" );
        webAppContext.addEventListener( new JuleApp() );
        webAppContext.setBaseResource( Resource.newResource( "target" ) );
        server.setHandler( webAppContext );
        server.start();
        return server;

    }

    @Test
    public void testApi() throws Exception {
        Server server = start( 8080 );
        ListenableFuture<Response> call = client.prepareGet( "http://localhost:8080/hello" )
                .addQueryParameter( "name", "jalla" ).execute();


        Show.stringShow.println( call.get().getResponseBody());
        //Kan future klassen forbedres?
        server.stop();
    }

}
