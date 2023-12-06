package main.hallo.smru;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import main.hallo.annotation.Annotation;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "smru_sample_events", schema = "public")
public class SmruSampleEvent {

    @Id
    @Column(name = "event_id", nullable = false, length = 255)
    private String eventId;

    @Column(name = "alert_type", length = 255)
    private String alertType;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;
    @JsonIgnore
    @Column(name = "deployment_id", length = 255)
    private String deploymentId;
    @JsonIgnore
    @Column(name = "recording_id", length = 255)
    private String recordingId;
    @JsonIgnore
    @OneToMany(mappedBy = "sampleEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Annotation> annotations;

    // Constructors, getters, and setters...

    public SmruSampleEvent() {
        // Default constructor
    }

    public SmruSampleEvent(String eventId, String alertType, Timestamp startTime, Timestamp endTime,
                       String deploymentId, String recordingId, List<Annotation> annotations) {
        this.eventId = eventId;
        this.alertType = alertType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deploymentId = deploymentId;
        this.recordingId = recordingId;
        this.annotations = annotations;
    }

    // Other methods...

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
    
    

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getRecordingId() {
		return recordingId;
	}

	public void setRecordingId(String recordingId) {
		this.recordingId = recordingId;
	}

	@Override
	public String toString() {
		return "SmruSampleEvent [eventId=" + eventId + ", alertType=" + alertType + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", deploymentId=" + deploymentId + ", recordingId=" + recordingId
				+ ", annotations=" + annotations + "]";
	}
    

}