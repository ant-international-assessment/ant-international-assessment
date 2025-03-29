package com.ant.assessment.simulation.service;

import com.google.api.client.util.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseAuthService {

//    @Value("${firebase.api-key}")
    private String firebaseApiKey = "AIzaSyB9iJ1mYNu_BiTUTaxowOw4kz1fDz1fgCE"; // need to setup on env

    private final RestTemplate restTemplate = new RestTemplate();

    public String login(String email, String password) {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey;

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("returnSecureToken", true);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
            return (String) response.getBody().get("idToken"); // Firebase ID token
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }
}
