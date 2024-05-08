package com.jiasinchin.SDTPsin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class F3 extends JFrame {

    private final RestTemplate restTemplate;

    public F3(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        setTitle("F3 Staff Admissions");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Staff with Most Admissions:");
        JTextArea resultArea = new JTextArea(10, 30);
        JButton fetchButton = new JButton("Fetch Data");

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String allocationUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Allocations";
                String employeeUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api/Employees";

                try {
                    ResponseEntity<String> allocationResponseEntity = restTemplate.getForEntity(allocationUrl, String.class);
                    ResponseEntity<String> employeeResponseEntity = restTemplate.getForEntity(employeeUrl, String.class);

                    JSONArray allocationArray = new JSONArray(allocationResponseEntity.getBody());
                    JSONArray employeeArray = new JSONArray(employeeResponseEntity.getBody());

                    HashMap<Integer, Integer> employeeCountMap = new HashMap<>();
                    for (int i = 0; i < allocationArray.length(); i++) {
                        JSONObject allocation = allocationArray.getJSONObject(i);
                        if (allocation.has("employeeID")) {
                            int employeeID = allocation.getInt("employeeID");
                            employeeCountMap.put(employeeID, employeeCountMap.getOrDefault(employeeID, 0) + 1);
                        }
                    }

                    int maxEmployeeID = -1;
                    int maxCount = 0;
                    for (int employeeID : employeeCountMap.keySet()) {
                        int count = employeeCountMap.get(employeeID);
                        if (count > maxCount) {
                            maxCount = count;
                            maxEmployeeID = employeeID;
                        }
                    }

                    JSONObject staffDetails = null;
                    for (int i = 0; i < employeeArray.length(); i++) {
                        JSONObject employee = employeeArray.getJSONObject(i);
                        if (employee.has("id") && employee.getInt("id") == maxEmployeeID) {
                            staffDetails = employee;
                            break;
                        }
                    }

                    if (staffDetails != null) {
                        String output = "Staff with the most admissions:\nEmployeeID: " + maxEmployeeID + "\nEmployee Details:\n" +
                                "\"forename\":\"" + staffDetails.getString("forename") + "\"\n" +
                                "\"surname\":\"" + staffDetails.getString("surname") + "\"\n" +
                                "\"id\":" + staffDetails.getInt("id");
                        resultArea.setText(output);
                    } else {
                        resultArea.setText("No staff found with the most admissions.");
                    }
                } catch (org.json.JSONException ex) {
                    ex.printStackTrace();
                    resultArea.setText("Failed to parse JSON data.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    resultArea.setText("Failed to fetch data from one or both APIs.");
                }
            }
        });

        panel.add(label);
        panel.add(fetchButton);
        panel.add(resultArea);
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        new F3(restTemplate);
    }
}
