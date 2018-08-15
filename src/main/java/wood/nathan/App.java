package wood.nathan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;

/**
 * Hello world!
 *
 */
public class App {
	
	//final static File baseDir = new File("C:\\Users\\Nathan\\Pictures\\iphone");
	final static File baseDir = new File("P:\\Shared Pictures\\woodlornaj\\iphone");
	
	public static void main(String[] args) {
		
		for (File indivFile : getListOfFilesToProcess(baseDir)) {
			try {
				System.out.println(indivFile.getName());
				moveFileToNewFolder(indivFile);
				
				
				
			} catch (NoExifDataFoundException e) {
				System.out.println("Not processing " + e.getFilename());
			} catch (NotAPhotoException e) {
				System.out.println("Not processing " + e.getFilename());
			}
		}
		System.out.println("Complete");

	}

	private static ArrayList<File> getListOfFilesToProcess(File baseDir) {
		return new ArrayList<File>(Arrays.asList(baseDir.listFiles()));
	}

	private static Date getDatePictureTaken(File photo) throws NoExifDataFoundException, NotAPhotoException {

		IImageMetadata metadata;
		SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		try {
			metadata = Sanselan.getMetadata(photo);

			if (metadata instanceof JpegImageMetadata) {
				JpegImageMetadata jpgMetadata = (JpegImageMetadata) metadata;
				TiffField field = jpgMetadata.findEXIFValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
				if (field == null) {
					throw new NoExifDataFoundException(photo.getAbsolutePath());
				} else {
					try {
						return df.parse(field.getValueDescription().replaceAll("'", ""));
					} catch (ParseException e) {
						
						throw new NoExifDataFoundException(photo.getAbsolutePath());
					}
				}
			} else {
				throw new NotAPhotoException(photo.getAbsolutePath());
			}

		} catch (ImageReadException e) {
			throw new NoExifDataFoundException(photo.getAbsolutePath());
			
		} catch (IOException e) {
			
			throw new NotAPhotoException(photo.getAbsolutePath());
		}

	}
	
	/**
	 * if required, create a new directory
	 * @param datePhotoTaken
	 * @return
	 */
	private static File createMonthlyDirectoryForPhoto(Date datePhotoTaken) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(datePhotoTaken);
		File folderName = new File(baseDir + 
				File.separator + 
				( cal.get(Calendar.YEAR)) +  
				"_" +
				(cal.get(Calendar.MONTH)<=9 ? "0" : "") +
				(cal.get(Calendar.MONTH)+1));
		folderName.mkdir();
		
		return folderName;
	
			
	}
	
	private static void moveFileToNewFolder(File photo) throws NoExifDataFoundException, NotAPhotoException
	{
	
			File newLocation = createMonthlyDirectoryForPhoto(getDatePictureTaken(photo));
			try {
				Files.move(Paths.get(photo.toURI()), 
						Paths.get(newLocation + File.separator + photo.getName() ));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
	
	}
}
