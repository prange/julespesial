package peak.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A route. Contains functions for testing if a url matches the route, and also extracting named and splat params from the url if there is a match.
 * @author atlosm
 *
 */
public class Route {

	
	private static final String namedParam = "/:\\w+";
	private static final String splatParam = "/\\*\\w+";
	
	private final String path;
	private List<String> paramsNames = new ArrayList<String>();
	private final Pattern routePattern;
	
	public Route(String path) {
		this.path = path;
		String regex = path.replaceAll( namedParam, "/:?([\\\\w\\\\.\\\\-]+)").replaceAll(splatParam,"/\\\\*?(.+)");
		routePattern = Pattern.compile( regex );
		Matcher paramMatcher = routePattern.matcher(path);
		while(paramMatcher.find()){
			int count = paramMatcher.groupCount()+1;
			for(int i = 1; i < count; i++){
				paramsNames.add(paramMatcher.group(i));
			}
		}
	}
	
	public boolean matches(String test){
		return routePattern.matcher(test).matches();
	}
	
	public Map<String,String> getParams(String test){
		Map<String,String> params = new HashMap<String, String>();
		Matcher m = routePattern.matcher(test);
		int n = 0;
		
		while (m.find()) {
			int count = m.groupCount()+1;
			for(int i = 1; i < count; i++){
				params.put(paramsNames.get(n),m.group(i));
				n++;
			}
		}
		return params;
	}
	
	@Override
	public String toString() {
		return "UrlMatcher("+path+")";
	}
	
}
