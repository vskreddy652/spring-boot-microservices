package com.eg2;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
 
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
 
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
 
//validatiion is not working
@Configuration
@ConfigurationProperties("cmdb")
class CmdbProperties1 {
 
    @NotEmpty
    private String resourceUrl;
 
    private List<Integer> resourcePort;
 
    @Max(5)
    @Min(0)
    private Integer resourceCount;
 
    @Override
    public String toString() {
 
        return "resourceUrl: "+ this.resourceUrl+"\n"
                + "resourcePort: "+this.resourcePort+"\n"
                + "resourceCount: "+this.resourceCount+"\n";
    }
 
    public String getResourceUrl() {
        return resourceUrl;
    }
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
    public List<Integer> getResourcePort() {
        return resourcePort;
    }
    public void setResourcePort(List<Integer> resourcePort) {
        this.resourcePort = resourcePort;
    }
 
    public Integer getResourceCount() {
        return resourceCount;
    }
}

@SpringBootApplication
public class MainApp_Validation
{
	  @PostConstruct
	  public void print() {
	    System.out.println("xxxxxxxxxxxxx:"+cmdbProperties.toString());
	  }
	  
	    @Autowired
	    private CmdbProperties1 cmdbProperties;
	  
    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(MainApp_Validation.class, args);
        
    }
}