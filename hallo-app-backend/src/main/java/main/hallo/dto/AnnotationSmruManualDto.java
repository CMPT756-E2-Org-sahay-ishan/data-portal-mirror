package main.hallo.dto;

public class AnnotationSmruManualDto {

	private String eventId;
	private String smruAnnotation;
	private Float confidence; 
	public AnnotationSmruManualDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AnnotationSmruManualDto(String eventId, String smruAnnotation) {
		super();
		this.eventId = eventId;
		this.smruAnnotation = smruAnnotation;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getSmruAnnotation() {
		return smruAnnotation;
	}
	public void setSmruAnnotation(String smruAnnotation) {
		this.smruAnnotation = smruAnnotation;
	}
	public Float getConfidence() {
		return confidence;
	}
	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	} 
	
	
	
}
