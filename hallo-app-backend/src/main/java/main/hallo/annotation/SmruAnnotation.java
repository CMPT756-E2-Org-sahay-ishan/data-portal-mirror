package main.hallo.annotation;

import javax.persistence.*;

import main.hallo.smru.model.SmruSampleEvent;

@Entity
@DiscriminatorValue("SMRU_ANNOTATION")
public class SmruAnnotation extends Annotation {

    @Column(name = "filename")
    private String filename;
    
    private String manualAnnotation; 
    private String Annotator;
    // Add more fields as needed...

    // Constructors, getters, and setters...

    public SmruAnnotation() {
        // Default constructor
    }

    public SmruAnnotation(SmruSampleEvent sampleEvent, Float confidence, String filename) {
        super(sampleEvent, confidence);
        this.filename = filename;
    }

   

    public String getManualAnnotation() {
		return manualAnnotation;
	}

	public void setManualAnnotation(String manualAnnotation) {
		this.manualAnnotation = manualAnnotation;
	}

	public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

	public String getAnnotator() {
		return Annotator;
	}

	public void setAnnotator(String annotator) {
		Annotator = annotator;
	}
    
}

