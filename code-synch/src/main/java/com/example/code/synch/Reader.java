package com.example.code.synch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Reader {

	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		File srcDir= new File(args[0]);
		
		String packageName=args[1];
		String[] packageFolders = packageName.split(Pattern.quote("."));
		File packageDir=srcDir;
		for (String string : packageFolders) {
			packageDir= new File(packageDir, string);
		}
		
		System.out.println("srcDir="+srcDir.getCanonicalPath());
		System.out.println("packageDir="+packageDir.getCanonicalPath());
		if(srcDir.exists() && srcDir.isDirectory())
		{
			deleteDirectory(srcDir);
		}
		if(!packageDir.exists())
		{
			packageDir.mkdirs();
		}
		String className="SynchedAutomationSharedConstants";
		try(PrintWriter pw= new PrintWriter(new FileWriter(new File(packageDir, className+".java")));)
		{
			pw.println("package "+packageName+";");
			pw.println("public class "+className+" {");
			Class c=com.example.config.AutomationSharedConstants.class;
			Field[] declaredFields = c.getDeclaredFields();
			for (Field field : declaredFields) {
				//for now assume all are strings
				String value = (String) field.get(null);
				pw.println("\tpublic static final  String "+field.getName()+"=\""+value+"\";");
				//System.out.println(field.getName()+" of type "+ field.getType().getName());
			}
			pw.println("}");
		}
		

	}
	
	static boolean  deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}

}
