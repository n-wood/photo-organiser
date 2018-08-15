package wood.nathan;

public class NotAPhotoException extends Exception {

	private static final long serialVersionUID = -5496314338131632588L;
	
	private String filename;
	
	public NotAPhotoException(String filename) {
		this.filename = filename;
	}
	
	public String getFilename()
	{
		return filename;
	}
}
