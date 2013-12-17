package peak.response;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A file response, responding with the file with the given name. The file is fetched from the classpath, not the filesystem.
 * @author atlosm
 *
 */
public class FileResponse extends ResponseBuilder
{

	private Filename filename;

	public FileResponse(Filename filename) {
		this.filename = filename;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		try {
			ServletContext context = request.getServletContext();
			InputStream is = context.getResourceAsStream(filename.value);
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
