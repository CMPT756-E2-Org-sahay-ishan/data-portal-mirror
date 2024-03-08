package main.hallo.smru.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import main.hallo.annotation.Annotation;

import java.sql.Timestamp;
import java.util.List;

@Entity
@DiscriminatorValue("RANDOM")
public class SmruSampleEvent extends SmruLimekiln{

 // Constructor

    public SmruSampleEvent() {
        super();
    }
    

}