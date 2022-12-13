import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.lang.ProcessHandle.Info;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

public class Launch {

	public static void main(String[] args) throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder("D:/dev/priv/wss/pinfed/pingfed-automation-ws2/git/pingfed-automation/win/ping/pingfederate/pingfederate-11.1.2/bin/run.bat");
		
		Process process = processBuilder.start();


		long pid1 = process.pid();
		System.out.println(pid1);
		//checkPid(pid1);
		
		BufferedReader reader = 
                new BufferedReader(new InputStreamReader(process.getInputStream()));

String line = null;
while ( (line = reader.readLine()) != null) {

  
   System.out.println(line);
   checkPid(pid1);
   break;
}

	}

	private static void checkPid(long pid1)  {
		Optional<ProcessHandle> processHandleOptional = ProcessHandle.of(pid1);
		if(processHandleOptional.isPresent())
		{
			ProcessHandle processHandle = processHandleOptional.get();
			shoProcesshandle(false, processHandle);
			
			processHandle.descendants().forEach((handle)->{
				shoProcesshandle(true, handle);
				
			});
			
		}
	}

	private static void shoProcesshandle(boolean inner, ProcessHandle processHandle) {
		long pid = processHandle.pid();
		long parentPid=-1;
		Optional<ProcessHandle> parentOptional = processHandle.parent();
		if(parentOptional.isPresent())
		{
			parentPid=parentOptional.get().pid();
		}
		String command = getOptionalData(processHandle.info()::command, "");
		
		String commandLine = getOptionalData(processHandle.info()::commandLine, "");
		String[] arguments = getOptionalData(processHandle.info()::arguments, new String[]{});
		System.out.println("pid="+pid+",parentPid="+parentPid+",command="+command+",commandLine="+commandLine+",arguments="+Arrays.toString(arguments));
		if(inner && command.endsWith("java.exe"))
		{
			Properties props= new Properties();
			props.setProperty("pingfed.launch.pid", String.valueOf(pid));
			File file= new File("pingfed.launch.properties");
			System.out.println("see file:"+file.getAbsolutePath());
			try(FileWriter fw = new FileWriter(file);) {
				
				props.store(fw, "autogeneerated");
			} catch (IOException e) {
				//if could not write file wont be there
				System.out.println("could not create the file");
				System.err.println("could not create the file");
			}
			
			
		}
	}

	private static <E> E getOptionalData(Supplier<Optional<E>> m, E defaultVal) {
		E command=defaultVal;
		
		Optional<E> optional = m.get();
		if(optional.isPresent())
		{
			command=optional.get();
		}
		return command;
	}

}
