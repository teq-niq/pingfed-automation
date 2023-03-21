package example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class StopNonClusterdPingFed extends Task {

	@Override
	public void execute() throws BuildException {

		try {
			inner();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new BuildException("Problem", e);
		}

	}
	
	private void inner() throws IOException, InterruptedException {
		final boolean isOnWin = isOnWin();
		System.out.println(isOnWin);
		Stream<ProcessHandle> allProcesses = ProcessHandle.allProcesses();
		
		
		Optional<ProcessHandle> found = allProcesses.parallel().filter((ProcessHandle h) -> {
			
			boolean ret=false;
			long pid = h.pid();
			Optional<String> commandLine = isOnWin?this.getCommandLine(pid):h.info().commandLine();
			

			
			
			if (commandLine.isPresent()) {
				String cmdLine = commandLine.get();
				cmdLine=cmdLine.trim();
				if (cmdLine.endsWith("org.pingidentity.RunPF")) {
					
					ret=true;
				}
			}
			return ret;

		}).findAny();
		
		if(found.isPresent())
		{
			kill(found.get().pid());
		}
		
		

	}

	private boolean isOnWin() {
		String os = System.getProperty("os.name").toLowerCase();
		boolean isOnWin=false;
		if(os.contains("win"))
		{
			isOnWin=true;
		}
		return isOnWin;
	}

	private void kill(long pid) {
		Optional<ProcessHandle> processhandleOptional = ProcessHandle.of(pid);
		if (processhandleOptional.isPresent()) {
			ProcessHandle processHandle = processhandleOptional.get();
			processHandle.destroy();
			System.out.println("called destroy for pid=" + pid);
		}
	}
	
	public static void main(String[] args) {
		StopNonClusterdPingFed stopNonClusterdPingFed = new StopNonClusterdPingFed();
		Optional<String> commandLine = stopNonClusterdPingFed.getCommandLine(26728);
		if(commandLine.isPresent())
		{
			System.out.println(commandLine.get());
		}
	}
	private Optional<String> getCommandLine(long desiredProcessid ) throws UncheckedIOException {

	   
	    try {
	      Process process = new ProcessBuilder("wmic", "process", "where", "ProcessID=" + desiredProcessid, "get",
	        "commandline", "/format:list").
	        redirectErrorStream(true).
	        start();
	      try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
	           BufferedReader reader = new BufferedReader(inputStreamReader)) {
	        while (true) {
	          String line = reader.readLine();
	          
	          if (line == null) {
	            return Optional.empty();
	          }
	          if (!line.startsWith("CommandLine=")) {
	            continue;
	          }
	          return Optional.of(line.substring("CommandLine=".length()));
	        }
	      }
	    } catch (IOException e) {
	      throw new UncheckedIOException(e);
	    }
	  }

}
