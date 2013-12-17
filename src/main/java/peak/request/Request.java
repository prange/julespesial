package peak.request;

import fj.Func;
import fj.Ord;
import fj.data.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A wrapper around a HttpServletRequest.
 * @author atlosm
 *
 */
public class Request {

	private static final int DEFAULT_BUFFER_SIZE = 10240;
	public final HttpServletRequest underlying;

	public Request(HttpServletRequest underlying) {
		this.underlying = underlying;
	}

	public TreeMap<String, List<String>> getParameters() {
		return TreeMap.fromMutableMap(Ord.stringOrd, underlying.getParameterMap()).map( Array.<String>wrap().andThen( Conversions.<String>Array_List() ));
	}

	public Option<String> getParameter(final String name) {
		return getParameters().get(name).<String> map(List.<String> head_());
	}

	public Option<String> getAttributeAsString(final String name) {
		Object attr = underlying.getAttribute(name);
		if (attr instanceof String)
			return Option.some((String) attr);
		else
			return Option.none();
	}
	
	public Option<String> getHeader(String name){
		String attr = underlying.getHeader(name);
		if (attr!=null && !attr.isEmpty())
			return Option.some((String) attr);
		else
			return Option.none();
	}

	public Option<Part> getPart(String name) {
		try {
			Part part = underlying.getPart(name);
			return Option.some(part);
		} catch (Exception e) {
			return Option.none();
		}
	}

	public Option<String> getPartAsString(String name) {
		return getPart(name).bind( Request.partToString());
	}

	public Option<FileUpload> getPartAsFile(String name) {
		return getPart(name).bind( Request.partToFile());
	}

	public Validation<Exception, InputStream> getInputStream() {
		try {
			final InputStream content = underlying.getInputStream();
			return Validation.success(content);
		} catch (Exception e) {
			return Validation.fail(e);
		}
	}

	public static Func<Part, Option<String>> partToString() {
		return new Func<Part, Option<String>>() {
			public Option<String> f(final Part part) {

				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "utf-8"));
					StringBuilder value = new StringBuilder();
					char[] buffer = new char[DEFAULT_BUFFER_SIZE];
					for (int length = 0; (length = reader.read(buffer)) > 0;) {
						value.append(buffer, 0, length);
					}
					return Option.some(value.toString());
				} catch (Exception e) {
					e.printStackTrace();
					return Option.none();
				}

			}
		};
	}

	public static Func<Part, Option<FileUpload>> partToFile() {
		return new Func<Part, Option<FileUpload>>() {
			public Option<FileUpload> f(Part part) {

				try {
					InputStream is = part.getInputStream();
					int i = is.available();
					byte[] bytes = new byte[i];
					is.read(bytes);
					String name = getFileName(part);
					return Option.some(new FileUpload(name, bytes));
				} catch (IOException e) {
					return Option.none();
				}
			}
		};
	}

	private static String getFileName(Part part) {
		String partHeader = part.getHeader("content-disposition");
		for (String cd : partHeader.split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;

	}

}