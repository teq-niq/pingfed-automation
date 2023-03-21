package example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Launch {
	private File pingFedLaunchPath;
	
	
	public Launch(File pingFedHomePath)
	{
		//detect windows or linux
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win"))
		{
			this.pingFedLaunchPath=new File(pingFedHomePath, "bin/run.bat");
		}
		else
		{
			this.pingFedLaunchPath=new File(pingFedHomePath, "bin/run.sh");
		}
		

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		if(args.length==1)
		{
			new Launch(
				new File(args[0])).launch();
		}
		else
		{
			throw new IllegalArgumentException("Incorrect arguments");
		}

	}

	public  void launch() throws IOException, InterruptedException {
		
					launchInternal();
	}

			private void launchInternal() throws IOException, InterruptedException {
				ProcessBuilder processBuilder = new ProcessBuilder(
						pingFedLaunchPath.getAbsolutePath());

				Process process = processBuilder.start();

				
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line = null;
			
				
				while ((line = reader.readLine()) != null) {

					System.out.println("Launched PingFederate. Check logs.");
					break;
					
					
				}
				
				
					
				
				
				
			}
			
			
		
		
	

	

}
