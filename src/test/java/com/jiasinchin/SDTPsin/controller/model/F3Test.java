package com.jiasinchin.SDTPsin.controller.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class F3Test {

    private F3 f3;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        f3 = new F3(restTemplate);
    }

    @Test
    void identifyStaffWithMostAdmissions() {
        // Mock allocation API response
        String allocationResponse = "[{\"employeeID\": 1},{\"employeeID\": 1},{\"employeeID\": 2},{\"employeeID\": 3},{\"employeeID\": 4},{\"employeeID\": 4},{\"employeeID\": 4},{\"employeeID\": 5},{\"employeeID\": 6}]";
        // Mock employee API response
        String employeeResponse = "[{\"id\": 1,\"forename\": \"John\",\"surname\": \"Doe\"},{\"id\": 2,\"forename\": \"Alice\",\"surname\": \"Smith\"},{\"id\": 3,\"forename\": \"Sarah\",\"surname\": \"Jones\"},{\"id\": 4,\"forename\": \"Sarah\",\"surname\": \"Jones\"},{\"id\": 5,\"forename\": \"Patrick\",\"surname\": \"Wicks\"},{\"id\": 6,\"forename\": \"Alice\",\"surname\": \"Smith\"}]";

        // Mock restTemplate behavior
        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(allocationResponse)
                .thenReturn(employeeResponse);

        // Expected output
        String expectedOutput = "Staff with the most admissions - EmployeeID: 4, Employee Details: {\"forename\":\"Sarah\",\"surname\":\"Jones\",\"id\":4}";

        // Verify the result
        assertEquals(expectedOutput, f3.identifyStaffWithMostAdmissions());
    }

}
