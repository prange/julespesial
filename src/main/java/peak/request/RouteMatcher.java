package peak.request;

import peak.FilterUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Matches a request based on the give route.
 * @author atlosm
 *
 */
public class RouteMatcher extends RequestMatcher
{

	@Override
	public String toString() {
		return "Matche(route=" + route + ")";
	}

	public final Route route;
	
	public RouteMatcher(Route route) {
		this.route = route;
	}
	
	@Override
	public Boolean f(HttpServletRequest req) {
		String path = FilterUtils.getRelativePath( req );
		boolean match =  route.matches(path);
		if(match){
			Map<String,String> params = route.getParams(path);
			for(Entry<String,String> entry:params.entrySet()){
				req.setAttribute(entry.getKey(), entry.getValue());
			}
		}
		return match;
	}

}
