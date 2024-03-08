package main.hallo.smru.services;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.hallo.smru.model.SmruLimekiln;
import main.hallo.smru.repo.SmruLimekilnRepo;

@Service
@Transactional
public class SmruLimekilnService {
	 @Autowired
	 private SmruLimekilnRepo smruLimekilnRepo;
	 
	 //Find an SMRU event (random or detected) by Id
	 public SmruLimekiln getSmruLimekilnById(String eventId) {
		 return smruLimekilnRepo.findById(eventId).orElse(null); 
	 }
	 
	 //Save a new SMRU event 
	 public SmruLimekiln saveEvent(SmruLimekiln event) {
		 return smruLimekilnRepo.save(event);
	 }
	 
	 
	 
	
}
