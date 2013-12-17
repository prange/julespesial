package peak.response;

import fj.Func;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Converts an object to a string representation with the given mapper. Useful for json responsebuilders.
 * @author atlosm
 *
 */
public class MappedResponse<T> extends ResponseBuilder
{

	private final T value;
	private final Func<T,String> mapper;
	
	public MappedResponse(T value, Func<T,String> mapper) {
		this.value = value;
		this.mapper = mapper;
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().print(mapper.f(value));
		} catch (IOException e) {
			// Oh no! Is this even possible? What to tell the customer? Sorry, we cannot write to your stream just now.. Oh wait, we cant write to the customer at all...
			e.printStackTrace();
		}
		
	}

}
