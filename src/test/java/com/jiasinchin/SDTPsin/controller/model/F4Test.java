package com.jiasinchin.SDTPsin.controller.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class F4Test {

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void testCheckAllocations() throws JSONException {
        F4 f4 = new F4(restTemplate);

        // Sample JSON data for mocked responses
        JSONArray allocationArray = new JSONArray()
                .put(new JSONObject().put("employeeID", 1))
                .put(new JSONObject().put("employeeID", 2));
        JSONArray employeeArray = new JSONArray()
                .put(new JSONObject().put("id", 1).put("name", "Employee 1"))
                .put(new JSONObject().put("id", 3).put("name", "Employee 3"));

        // Mocking responses
        when(restTemplate.getForEntity("https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations", String.class))
                .thenReturn(new ResponseEntity<>(allocationArray.toString(), HttpStatus.OK));
        when(restTemplate.getForEntity("https://web.socem.plymouth.ac.uk/COMP2005/api/Employees", String.class))
                .thenReturn(new ResponseEntity<>(employeeArray.toString(), HttpStatus.OK));

        // Performing the test
        String result = f4.checkAllocations();
        JSONArray expectedResult = new JSONArray().put(new JSONObject().put("id", 3).put("name", "Employee 3"));
        assertEquals(expectedResult.toString(), result);
    }
}
