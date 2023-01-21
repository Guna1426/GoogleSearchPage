package base;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class Helper {

	public static String captureScreenshots(WebDriver driver) {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String ScreenshotPath = System.getProperty("user.dir")+"/Screenshots/"+getCurrentDateTime()+".PNG";
		try {
			FileHandler.copy(src, new File(ScreenshotPath));
			// System.out.println("Screenshot captured");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ScreenshotPath;
	}

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yy_HH_mm_ss");
		Date currentDate = new Date();
		return dateFormat.format(currentDate);

	}

}
