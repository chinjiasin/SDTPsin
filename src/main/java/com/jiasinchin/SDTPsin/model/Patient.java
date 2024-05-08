package com.jiasinchin.SDTPsin.model;

public class Patient {
    private String id;
    private String name;
    // Other fields and methods

    @Override
    public String toString() {
        return "Patient ID: " + id + ", Name: " + name;
    }
}