package example;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class StartNonClusterdPingFed extends Task{
	private File pingFedHome;
	

	public void setPingFedHome(File pingFedHome) {
		this.pingFedHome = pingFedHome;
	}
	@Override
	public void execute() throws BuildException {
		try {
			new Launch(pingFedHome).launch();
		} catch (IOException | InterruptedException e) {
		
			e.printStackTrace();
			
			throw new BuildException("Problem launching", e);
		}
	}
	

}
