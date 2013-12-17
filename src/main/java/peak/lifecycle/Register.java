package peak.lifecycle;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.util.EnumSet;

public class Register
{

    public static void filter(ServletContextEvent event, String path,Filter filter){
        FilterRegistration registration = event.getServletContext().addFilter(filter.getClass().getName(),filter);
        registration.addMappingForUrlPatterns( EnumSet.allOf( DispatcherType.class ),true,path);
    }

    public static void servlet(ServletContextEvent event, String path, HttpServlet servlet){
        ServletRegistration registration = event.getServletContext().addServlet(servlet.getClass().getName(),servlet);
        registration.addMapping(path);
    }
}
