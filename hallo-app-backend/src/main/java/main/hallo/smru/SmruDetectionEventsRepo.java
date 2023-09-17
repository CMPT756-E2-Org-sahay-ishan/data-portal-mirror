package main.hallo.smru;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.hallo.dto.SmruEventsDto;

@Repository
public interface SmruDetectionEventsRepo extends JpaRepository<SmruDetectionEvents, String>{
	//Original query
	//@Query(nativeQuery = true, value="select idstring,date(timezone('america/vancouver',(to_timestamp(densestminutestarttime, 'YYYY-MM-DDTHH:MI:SSZ')))), cast(timezone('america/vancouver',(to_timestamp(densestminutestarttime, 'YYYY-MM-DDTHH:MI:SSZ')) )as time)  as time from  smru_detection_events;")
	@Query(nativeQuery = true, value="select idstring,date(to_timestamp(replace(replace(densestminutestarttime, 'T', ' '), 'Z', ''), 'YYYY-MM-DD HH24:MI:SS')), "
			+ "cast(to_timestamp(replace(replace(densestminutestarttime, 'T', ' '), 'Z', ''), 'YYYY-MM-DD HH24:MI:SS') as time) as time "
			+ "from smru_detection_events;")
	List<SmruDto> allDtoresults();
}
