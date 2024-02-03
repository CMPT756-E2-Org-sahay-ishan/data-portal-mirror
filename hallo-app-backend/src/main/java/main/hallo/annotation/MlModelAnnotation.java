package main.hallo.annotation;
import javax.persistence.*;

import main.hallo.smru.model.SmruSampleEvent;

@Entity
@DiscriminatorValue("ML_MODEL")
public class MlModelAnnotation extends Annotation{

    @Column(name = "is_orca_found")
    private boolean isOrcaFound;

    // Add more fields as needed

    // Constructors, getters, and setters

    public MlModelAnnotation() {
        // Default constructor
    }

    public MlModelAnnotation(SmruSampleEvent sampleEvent, Float confidence, boolean isOrcaFound) {
        super(sampleEvent, confidence);
        this.isOrcaFound = isOrcaFound;
    }


    public boolean isOrcaFound() {
        return isOrcaFound;
    }

    public void setOrcaFound(boolean orcaFound) {
        isOrcaFound = orcaFound;
    }
}
