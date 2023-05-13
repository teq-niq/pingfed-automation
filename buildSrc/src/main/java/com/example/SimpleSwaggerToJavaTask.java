package com.example;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class SimpleSwaggerToJavaTask extends DefaultTask {
    @TaskAction
    public void doGenerate(){
        System.out.println("generating here");
    }
}
