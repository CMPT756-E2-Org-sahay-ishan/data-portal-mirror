package main.hallo.smru.model;

import java.math.BigInteger;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DETECTED")
public class SmruDetectedEvent extends SmruLimekiln{


    private Integer detectionCount;
    private String densestMinuteStartTime ;
    private Boolean recordingRequested;
    private Boolean recordingReceived ;
    private String detectionType;
    private BigInteger batchId;
    private Boolean hasAnnotation;

	public SmruDetectedEvent() {
		super();
		// TODO Auto-generated constructor stub
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
