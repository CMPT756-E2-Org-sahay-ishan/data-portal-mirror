package main.hallo.smru.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.hallo.annotation.Annotation;
import main.hallo.smru.model.SmruSampleEvent;
import main.hallo.smru.repo.SmruSampleEventRepo;

@Service
public class SmruSampleEventService {
	@Autowired
	SmruSampleEventRepo smruSampleEventRepo;  

	 

	    public List<Annotation> getAnnotationsByEventId(String eventId) {
	    	SmruSampleEvent sampleEvent = smruSampleEventRepo.findById(eventId)
	                .orElseThrow(() -> new RuntimeException("SampleEvent not found with id: " + eventId));

	        return sampleEvent.getAnnotations();
	    }
	    
	    //########################################################### Filter sample events
	    public List<SmruSampleEvent> findAllEventsBetweenStartTimes(Timestamp startTime1, Timestamp startTime2) {

	   
	        return smruSampleEventRepo.findAllByStartTimeBetween(startTime1, startTime2);
	    }
	    
	    //############################################################ all sample events
	    public List<SmruSampleEvent> findAllSampleEvents(){ 
	    	List<SmruSampleEvent> result=smruSampleEventRepo.findAll();	    
	    	return smruSampleEventRepo.findAll();
	    }
	    //############################################################
}
