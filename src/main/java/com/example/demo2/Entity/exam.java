package com.example.demo2.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "exam")
public class exam implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_name", unique = true, nullable = false)
    private String name;

    @Column(name = "duration")
    private int duration;



    @Column(name = "testcenter_id")
    private long testcenter_id;


    @Column(name = "available_date")
    private String availableDates;




    @Column(name = "location")
    private String location;




    public String getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(String availableDates) {
        this.availableDates = availableDates;
    }




    // Constructors, getters, and setters
    public exam() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTestcenter_id() {
        return testcenter_id;
    }

    public void setTestcenter_id(long testcenter_id) {
        this.testcenter_id = testcenter_id;
    }


}
