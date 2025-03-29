package com.ant.assessment.simulation.model;

import lombok.Data;

@Data
public class Car {
    private int id;
    private String name;
    private int position;
    private String status;

    public Car() {}

    public Car(int id, String name) {
        this.id = id;
        this.name = name;
        this.position = 0;
        this.status = "READY";
    }

    // Getters & Setters
}
