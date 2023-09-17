package main.hallo.smru;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import main.hallo.dto.SmruEventsDto;

@Component
	public class SmruDetectionEventsDtoRepo{
	@Autowired
    EntityManager em;
	 public List<SmruEventsDto> findAllDateProcessedSmruEvents(){
		 String sql="select idstring,date(timezone('america/vancouver',(to_timestamp(densestminutestarttime, 'YYYY-MM-DDTHH:MI:SSZ')))), cast(timezone('america/vancouver',(to_timestamp(densestminutestarttime, 'YYYY-MM-DDTHH:MI:SSZ')) )as time) from  smru_detection_events;";
	      List<SmruEventsDto> test = em.createNativeQuery(sql).getResultList();
	      for (SmruEventsDto item:test) {System.out.println(item);}
	        return test;
	 }
	

}
