package com.jiasinchin.SDTPsin.controller.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class F3TestModel {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private F3 f3Controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(f3Controller).build();
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

        // Instantiate the F3 class with the mocked RestTemplate
        F3 f3 = new F3(restTemplate);

        // Call the method to be tested
        String result = f3.identifyStaffWithMostAdmissions();

        // Expected output
        String expectedOutput = "Staff with the most admissions - EmployeeID: 4, Employee Details: {\"forename\":\"Sarah\",\"surname\":\"Jones\",\"id\":4}";

        // Verify the result
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testNoStaffFound() throws Exception {
        // Mock responses from the Allocation API and Employee API
        String allocationResponse = "[{\"employeeID\": 3}, {\"employeeID\": 4}]";
        String employeeResponse = "[{\"id\": 1, \"name\": \"John Doe\"}, {\"id\": 2, \"name\": \"Jane Smith\"}]";
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations", String.class))
                .thenReturn(allocationResponse);
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/api/Employees", String.class))
                .thenReturn(employeeResponse);

        mockMvc.perform(get("/f3"))
                .andExpect(status().isOk())
                .andExpect(content().string("No staff found with the most admissions."));
    }

    @Test
    public void testFailedToFetchData() throws Exception {
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations", String.class))
                .thenThrow(new RuntimeException("Failed to fetch data from Allocation API"));
        when(restTemplate.getForObject("https://web.socem.plymouth.ac.uk/COMP2005/api/Employees", String.class))
                .thenReturn("[]");

        mockMvc.perform(get("/f3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Failed to fetch data from one or both APIs."));
    }
}
