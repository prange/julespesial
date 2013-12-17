package peak.request;

import javax.servlet.http.HttpServletRequest;

/**
 * Matches a request method, yielding True if the request matches the given method.
 * @author atlosm
 *
 */
public class MethodMatcher extends RequestMatcher
{

	public final HttpMethod method;

	public MethodMatcher(HttpMethod method) {
		this.method = method;
	}

	public Boolean f(HttpServletRequest req) {
		return req.getMethod().equalsIgnoreCase(method.name());
	}

	public String toString() {
		return "match(method=" + method + ")";
	}

}
