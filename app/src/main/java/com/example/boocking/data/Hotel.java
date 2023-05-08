package com.example.boocking.data;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Hotel implements Serializable {
    private String name;
    private String id_hotel;
    private String id_manager;
    private String location;
    private Integer rating;

    public Hotel() {}

    public Hotel(String name, String id_hotel, String id_manager, String location, Integer rating) {
        this.name = name;
        this.id_hotel = id_hotel;
        this.id_manager = id_manager;
        this.location = location;
        this.rating = rating;
    }

    public Hotel(Hotel hotel) {
        this.name = hotel.getName();
        this.id_hotel = hotel.getId_hotel();
        this.id_manager = hotel.getId_manager();
        this.location = hotel.getLocation();
        this.rating = hotel.getRating();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_hotel() {
        return id_hotel;
    }

    public void setId_hotel(String id_hotel) {
        this.id_hotel = id_hotel;
    }

    public String getId_manager() {
        return id_manager;
    }

    public void setId_manager(String id_manager) {
        this.id_manager = id_manager;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " " + id_hotel + " " + id_manager + " " + location + " " + String.valueOf(rating);
    }
}
