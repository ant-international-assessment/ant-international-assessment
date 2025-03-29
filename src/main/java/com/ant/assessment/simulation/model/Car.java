package com.ant.assessment.simulation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {
    private int id;
    private String name;
    private int position;
    private String status;
    private boolean isUser;
    private String username;

    public Car() {}

    public Car(int id, String name) {
        this.id = id;
        this.name = name;
        this.position = 0;
        this.status = "READY";
    }

    // Getters & Setters
}
