package peak.request;

import fj.data.Option;

/**
 * Part of the DSL for extracting named parameters from an url.
 * @author atlosm
 *
 */
public class UrlParam {

	private final String name;
	
	public UrlParam(final String name) {this.name = name;}
	
	public Option<String> get(Request req){
		return req.getAttributeAsString(name);
	}
	
}
