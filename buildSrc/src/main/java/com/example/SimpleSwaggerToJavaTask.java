package com.example;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.TaskAction;
import io.swagger.codegen.config.CodegenConfigurator;
import io.swagger.codegen.DefaultGenerator;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
public abstract class SimpleSwaggerToJavaTask extends DefaultTask {

    @InputFile
    public abstract RegularFileProperty getInputSpecs();

    @OutputDirectory
    public abstract  DirectoryProperty getTarget()  ;

    @TaskAction
    public void doGenerate(){
        String sr=getTarget().get().getAsFile().getAbsolutePath();
        System.out.println("sr="+sr);
        CodegenConfigurator config=new CodegenConfigurator();
        config.setInputSpec(getInputSpecs().get().getAsFile().getAbsolutePath());
        config.setLang("java");
        config.setLibrary("resttemplate");

        config.setModelPackage("com.example.pingfedadmin.model");
        config.setApiPackage("com.example.pingfedadmin.api");
        config.setInvokerPackage("com.example.pingfedadmin.invoker");
        config.setOutputDir(getTarget().get().getAsFile().getAbsolutePath());
        DefaultGenerator generator=new DefaultGenerator();
        generator.opts(config.toClientOptInput()).generate();
    }
}
