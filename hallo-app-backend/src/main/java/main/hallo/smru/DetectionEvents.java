package main.hallo.smru;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DetectionEvents {
	@Id
	private String idstring;
	
    private String alertType;
    private String startTime;
    private String endTime;
    private String deploymentIdString ;
    private String recordingIdString;
    private Integer detectionCount;
    private String densestMinuteStartTime ;
    private Boolean recordingRequested;
    private Boolean recordingReceived ;
    private String detectionType;
    private BigInteger batchId;
    private Boolean hasAnnotation;
    
    
    
    
	public String getIdstring() {
		return idstring;
	}
	public void setIdstring(String idstring) {
		this.idstring = idstring;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDeploymentIdString() {
		return deploymentIdString;
	}
	public void setDeploymentIdString(String deploymentIdString) {
		this.deploymentIdString = deploymentIdString;
	}
	public String getRecordingIdString() {
		return recordingIdString;
	}
	public void setRecordingIdString(String recordingIdString) {
		this.recordingIdString = recordingIdString;
	}
	public Integer getDetectionCount() {
		return detectionCount;
	}
	public void setDetectionCount(Integer detectionCount) {
		this.detectionCount = detectionCount;
	}
	public String getDensestMinuteStartTime() {
		return densestMinuteStartTime;
	}
	public void setDensestMinuteStartTime(String densestMinuteStartTime) {
		this.densestMinuteStartTime = densestMinuteStartTime;
	}
	public Boolean getRecordingRequested() {
		return recordingRequested;
	}
	public void setRecordingRequested(Boolean recordingRequested) {
		this.recordingRequested = recordingRequested;
	}
	public Boolean getRecordingReceived() {
		return recordingReceived;
	}
	public void setRecordingReceived(Boolean recordingReceived) {
		this.recordingReceived = recordingReceived;
	}
	public String getDetectionType() {
		return detectionType;
	}
	public void setDetectionType(String detectionType) {
		this.detectionType = detectionType;
	}
	public BigInteger getBatchId() {
		return batchId;
	}
	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
	}
	public Boolean getHasAnnotation() {
		return hasAnnotation;
	}
	public void setHasAnnotation(Boolean hasAnnotation) {
		this.hasAnnotation = hasAnnotation;
	}
    
    
    
    
}
