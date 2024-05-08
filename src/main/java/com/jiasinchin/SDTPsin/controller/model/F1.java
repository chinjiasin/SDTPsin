package com.jiasinchin.SDTPsin.controller.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class F1 {

    final String patientApiUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Patients";
    final String admissionApiUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions";
    private final RestTemplate restTemplate;

    public F1(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/f1/{patientId}")
    public String getAdmissionsId(@PathVariable final String patientId) {
        StringBuilder result = new StringBuilder();

        try {
            // Get patient details
            JSONObject patientDetails = getPatientDetails(patientId);

            // Get admission details
            List<String> admissionDetails = getAdmissionDetails(patientId);

            // Build the result string with patient and admission details
            result.append("Patient Details:\n").append(patientDetails != null ? patientDetails.toString() : "Patient not found").append("\n\n");
            result.append("Admission Details:\n");
            for (String admission : admissionDetails) {
                result.append(admission).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private JSONObject getPatientDetails(String patientId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(patientApiUrl, String.class);
            JSONArray patientArray = new JSONArray(Objects.requireNonNull(response.getBody()));

            for (int i = 0; i < patientArray.length(); i++) {
                JSONObject patient = patientArray.getJSONObject(i);
                if (String.valueOf(patient.getInt("id")).equals(patientId)) {
                    return patient;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> getAdmissionDetails(String patientId) {
        List<String> admissionDetails = new ArrayList<>();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(admissionApiUrl, String.class);
            JSONArray admissionArray = new JSONArray(Objects.requireNonNull(response.getBody()));

            for (int i = 0; i < admissionArray.length(); i++) {
                JSONObject admission = admissionArray.getJSONObject(i);
                if (String.valueOf(admission.getInt("patientID")).equals(patientId)) {
                    admissionDetails.add(admission.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admissionDetails;
    }
}
