package admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	
	public static Properties loadProps(File file) throws FileNotFoundException, IOException
	{
		Properties props=null;
		if(file.exists())
		{
			props= new Properties();
			try(FileReader propsfile=new FileReader(file))
			{
				props.load(propsfile);
			}
		}
		return props;
	}

}
