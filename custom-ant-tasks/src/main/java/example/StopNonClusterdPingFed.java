package example;
import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class StopNonClusterdPingFed extends Task{
	
	private File launchPropertiesFile;
	public void setLaunchPropertiesFile(File launchPropertiesFile) {
		this.launchPropertiesFile = launchPropertiesFile;
	}
	
	@Override
	public void execute() throws BuildException {
		
			try {
				new Kill(launchPropertiesFile).killUsingPidFromFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BuildException("Problem stopping", e);
			}
		
	}
	

}
