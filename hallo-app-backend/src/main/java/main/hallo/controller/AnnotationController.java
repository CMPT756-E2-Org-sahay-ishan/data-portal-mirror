package main.hallo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.hallo.annotation.Annotation;
import main.hallo.annotation.AnnotationService;
import main.hallo.dto.AnnotationDto;

@RestController
@RequestMapping("/api/annotations")
public class AnnotationController {

	@Autowired
	AnnotationService  annotationService;
	
	@GetMapping("/smru/{eventId}")
		
		 public ResponseEntity<List<Annotation>> getAnnotationsByEventId(@PathVariable String eventId) {
        List<Annotation> annotations = annotationService.findAnnotationsByEventId(eventId);

        if (annotations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(annotations, HttpStatus.OK);
        }
		
	} 
	//#################################################################
    @PostMapping("/smru/ml")
    public ResponseEntity<?> createAnnotation(@RequestBody AnnotationDto annotationDto) {
        try {
            // Assuming AnnotationDto is a DTO class representing the data from the request
            Annotation createdAnnotation = annotationService.createMLAnnotationForSampleEvents(annotationDto);
            // Assuming you want to return the created annotation in the response
            return new ResponseEntity<>(createdAnnotation, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return new ResponseEntity<>("Failed to create annotation: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
}
