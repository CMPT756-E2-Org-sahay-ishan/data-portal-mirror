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
import main.hallo.annotation.MlModelAnnotation;
import main.hallo.annotation.SmruAnnotation;
import main.hallo.dto.AnnotationMLModelDto;
import main.hallo.dto.AnnotationSmruManualDto;

@RestController
@RequestMapping("/api/smru/label")
public class AnnotationController {

	@Autowired
	AnnotationService  annotationService;
	
	//################################################################Getting annotations associated to one event
	
	@GetMapping("/{eventId}")
		
		 public ResponseEntity<List<Annotation>> getAnnotationsByEventId(@PathVariable String eventId) {
        List<Annotation> annotations = annotationService.findAnnotationsByEventId(eventId);

        if (annotations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(annotations, HttpStatus.OK);
        }
		
	} 
 
    //##################################################### End point for posting manual annotation, such as by SMRU
    @PostMapping("/manual")
    public ResponseEntity<?> createManualDecision(@RequestBody AnnotationSmruManualDto annotationManual) {
    	try {
    	Annotation savedAnnotation = annotationService.saveManualAnnotation(annotationManual);
        return ResponseEntity.ok("Manual annotation created with ID: " + savedAnnotation.getAnnotationId());
    }
    	catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return new ResponseEntity<>("Failed to create annotation: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //######################################################## End point for posting ML model label
    @PostMapping("/ml")
    public ResponseEntity<?> createMLDecision(@RequestBody AnnotationMLModelDto annotationDto) {
    	System.out.println("This is a new annotation "+ annotationDto);
    	try {
         annotationService.saveMLAnnotation(annotationDto);
         
        return ResponseEntity.ok("Manual annotation created with ID: " + annotationDto.getEventId());
    }
    	catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return new ResponseEntity<>("Failed to create annotation: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //########################################################

}
