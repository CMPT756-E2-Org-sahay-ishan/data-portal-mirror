package main.hallo.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.hallo.smru.DetectionEvents;
import main.hallo.smru.SmruDetectionEventsRepo;
import main.hallo.smru.SmruService;



@RestController
@RequestMapping("/api/smru")
public class SMRUController {
@Autowired
SmruService smruService;

	@GetMapping("")
	public String
	getAudio(){
		return "Hello World!";	
	}
	
	@GetMapping("/events/all")
	public ResponseEntity<?>
	//When this API gets behind the filter, this part needs to be commented out
	//getAudios(@AuthenticationPrincipal main.hallo.user.User user)
	getAllEvents(){

		List<DetectionEvents> allEvents=smruService.smruEvents();
		return ResponseEntity.ok(allEvents);
	}
	
}
