package peak;

import peak.lifecycle.Register;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class WebApplication implements ServletContextListener {



    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final RouteBuilder b = init(RouteBuilder.create());
        Register.filter(servletContextEvent,"*",new RouteFilter() {
            @Override protected RouteBuilder init(RouteBuilder builder, FilterConfig config) {
                return b;
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public abstract RouteBuilder init(RouteBuilder builder);

}
