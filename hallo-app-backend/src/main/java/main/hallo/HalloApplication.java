package main.hallo;

import java.io.File;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@SpringBootApplication
public class HalloApplication {

	public static void main(String[] args) {
		SpringApplication.run(HalloApplication.class, args);
	}

	
	   @PostConstruct
	    public void init(){
	      // Setting Spring Boot SetTimeZone
	      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	    }
}
