package peak.request;

import fj.data.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Combines several matchers with boolean AND.
 * @author atlosm
 *
 */
public class CompositeMatcher extends RequestMatcher
{

	private final List<RequestMatcher> matchers;

	private CompositeMatcher(List<RequestMatcher> matchers) {
		this.matchers = matchers;
	}

	public static CompositeMatcher and(RequestMatcher...matchers){
		return new CompositeMatcher(List.list(matchers));
	}
	
	@Override
	public Boolean f(final HttpServletRequest req) {
		for(RequestMatcher matcher:matchers.reverse())
			if(!matcher.f(req))
				return false;
		return true;
	}

	public String toString() {
		return "Match(" + matchers + ")";
	}

}
