package main.hallo.annotation;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import main.hallo.smru.model.SmruLimekiln;
import main.hallo.smru.model.SmruSampleEvent;

@Entity
@Table(name = "annotations", schema = "public")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "annotation_type", discriminatorType = DiscriminatorType.STRING)
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annotation_id")
    private Long annotationId;
    
    @JsonIgnore 
    @ManyToOne
    @JoinColumn(name = "event_id")
    private SmruLimekiln smruLimekiln;

    @Column(name = "confidence")
    private Float confidence;

    // Constructors, getters, and setters...
    
    private Timestamp date;  
    
    public Annotation() {
        // Default constructor
    }

    public Annotation(SmruLimekiln smruLimekiln, Float confidence) {
        this.smruLimekiln = smruLimekiln;
        this.confidence = confidence;
    }

    // Getters and Setters
    
    public Long getAnnotationId() {
        return annotationId;
    }

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public void setAnnotationId(Long annotationId) {
        this.annotationId = annotationId;
    }


    public SmruLimekiln getSmruLimekiln() {
		return smruLimekiln;
	}

	public void setSmruLimekiln(SmruLimekiln smruLimekiln) {
		this.smruLimekiln = smruLimekiln;
	}

	public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

	@Override
	public String toString() {
		return "Annotation [annotationId=" + annotationId + ", smruLimekiln=" + smruLimekiln + ", confidence="
				+ confidence + ", date=" + date + "]";
	}
    
}