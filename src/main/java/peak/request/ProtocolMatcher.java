package peak.request;

import javax.servlet.http.HttpServletRequest;

/**
 * Matches the protocol of a request, yielding true if the protocol matches the given protocol.
 * @author atlosm
 *
 */
public class ProtocolMatcher extends RequestMatcher
{

	public final Protocol protocol;

	public ProtocolMatcher(Protocol protocol) {
		this.protocol = protocol;
	}

	public Boolean f(HttpServletRequest req) {
		return req.getProtocol().equalsIgnoreCase(protocol.name());
	}

	@Override
	public String toString() {
		return "match(protocol=" + protocol + ")";
	}

}
