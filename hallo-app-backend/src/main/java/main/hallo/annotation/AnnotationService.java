package main.hallo.annotation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.hallo.dto.AnnotationDto;
import main.hallo.smru.SmruSampleEvent;
import main.hallo.smru.SmruSampleEventRepo;

@Service
public class AnnotationService {
@Autowired
AnnotationRepo annotationRepo;
@Autowired
SmruSampleEventRepo smruSampleEventRepo;	
	
    public List<Annotation> findAnnotationsByEventId(String eventId) {
        return annotationRepo.findByEventId(eventId);
    }
    
    public Annotation createMLAnnotationForSampleEvents( AnnotationDto annotationDTO) {
        SmruSampleEvent sampleEvent = smruSampleEventRepo.findById(annotationDTO.getEventId())
                .orElseThrow(() -> new RuntimeException("Sample Event not found with id: " + annotationDTO.getEventId()));

        // Create a new Annotation entity
        Annotation newAnnotation = new MlModelAnnotation();
        newAnnotation.setSampleEvent(sampleEvent);
        newAnnotation.setConfidence(annotationDTO.getConfidence());
        // Set other fields if necessary

        // Save the new Annotation to the database
        return annotationRepo.save(newAnnotation);
    }
}
