package main.hallo.smru;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmruDetectionEventsRepo extends JpaRepository<DetectionEvents, String>{

	
}
