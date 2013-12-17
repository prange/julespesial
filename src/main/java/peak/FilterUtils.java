package peak;

import javax.servlet.http.HttpServletRequest;

public class FilterUtils {
	
	private static final String SLASH = "/";
	
    public static String getRelativePath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        path = path.substring(contextPath.length());

        if (path.length() > 0) {
            path = path.substring(1);
        }
        
        if (!path.startsWith(SLASH)) {
            path = SLASH + path;
        }

        return path;
    }
}
