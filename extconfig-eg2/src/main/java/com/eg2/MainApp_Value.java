package com.eg2;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
 
@SpringBootApplication
public class MainApp_Value
{
	  @Value("${file.directory}")
	  private String fileDirectory;

	  @PostConstruct
	  public void print() {
	    System.out.println("wwwwwwwwwww:"+fileDirectory);
	  }
	  
    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(MainApp_Value.class, args);
        
    }
}