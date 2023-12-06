package main.hallo.annotation;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import main.hallo.smru.SmruSampleEvent;

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
    private SmruSampleEvent sampleEvent;

    @Column(name = "confidence")
    private String confidence;

    // Constructors, getters, and setters...

    public Annotation() {
        // Default constructor
    }

    public Annotation(SmruSampleEvent sampleEvent, String confidence) {
        this.sampleEvent = sampleEvent;
        this.confidence = confidence;
    }

    // Getters and Setters

    public Long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(Long annotationId) {
        this.annotationId = annotationId;
    }

    public SmruSampleEvent getSampleEvent() {
        return sampleEvent;
    }

    public void setSampleEvent(SmruSampleEvent sampleEvent) {
        this.sampleEvent = sampleEvent;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
}