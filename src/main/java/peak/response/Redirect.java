package peak.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirects
 *
 */
public class Redirect extends ResponseBuilder
{

	private final Addr addr;
	
	public Redirect(Addr address) {
		this.addr = address;
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(addr.address);
		} catch (Exception e) {
			//Again with the exception, is this really that expected?
			e.printStackTrace();
		}
	}

}
