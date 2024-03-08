package main.hallo.annotation;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.hallo.dto.AnnotationMLModelDto;
import main.hallo.dto.AnnotationSmruManualDto;
import main.hallo.smru.model.SmruLimekiln;
import main.hallo.smru.repo.SmruLimekilnRepo;
import main.hallo.smru.repo.SmruSampleEventRepo;

@Service
@Transactional
public class AnnotationService {
	@Autowired
	AnnotationRepo annotationRepo;
	@Autowired
	SmruSampleEventRepo smruSampleEventRepo;
	@Autowired
	SmruLimekilnRepo smruLimekilnRepo;

	// ####################################################### find annotation by
	// event Id
	public List<Annotation> findAnnotationsByEventId(String eventId) {
		return annotationRepo.findByEventId(eventId);
	}

	// ################################################## All Annotations
	public List<Annotation> findAnnotationsAll() {
		return annotationRepo.findAll();
	}

	// ##################################################
	public Annotation saveMLAnnotation(AnnotationMLModelDto annotationDTO) {

		// If event does not exists throw and exception
		SmruLimekiln event = smruSampleEventRepo.findById(annotationDTO.getEventId()).orElseThrow(
				() -> new RuntimeException("Sample Event not found with id: " + annotationDTO.getEventId()));

		// Create an ML annotation object
		MlModelAnnotation newAnnotation = new MlModelAnnotation();
		newAnnotation.setSmruLimekiln(event);
		newAnnotation.setConfidence(annotationDTO.getConfidence());
		newAnnotation.setOrcaFound(annotationDTO.isIfItIsOrca());
		// Setting time of annotation
		// Get the current time
		Instant currentUtcTime = Instant.now();

		// Convert Instant to Timestamp
		Timestamp timestamp = Timestamp.from(currentUtcTime);

		newAnnotation.setDate(timestamp);
		// Set other fields if necessary

		return annotationRepo.save(newAnnotation);
	}
	// #######################################################

	public Annotation saveManualAnnotation(AnnotationSmruManualDto annotationManual) {

		// If event does not exists throw and exception
		SmruLimekiln event = smruSampleEventRepo.findById(annotationManual.getEventId()).orElseThrow(
				() -> new RuntimeException("Sample Event not found with id: " + annotationManual.getEventId()));

		// Create an ML annotation object
		SmruAnnotation newAnnotation = new SmruAnnotation();
		newAnnotation.setSmruLimekiln(event);
		newAnnotation.setManualAnnotation(annotationManual.getSmruAnnotation());
		// Setting time of annotation
		// Get the current time
		Instant currentUtcTime = Instant.now();

		// Convert Instant to Timestamp
		Timestamp timestamp = Timestamp.from(currentUtcTime);

		newAnnotation.setDate(timestamp);
		// Set other fields if necessary

		return annotationRepo.save(newAnnotation);
	}

	// #######################################################
}
