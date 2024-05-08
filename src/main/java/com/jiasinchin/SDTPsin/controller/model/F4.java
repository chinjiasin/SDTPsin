package com.jiasinchin.SDTPsin.controller.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class F4{

    private final RestTemplate restTemplate;

    public F4(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/f4")
    public String checkAllocations() {
        String allocationUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations";
        String employeeUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Employees";

        try {
            JSONArray allocationArray = fetchJsonArray(allocationUrl);
            JSONArray employeeArray = fetchJsonArray(employeeUrl);

            JSONArray unallocatedEmployees = new JSONArray();
            for (int i = 0; i < employeeArray.length(); i++) {
                JSONObject employee = employeeArray.getJSONObject(i);
                int employeeID = employee.getInt("id");
                boolean found = false;
                for (int j = 0; j < allocationArray.length(); j++) {
                    JSONObject allocation = allocationArray.getJSONObject(j);
                    int allocationEmployeeID = allocation.getInt("employeeID");
                    if (allocationEmployeeID == employeeID) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    unallocatedEmployees.put(employee);
                }
            }

            return unallocatedEmployees.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fetch data from one or both APIs.";
        }
    }

    private JSONArray fetchJsonArray(String url) throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return new JSONArray(responseEntity.getBody());
        } else {
            throw new RuntimeException("Failed to fetch data from API. Status code: " + responseEntity.getStatusCode());
        }
    }
}
