package com.jiasinchin.SDTPsin.model;

// Model class for Allocation
public class Allocation {
    private int id;
    private int admissionID;
    private int employeeID;
    private String startTime;
    private String endTime;

    public Allocation(int id, int admissionID, int employeeID, String startTime, String endTime) {
        this.id = id;
        this.admissionID = admissionID;
        this.employeeID = employeeID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getEmployeeID() {
        return employeeID;
    }
}
