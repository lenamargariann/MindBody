package com.epam.xstack.mindbodytrainingservice.model;

import lombok.Data;

@Data
public class TrainingDateProjection {
    private int year;
    private int month;
    private long totalDuration;

    public TrainingDateProjection(int year, int month, long totalDuration) {
        this.year = year;
        this.month = month;
        this.totalDuration = totalDuration;
    }

}
