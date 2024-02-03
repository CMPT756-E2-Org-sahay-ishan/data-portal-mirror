package main.hallo.smru.repo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.hallo.dto.SmruEventsDto;
import main.hallo.smru.dto.SmruDto;
import main.hallo.smru.model.SmruDetectedEvent;
import main.hallo.smru.model.SmruSampleEvent;

@Repository
public interface SmruDetectionEventsRepo extends JpaRepository<SmruDetectedEvent, String>{

	@Query(nativeQuery = true, value="select idstring,date(to_timestamp(replace(replace(densestminutestarttime, 'T', ' '), 'Z', ''), 'YYYY-MM-DD HH24:MI:SS')), "
			+ "cast(to_timestamp(replace(replace(densestminutestarttime, 'T', ' '), 'Z', ''), 'YYYY-MM-DD HH24:MI:SS') as time) as time "
			+ "from smru_detection_events;")
	List<SmruDto> allDtoresults();
	
	
	//Return detected events between two time stamps
	@Query("SELECT se FROM SmruDetectedEvent se WHERE se.startTime BETWEEN :startTime1 AND :startTime2")
    List<SmruDetectedEvent> findAllByStartTimeBetween(@Param("startTime1") Timestamp startTime1, @Param("startTime2") Timestamp startTime2);
	
	//return all detected events
	 List<SmruDetectedEvent> findAll();

}
