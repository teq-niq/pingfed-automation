import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class VerifyDownloads {

	private static final String MYSQL_CONNECTOR_PREFIX = "mysql-connector-java-";
	private static final String JAR = ".jar";
	private static final String ZIP = ".zip";
	private static final String PING_FED_PREFIX = "PingFederate-";
	private static final String PING_FED_PREFIXL = PING_FED_PREFIX.toLowerCase();
	private static final String PING_DIR_PREFIX = "PingDirectory-";
	private static final String LIC = ".lic";

	public static void main(String[] args) throws IOException {
		new VerifyDownloads().verify();

	}

	private  void verify() throws IOException {
		File dir= new File("./../downloads");
		
		File[] listFiles = dir.listFiles();
		int zipCount=0;
		int licCount=0;
		int jarCount=0;
		String pdirLicVersion=null;
		String pfedLicVersion=null;
		String pdirVersion=null;
		String pfedVersion=null;
		String mysqlConnVersion=null;
		for (File file : listFiles) {
			if(file.getName().endsWith(LIC))
			{
				licCount++;
				if(file.getName().startsWith(PING_DIR_PREFIX))
				{
					pdirLicVersion=getVersion(file.getName(), PING_DIR_PREFIX, LIC);
				}
				else if(file.getName().startsWith(PING_FED_PREFIX))
				{
					pfedLicVersion=getVersion(file.getName(), PING_FED_PREFIX, LIC);

				}
			}
			else if(file.getName().endsWith(ZIP))
			{
				zipCount++;
				if(file.getName().startsWith(PING_DIR_PREFIX))
				{
					pdirVersion=getVersion(file.getName(), PING_DIR_PREFIX, ZIP);
				}
				else if(file.getName().toLowerCase().startsWith(PING_FED_PREFIXL))
				{
					pfedVersion=getVersion(file.getName(), PING_FED_PREFIXL.toLowerCase(), ZIP);
				}
			}
			else if(file.getName().endsWith(JAR))
			{
				jarCount++;
				if(file.getName().startsWith(MYSQL_CONNECTOR_PREFIX))
				{
					mysqlConnVersion=getVersion(file.getName(), MYSQL_CONNECTOR_PREFIX, JAR);
				}
			}
		}
		
		System.out.println("Expecting:zipCount=2,jarCount=1,licCount=2");
		System.out.println("Found:zipCount="+zipCount+",jarCount="+jarCount+",licCount="+licCount);
		if(zipCount!=2||jarCount!=1||licCount!=2)
		{
			throw new AssertionError("downloads folder is not verified.Follow downloadnotes.txt in downloads folder. Ensure you download  only one file of each type");
		}
		
		Properties properties= new Properties();
		properties.put("ping.dir.lic.version", pdirLicVersion);
		properties.put("ping.fed.lic.version", pfedLicVersion);
		properties.put("ping.dir.version", pdirVersion);
		properties.put("ping.fed.version", pfedVersion);
		properties.put("mysql.conn.version", mysqlConnVersion);
		File versionFile= new File("./../versions.properties");
		try(FileWriter fw= new FileWriter(versionFile);)
		{
			properties.store(fw, "generated");
		}
		
		
		
		if(pdirLicVersion!=null && pfedLicVersion!=null && pdirVersion!=null && pfedVersion!=null &&mysqlConnVersion!=null)
		{
			System.out.println("Versions captured. see generated versions.properties at "+versionFile.getAbsolutePath());
		}
		else
		{
			throw new AssertionError("Versions could not be captured. see generated versions.properties at "+versionFile.getAbsolutePath());
		}
	}

	private String getVersion(String fileName, String prefix, String extension) {
		String ret=fileName.substring(prefix.length(), fileName.length()-extension.length());
		return ret;
	}

}
