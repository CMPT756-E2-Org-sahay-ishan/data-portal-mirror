package main.hallo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.http.MediaType;

import main.hallo.dto.SmruEventsDto;
import main.hallo.smru.SmruDetectionEvents;
import main.hallo.smru.SmruDetectionEventsDtoRepo;
import main.hallo.smru.SmruDetectionEventsRepo;
import main.hallo.smru.SmruDto;
import main.hallo.smru.SmruService;


@RestController
@RequestMapping("/api/smru")
public class SMRUController {
@Autowired
SmruService smruService;


@Autowired
SmruDetectionEventsDtoRepo smruRepo;
@Autowired
SmruDetectionEventsRepo smruMainRepo;

@Value("${smruAudioPath}")
private String smruAudioFolder;


	@GetMapping("")
	public String
	getAudio(){
		return "Hello World!";	
	}
	
	@GetMapping("/events/all")
	public ResponseEntity<?>
	//When this API gets behind the filter, this part needs to be commented out
	//getAllEvents(){
	getAllEvents(@AuthenticationPrincipal main.hallo.user.User user){
	System.out.println("This is the user " +user); 

		List<SmruDetectionEvents> allEvents=smruService.smruEvents();
		return ResponseEntity.ok(allEvents);
	}
	
	@GetMapping("/audio/{stringId}")
	public ResponseEntity<?>
		
	//When this API gets behind the filter, this part needs to be commented out
	//getAudios(@AuthenticationPrincipal main.hallo.user.User user)
	getAudioForOneEvent(@PathVariable String stringId){
		
        File file = new File(smruAudioFolder + "/" + stringId+".wav");
       
        if (!file.exists()) {
        	System.out.println("File not exists");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
            else {
            	System.out.println("File exists");
                InputStreamResource resource;
				try {
					resource = new InputStreamResource(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}

                return ResponseEntity.ok()
                        // Content-Disposition
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                        // Content-Type
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        // Content-Length
                        .contentLength(file.length()) //
                        .body(resource);
            
        }

	}
	
	//####################################### Dwonload multiple audio files in the fomrat of zip
	
	@GetMapping(path = "/audio/downloads/all")
	public ResponseEntity<StreamingResponseBody> downloadZip(HttpServletResponse response) {



		// list of file paths for download
		List<String> paths = Arrays.asList(smruAudioFolder + "/" + "64bf4af7c8fe6a06228ebc56.wav",
				smruAudioFolder + "/" + "64bf4af7c8fe6a06228ebc57.wav");

		int BUFFER_SIZE = 1024;

		StreamingResponseBody streamResponseBody = out -> {

			final ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
			ZipEntry zipEntry = null;
			InputStream inputStream = null;

			try {
				for (String path : paths) {
					File file = new File(path);
					zipEntry = new ZipEntry(file.getName());

					inputStream = new FileInputStream(file);

					zipOutputStream.putNextEntry(zipEntry);
					byte[] bytes = new byte[BUFFER_SIZE];
					int length;
					while ((length = inputStream.read(bytes)) >= 0) {
						zipOutputStream.write(bytes, 0, length);
					}

				}
				// set zip size in response
				response.setContentLength((int) (zipEntry != null ? zipEntry.getSize() : 0));
			} catch (IOException e) {
				
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
				if (zipOutputStream != null) {
					zipOutputStream.close();
				}
			}

		};

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=example.zip");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");

		return ResponseEntity.ok(streamResponseBody);
	}
	
	
	@GetMapping(path = "/all")
	public List<SmruDto> findAllEvents() {
		//return (List<SmruEventsDto>) smruRepo.findAllDateProcessedSmruEvents();smruMainRepo
		List<SmruDto> result=smruMainRepo.allDtoresults();
		for (SmruDto item:result) {System.out.println(item.getTime());}
		return result;
	}
}
