package wood.nathan;

public class NoExifDataFoundException extends Exception {
	
	private static final long serialVersionUID = 2044047201017184261L;
	
	private String filename;
	
	public NoExifDataFoundException(String filename) {
		this.filename = filename;
	}
	
	public String getFilename()
	{
		return filename;
	}
}
