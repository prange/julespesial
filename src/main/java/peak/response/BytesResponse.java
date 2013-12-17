package peak.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A binary response.
 * @author atlosm
 *
 */
public class BytesResponse extends ResponseBuilder
{

	public final byte[] bytes;
	public final String mimetype;

	public BytesResponse(byte[] bytes, String mimetype) {
		this.bytes = bytes;
		this.mimetype = mimetype;
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			new SetContentType(mimetype).handle(request, response);
			InputStream is = new ByteArrayInputStream(bytes);
			OutputStream os = response.getOutputStream();
			int val;
			while ((val = is.read()) != -1)
				os.write(val);
		} catch (Exception e) {
			// This is actually plausible, what to tell?
			throw new RuntimeException("Problems while streaming file",e);
		}
	}

}
