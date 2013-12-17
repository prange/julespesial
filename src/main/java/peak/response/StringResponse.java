package peak.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A string response
 *
 */
public class StringResponse extends ResponseBuilder
{

	private final String string;

	public StringResponse(String string) {
		this.string = string;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getOutputStream().print(string);
		} catch (IOException e) {
			// Well, if it comes to this, we have to give up...
			e.printStackTrace();
		}

	}

}
