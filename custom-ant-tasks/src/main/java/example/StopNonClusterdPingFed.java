package example;

import java.io.IOException;
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
		Stream<ProcessHandle> allProcesses = ProcessHandle.allProcesses();
		allProcesses.forEach((ProcessHandle h) -> {

			long pid = h.pid();
			Optional<String> commandLine = h.info().commandLine();
			if (commandLine.isPresent()) {
				String cmdLine = commandLine.get();
				
				if (cmdLine.endsWith("org.pingidentity.RunPF")) {
					System.out.println(pid+"\t"+cmdLine.substring(0, 25));
					kill(pid);
				}
			}

		});

	}

	private void kill(long pid) {
		Optional<ProcessHandle> processhandleOptional = ProcessHandle.of(pid);
		if (processhandleOptional.isPresent()) {
			ProcessHandle processHandle = processhandleOptional.get();
			processHandle.destroy();
			System.out.println("called destroy for pid=" + pid);
		}
	}

}
