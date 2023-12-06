package main.hallo.smru;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface SmruSampleEventRepo extends JpaRepository<SmruSampleEvent, String> {
	
    //@Query("SELECT se FROM SmruSampleEvent se WHERE se.startTime BETWEEN :startTime1 AND :startTime2")
    
	@Query("SELECT se FROM SmruSampleEvent se WHERE se.startTime BETWEEN :startTime1 AND :startTime2")
    List<SmruSampleEvent> findAllByStartTimeBetween(@Param("startTime1") Timestamp startTime1, @Param("startTime2") Timestamp startTime2);
 
    List<SmruSampleEvent> findAll();

}
