package example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class KillUsingFileForWin {
	private File launchPropertiesFile;

	public KillUsingFileForWin(File launchPropertiesFile) {
		this.launchPropertiesFile = launchPropertiesFile;
	}

	public static void main(String[] args) throws IOException {
		if(args.length==1)
		{
			new KillUsingFileForWin(new File(args[0])).killUsingPidFromFile();
		}
		else
		{
			throw new IllegalArgumentException("Incorrect arguments");
		}
	}

	public void killUsingPidFromFile() throws IOException {
		Properties props = new Properties();

		System.out.println("trying to load file:" + launchPropertiesFile.getAbsolutePath());

		try (FileReader fr = new FileReader(launchPropertiesFile);) {

			props.load(fr);
			System.out.println("Loaded file");
			String pidString = props.getProperty("pingfed.launch.pid");
			if (pidString != null && pidString.length() > 0) {
				long pid = Long.parseLong(pidString);
				kill(pid);
			}
		} catch (IOException e) {
			System.out.println("Could not load file");
			System.err.println("Could not load file");
			throw (e);
		}
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
