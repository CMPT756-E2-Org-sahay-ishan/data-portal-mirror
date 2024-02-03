package main.hallo.smru.repo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.hallo.smru.model.SmruSampleEvent;
@Repository
public interface SmruSampleEventRepo extends JpaRepository<SmruSampleEvent, String> {
	
	//Finding random events between two time stamps 
	@Query("SELECT se FROM SmruSampleEvent se WHERE se.startTime BETWEEN :startTime1 AND :startTime2")
    List<SmruSampleEvent> findAllByStartTimeBetween(@Param("startTime1") Timestamp startTime1, @Param("startTime2") Timestamp startTime2);
 
	//Finding all random events
    List<SmruSampleEvent> findAll();

}
