package main.hallo.smru.services;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.hallo.smru.model.SmruDetectedEvent;
import main.hallo.smru.repo.SmruDetectionEventsRepo;

@Service
public class SmruService {

	@Autowired
	SmruDetectionEventsRepo smruDetectionEventsRepo;
	

	//Return all detected events
	public List<SmruDetectedEvent> findAllDetectedEvents() {
		return smruDetectionEventsRepo.findAll();
		
	}

	//return all detected events between two time stamps
	public List<SmruDetectedEvent> findAllEventsBetweenStartTimes(Timestamp timestamp1, Timestamp timestamp2) {
		
		return smruDetectionEventsRepo.findAllByStartTimeBetween(timestamp1, timestamp2);
	}
}
