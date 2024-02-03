package main.hallo.smru.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import main.hallo.annotation.Annotation;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EVENT_TYPE") 
@Table(name = "smru_limekiln", schema = "public")
public class SmruLimekiln {
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
	@OneToMany(mappedBy = "smruLimekiln", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Annotation> annotations = new ArrayList<>();

	public SmruLimekiln() {
		super();
		
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


	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}
	
	

}
