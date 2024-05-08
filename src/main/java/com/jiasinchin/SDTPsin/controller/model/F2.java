package com.jiasinchin.SDTPsin.controller.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class F2 {

    @GetMapping("/f2")
    public List<String> getPatientsCurrentlyAdmitted() {
        List<String> output = new ArrayList<>();

        // URLs for Admission API and Patient API
        String admissionUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Admissions";
        String patientUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Patients";

        // RestTemplate for making HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Fetch data from Admission API
            String admissionResponse = restTemplate.getForObject(admissionUrl, String.class);
            JSONArray admissionArray = new JSONArray(admissionResponse);

            // Fetch data from Patient API
            String patientResponse = restTemplate.getForObject(patientUrl, String.class);
            JSONArray patientArray = new JSONArray(patientResponse);

            // Iterate over admission records to find currently admitted patients
            for (int i = 0; i < admissionArray.length(); i++) {
                JSONObject admission = admissionArray.getJSONObject(i);
                String dischargeDate = admission.getString("dischargeDate");

                // Check if the patient is currently admitted (discharge date is after current date or null)
                if (isCurrentlyAdmitted(dischargeDate)) {
                    // Find patient details
                    String patientDetail = findPatientDetails(patientArray, admission.getInt("patientID"));
                    output.add("Admission Detail: " + admission.toString() + ", Patient Detail: " + patientDetail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            output.add("Failed to fetch data from one or both APIs.");
        }

        return output;
    }

    // Helper method to check if the patient is currently admitted
    private boolean isCurrentlyAdmitted(String dischargeDate) {
        return dischargeDate.equals("0001-01-01T00:00:00") || dischargeDate.compareTo(getCurrentDateTime()) > 0;
    }

    // Helper method to get current date and time
    private String getCurrentDateTime() {
        // Implement logic to get current date and time
        return "2024-05-04T12:00:00";
    }

    // Helper method to find patient details by patient ID
    private String findPatientDetails(JSONArray patientArray, int patientID) throws JSONException {
        for (int i = 0; i < patientArray.length(); i++) {
            JSONObject patient = patientArray.getJSONObject(i);
            if (patient.getInt("id") == patientID) {
                return patient.toString();
            }
        }
        return null;
    }
}
