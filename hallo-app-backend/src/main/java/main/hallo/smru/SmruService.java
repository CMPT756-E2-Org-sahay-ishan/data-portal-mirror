package main.hallo.smru;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmruService {

	@Autowired
	SmruDetectionEventsRepo smruDetectionEventsRepo;
	
	public List<SmruDetectionEvents> smruEvents(){
		
		List<SmruDetectionEvents> result=smruDetectionEventsRepo.findAll();
		return result;
	}
}
