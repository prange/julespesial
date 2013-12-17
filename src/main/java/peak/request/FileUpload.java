package peak.request;

/**
 * Contains the file upload bytes
 * @author atlosm
 *
 */
public class FileUpload {
	
	public final byte[] bytes;
	public final String name;
	
	 public FileUpload(String name, byte[] bytes) {
		 this.name = name;
		 this.bytes = bytes;
	}

}
