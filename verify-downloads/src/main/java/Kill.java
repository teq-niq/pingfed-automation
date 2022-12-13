import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class Kill {
public static void main(String[] args) {
	killUsingPidFromFile();
	
	
	
}

private static void killUsingPidFromFile() {
	Properties props= new Properties();
	File file= new File("pingfed.launch.properties");
	System.out.println("trying to load file:"+file.getAbsolutePath());
	
	try(FileReader fr = new FileReader(file);) {
		
		props.load(fr);
		System.out.println("Loaded file");
		String pidString = props.getProperty("pingfed.launch.pid");
		if(pidString!=null && pidString.length()>0)
		{
			long pid=Long.parseLong(pidString);
			 kill(pid);
		}
	} catch (IOException e) {
		System.out.println("Could not load file");
		System.err.println("Could not load file");
	}
}

private static void kill(long pid) {
	Optional<ProcessHandle> processhandleOptional = ProcessHandle.of(pid);
	 if(processhandleOptional.isPresent())
	 {
		 ProcessHandle processHandle = processhandleOptional.get();
		 processHandle.destroy();
		 System.out.println("called destroy for pid="+pid);
	 }
}
}
