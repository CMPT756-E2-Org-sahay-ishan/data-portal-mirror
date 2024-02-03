package main.hallo.dto;

public class AnnotationMLModelDto {
	private String eventId;
	private Float confidence;
	
	private boolean ifItIsOrca;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public Float getConfidence() {
		return confidence;
	}
	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}

	public boolean isIfItIsOrca() {
		return ifItIsOrca;
	}
	public void setIfItIsOrca(boolean ifItIsOrca) {
		this.ifItIsOrca = ifItIsOrca;
	}
	@Override
	public String toString() {
		return "AnnotationDto [eventId=" + eventId + ", confidence=" + confidence + 
				", ifItIsOrca=" + ifItIsOrca + "]";
	} 
	
	
}
