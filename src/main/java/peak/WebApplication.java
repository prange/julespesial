package peak;

import fj.Func;
import fj.Function;
import peak.lifecycle.Register;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class WebApplication implements ServletContextListener {



    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final RouteBuilder b = init(RouteBuilder.create());
        initContext().f(servletContextEvent.getServletContext());
        Register.filter(servletContextEvent,"*",new RouteFilter() {
            @Override protected RouteBuilder init(RouteBuilder builder, FilterConfig config) {
                return b;
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    protected Func<ServletContext,ServletContext> initContext(){
        return Function.identity();
    }

    protected abstract RouteBuilder init(RouteBuilder builder);

}
