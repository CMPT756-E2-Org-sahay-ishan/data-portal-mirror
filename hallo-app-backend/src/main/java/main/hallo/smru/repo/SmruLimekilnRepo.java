package main.hallo.smru.repo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.hallo.smru.model.SmruLimekiln;

@Repository
public interface SmruLimekilnRepo  extends JpaRepository<SmruLimekiln, String>{
	
	//Query to filter all events (random and detected) between two time stamps 
	@Query(value="SELECT se FROM Smru_Limekiln se WHERE se.startTime BETWEEN :start AND :end AND se.dtype='RANDOM'",nativeQuery = true)
	List<SmruLimekiln> getRandomEventsBetweenStartAndEndTime
	(@Param("start") Timestamp start, @Param("end") Timestamp end);
	

}
