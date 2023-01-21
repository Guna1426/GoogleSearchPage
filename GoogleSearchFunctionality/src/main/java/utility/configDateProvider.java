package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class configDateProvider {
	
	Properties prop;
	
	public void getConfigProperty() throws IOException
	{
		File file = new File("./config/config.properties");

		try {
			FileInputStream fis = new FileInputStream(file);
			prop  = new Properties();
			prop.load(fis);
			
		} catch (FileNotFoundException e) {
			System.out.println("Unable to Read The file config.Properites"+e.getMessage());
		}
		
	}
	
	public String getDateFromConfig(String key)
	{
		return prop.getProperty(key);
		
	}
	
	public String getStagingURL()
	{
		return prop.getProperty("url");
		
	}

	public String getBrowser()
	{
		System.out.println(prop.getProperty("browser"));
		return prop.getProperty("browser");
	}
}
