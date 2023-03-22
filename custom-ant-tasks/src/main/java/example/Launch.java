package example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

public class Launch {
	private File pingFedLaunchPath;
	private File launchPropertiesFile;
	
	public Launch(File pingFedHomePath, File launchPropertiesFile)
	{
		//detect windows or linux
		final boolean isOnWin = isOnWin();
		if(isOnWin)
		{
			this.pingFedLaunchPath=new File(pingFedHomePath, "bin/run.bat");
		}
		else
		{
			this.pingFedLaunchPath=new File(pingFedHomePath, "bin/run.sh");
		}
		
		this.launchPropertiesFile=launchPropertiesFile;
	}

	public static void main(String[] args) throws IOException {
		if(args.length==2)
		{
			new Launch(
				new File(args[0]),
				new File(args[1])).launch();
		}
		else
		{
			throw new IllegalArgumentException("Incorrect arguments");
		}

	}

	public  void launch() throws IOException {
		
		ProcessBuilder processBuilder = new ProcessBuilder(
				pingFedLaunchPath.getAbsolutePath());

		Process process = processBuilder.start();

		long pid1 = process.pid();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line = null;
		while ((line = reader.readLine()) != null) {

			System.out.println("Launched PingFederate. Check logs.");
			final boolean isOnWin = isOnWin();
			if(isOnWin)
			{
				checkPid(pid1);
			}
			
			break;
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
	private  void checkPid(long pid1) {
		Optional<ProcessHandle> processHandleOptional = ProcessHandle.of(pid1);
		if (processHandleOptional.isPresent()) {
			ProcessHandle processHandle = processHandleOptional.get();
			shoProcesshandle(false, processHandle);

			processHandle.descendants().forEach((handle) -> {
				shoProcesshandle(true, handle);

			});

		}
	}

	private  void shoProcesshandle(boolean inner, ProcessHandle processHandle) {
		long pid = processHandle.pid();
		long parentPid = -1;
		Optional<ProcessHandle> parentOptional = processHandle.parent();
		if (parentOptional.isPresent()) {
			parentPid = parentOptional.get().pid();
		}
		String command = getOptionalData(processHandle.info()::command, "");

		String commandLine = getOptionalData(processHandle.info()::commandLine, "");
		String[] arguments = getOptionalData(processHandle.info()::arguments, new String[] {});
		System.out.println("pid=" + pid + ",parentPid=" + parentPid + ",command=" + command + ",commandLine="
				+ commandLine + ",arguments=" + Arrays.toString(arguments));
		if (inner && (command.endsWith("java.exe")||command.endsWith("java"))) {
			Properties props = new Properties();
			props.setProperty("pingfed.launch.pid", String.valueOf(pid));

			System.out.println("see file:" + this.launchPropertiesFile.getAbsolutePath());
			try (FileWriter fw = new FileWriter(this.launchPropertiesFile);) {

				props.store(fw, "autogeneerated");
			} catch (IOException e) {
				// if could not write file wont be there
				System.out.println("could not create the file");
				System.err.println("could not create the file");
			}

		}
	}

	private  <E> E getOptionalData(Supplier<Optional<E>> m, E defaultVal) {
		E command = defaultVal;

		Optional<E> optional = m.get();
		if (optional.isPresent()) {
			command = optional.get();
		}
		return command;
	}

}
