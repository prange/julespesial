package peak.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the content type of the response to the given type.
 *
 */
public class SetContentType extends ResponseBuilder
{

	private final String contentType;

    public static final SetContentType json = new SetContentType("application/json");
    public static final SetContentType plain = new SetContentType("text/plain");

	public SetContentType(String contentType){
		this.contentType = contentType;
	}
	
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(contentType);
	}


}
