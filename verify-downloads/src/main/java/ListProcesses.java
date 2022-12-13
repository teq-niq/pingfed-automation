import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class ListProcesses {

	public static void main(String[] args) {
		 ProcessHandle.allProcesses().forEach((processHandle)->{
			 
			 shoProcesshandle(processHandle, "");
				processHandle.descendants().forEach((handle)->{
					shoProcesshandle(handle, "\t");
					
				});
				
			
		 });

	}
	
	private static void shoProcesshandle(ProcessHandle processHandle, String indent) {
		long pid = processHandle.pid();
		boolean alive = processHandle.isAlive();
		long parentPid=-1;
		Optional<ProcessHandle> parentOptional = processHandle.parent();
		if(parentOptional.isPresent())
		{
			parentPid=parentOptional.get().pid();
		}
		String command = getOptionalData(processHandle.info()::command, "");
		
		String commandLine = getOptionalData(processHandle.info()::commandLine, "");
		String[] arguments = getOptionalData(processHandle.info()::arguments, new String[]{});
		System.out.println(indent+"pid="+pid+",parentPid="+parentPid+",alive="+alive+",command="+command+",commandLine="+commandLine+",arguments="+Arrays.toString(arguments));
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
