package example;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class StartNonClusterdPingFed extends Task{
	private File pingFedHome;
	private File launchPropertiesFile;
	public void setLaunchPropertiesFile(File launchPropertiesFile) {
		this.launchPropertiesFile = launchPropertiesFile;
	}
	public void setPingFedHome(File pingFedHome) {
		this.pingFedHome = pingFedHome;
	}
	@Override
	public void execute() throws BuildException {
		try {
			new Launch(pingFedHome, launchPropertiesFile).launch();
		} catch (IOException e) {
		
			e.printStackTrace();
			
			throw new BuildException("Problem launching", e);
		}
	}
	

}
