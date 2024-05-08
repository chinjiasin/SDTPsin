package com.jiasinchin.SDTPsin.controller.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

@RestController
public class F3 {

    public AbstractButton fetchButton;
    public Label resultArea;
    private RestTemplate restTemplate;

    @Autowired
    public F3(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/f3")
    public String identifyStaffWithMostAdmissions() {
        // URLs for Allocation API and Employee API
        String allocationUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations";
        String employeeUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Employees";

        try {
            // Fetch data from Allocation API
            String allocationResponse = restTemplate.getForObject(allocationUrl, String.class);
            JSONArray allocationArray = new JSONArray(allocationResponse);

            // Fetch data from Employee API
            String employeeResponse = restTemplate.getForObject(employeeUrl, String.class);
            JSONArray employeeArray = new JSONArray(employeeResponse);

            // Count occurrences of each employeeID
            HashMap<Integer, Integer> employeeCountMap = new HashMap<>();
            for (int i = 0; i < allocationArray.length(); i++) {
                JSONObject allocation = allocationArray.getJSONObject(i);
                if (allocation.has("employeeID")) {
                    int employeeID = allocation.getInt("employeeID");
                    employeeCountMap.put(employeeID, employeeCountMap.getOrDefault(employeeID, 0) + 1);
                }
            }

            // Find the employeeID with the maximum count
            int maxEmployeeID = -1;
            int maxCount = 0;
            for (int employeeID : employeeCountMap.keySet()) {
                int count = employeeCountMap.get(employeeID);
                if (count > maxCount) {
                    maxCount = count;
                    maxEmployeeID = employeeID;
                }
            }

            // Get employee details for the staff member with the most admissions
            JSONObject staffDetails = null;
            for (int i = 0; i < employeeArray.length(); i++) {
                JSONObject employee = employeeArray.getJSONObject(i);
                if (employee.has("id") && employee.getInt("id") == maxEmployeeID) {
                    staffDetails = employee;
                    break;
                }
            }

            if (staffDetails != null) {
                return "Staff with the most admissions - EmployeeID: " + maxEmployeeID + ", Employee Details: " + staffDetails.toString();
            } else {
                return "No staff found with the most admissions.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fetch data from one or both APIs.";
        }
    }

    public void setFetchButton(JButton fetchButton) {
    }
}
