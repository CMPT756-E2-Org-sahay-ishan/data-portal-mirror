package main.hallo.annotation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface AnnotationRepo extends JpaRepository<Annotation, Long> {
	@Query(nativeQuery =true,value="Select * from Annotations a where a.event_id=:eventId")
	List<Annotation> findByEventId(@Param("eventId") String eventId);
}