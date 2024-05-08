package com.jiasinchin.SDTPsin.controller.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class F2Test {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private F2 employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPatientsCurrentlyAdmitted() {
        // Mock admission API response
        String admissionResponse = "[{\"admissionDate\":\"2020-12-07T22:14:00\",\"patientID\":1,\"dischargeDate\":\"0001-01-01T00:00:00\",\"id\":2}, {\"admissionDate\":\"2024-02-23T21:50:00\",\"patientID\":5,\"dischargeDate\":\"2024-09-27T09:56:00\",\"id\":4}, {\"admissionDate\":\"2024-04-19T21:50:00\",\"patientID\":5,\"dischargeDate\":\"0001-01-01T00:00:00\",\"id\":6}]";
        // Mock patient API response
        String patientResponse = "[{\"forename\":\"Viv\",\"surname\":\"Robinson\",\"id\":1,\"nhsNumber\":\"1113335555\"}, {\"forename\":\"Rhi\",\"surname\":\"Sharpe\",\"id\":5,\"nhsNumber\":\"6663339999\"}]";

        // Set up mock behavior
        when(restTemplate.getForEntity(any(String.class), any(Class.class)))
                .thenReturn(ResponseEntity.ok().body(admissionResponse), ResponseEntity.ok().body(patientResponse));

        // Execute the method under test
        List<String> output = employeeController.getPatientsCurrentlyAdmitted();

        // Assert the output
        assertEquals(3, output.size());
        assertEquals("Admission Detail: {\"admissionDate\":\"2020-12-07T22:14:00\",\"patientID\":1,\"dischargeDate\":\"0001-01-01T00:00:00\",\"id\":2}, Patient Detail: {\"forename\":\"Viv\",\"surname\":\"Robinson\",\"id\":1,\"nhsNumber\":\"1113335555\"}", output.get(0));
        assertEquals("Admission Detail: {\"admissionDate\":\"2024-02-23T21:50:00\",\"patientID\":5,\"dischargeDate\":\"2024-09-27T09:56:00\",\"id\":4}, Patient Detail: {\"forename\":\"Rhi\",\"surname\":\"Sharpe\",\"id\":5,\"nhsNumber\":\"6663339999\"}", output.get(1));
        assertEquals("Admission Detail: {\"admissionDate\":\"2024-04-19T21:50:00\",\"patientID\":5,\"dischargeDate\":\"0001-01-01T00:00:00\",\"id\":6}, Patient Detail: {\"forename\":\"Rhi\",\"surname\":\"Sharpe\",\"id\":5,\"nhsNumber\":\"6663339999\"}", output.get(2));
    }
}