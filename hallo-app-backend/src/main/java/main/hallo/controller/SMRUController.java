package main.hallo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
//import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.http.MediaType;
import main.hallo.smru.dto.SmruDto;
import main.hallo.smru.model.SmruDetectedEvent;
import main.hallo.smru.model.SmruLimekiln;
import main.hallo.smru.model.SmruSampleEvent;
import main.hallo.smru.repo.SmruDetectionEventsRepo;
import main.hallo.smru.services.SmruLimekilnService;
import main.hallo.smru.services.SmruSampleEventService;
import main.hallo.smru.services.SmruService;


@RestController
@RequestMapping("/api/smru")
public class SMRUController {

	@Autowired
	SmruLimekilnService smruLimekilnService;	
	
@Autowired
SmruService smruDetectedEventService;

@Autowired
SmruSampleEventService smruSampleEventService;



@Autowired
SmruDetectionEventsRepo smruMainRepo;

@Value("${smruAudioPath}")
private String smruAudioFolder;

@Value("${smruSampleAudioPath}")
private String smruSampleAudioFolder;


	
	@GetMapping("/audio/{stringId}")
	public ResponseEntity<?>
		
	//When this API gets behind the filter, this part needs to be commented out
	//getAudios(@AuthenticationPrincipal main.hallo.user.User user)
	getAudioForOneEvent(@PathVariable String stringId ){
		
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
	
	//####################################### Download multiple audio files in the format of zip
	
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
	
	////Getting all  smru data for test
	@GetMapping(path = "/all")
	public List<SmruDto> findAllEvents() {
		List<SmruDto> result=smruMainRepo.allDtoresults();
		return result;

	}
	
	////////////////////////////////////////////////////////////////
		//Sample Events Controller
	///////////////////////////////////////////////////////////////
	
	  @GetMapping("/sampleEvent/audio/{eventId}")
	  public ResponseEntity<?> getAudioForOneSampleEvent(@PathVariable String eventId){
			
	        File file = new File(smruSampleAudioFolder + "/" + eventId+".wav");
	       System.out.println("This is the details of the file "+ file );
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

	    
	    //################################################################## return detected events between two time stamps
	    @GetMapping("/detectedEvent")
	    public ResponseEntity<List<SmruDetectedEvent>> getDetectedEventsBetweenStartTimes(
	            @RequestParam String startTime1,
	            @RequestParam String startTime2) {

	    	//Creating timestamps from inputs
	    	Timestamp timestamp1 = Timestamp.valueOf(startTime1);
	    	Timestamp timestamp2 = Timestamp.valueOf(startTime2);

	    	return ResponseEntity.ok(smruDetectedEventService.findAllEventsBetweenStartTimes(timestamp1, timestamp2));
	    }
	  
	  //################################################################## return detected sample between two time stamps
	    @GetMapping("/sampleEvent")
	    public ResponseEntity<List<SmruSampleEvent>> getSampleEventsBetweenStartTimes(
	            @RequestParam String startTime1,
	            @RequestParam String startTime2) {

	    	//Creating timestamps from inputs
	    	Timestamp timestamp1 = Timestamp.valueOf(startTime1);
	    	Timestamp timestamp2 = Timestamp.valueOf(startTime2);

	    	return ResponseEntity.ok(smruSampleEventService.findAllEventsBetweenStartTimes(timestamp1, timestamp2));
	    }
	       
	    
	    //################################################################## return all random events

	    @GetMapping("/sampleEvent/all")
	    public ResponseEntity<List<SmruSampleEvent>> getEventsSampleAll(@AuthenticationPrincipal main.hallo.user.User user)
	           {
	    	return ResponseEntity.ok(smruSampleEventService.findAllSampleEvents());
	    	
	   
	    }
	  //################################################################## return all detected events
	    @GetMapping("/detectedEvent/all")
	    public ResponseEntity<List<SmruDetectedEvent>> getDetectedSampleAll()
	           {
	    	return ResponseEntity.ok(smruDetectedEventService.findAllDetectedEvents());
	   
	    }
}
