package main.hallo.dto;

public class AnnotationDto {
	private String eventId;
	private String confidence;
	private String annotator;
	private boolean ifItIsOrca;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getConfidence() {
		return confidence;
	}
	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}
	public String getAnnotator() {
		return annotator;
	}
	public void setAnnotator(String annotator) {
		this.annotator = annotator;
	}
	public boolean isIfItIsOrca() {
		return ifItIsOrca;
	}
	public void setIfItIsOrca(boolean ifItIsOrca) {
		this.ifItIsOrca = ifItIsOrca;
	}
	@Override
	public String toString() {
		return "AnnotationDto [eventId=" + eventId + ", confidence=" + confidence + ", annotator=" + annotator
				+ ", ifItIsOrca=" + ifItIsOrca + "]";
	} 
	
	
}
