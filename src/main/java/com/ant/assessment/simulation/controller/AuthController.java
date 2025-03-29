package com.ant.assessment.simulation.controller;

import com.ant.assessment.simulation.component.RSAUtil;
import com.ant.assessment.simulation.service.FirebaseAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @Autowired
    private RSAUtil rsaUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        try {
            String encrypted = body.get("encrypted");
            String decryptedJson = rsaUtil.decrypt(encrypted);
            Map<String, String> decrypted = objectMapper.readValue(decryptedJson, Map.class);

            String email = decrypted.get("email");
            String password = decrypted.get("password");

            if (email == null || password == null || email.isBlank() || password.isBlank()) {
                return ResponseEntity.badRequest().body("Email or password is missing or empty");
            }

            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            String userId = email.split("@")[0];
            createUserDoc(email);

            Map<String, Object> response = new HashMap<>();
            response.put("uid", userId);
            response.put("email", email);

            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to decrypt or parse request");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String encrypted = body.get("encrypted");
            String decryptedJson = rsaUtil.decrypt(encrypted);
            Map<String, String> decrypted = objectMapper.readValue(decryptedJson, Map.class);

            String email = decrypted.get("email");
            String password = decrypted.get("password");

            String token = firebaseAuthService.login(email, password);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to decrypt or authenticate");
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("token");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("uid", decodedToken.getUid());
            response.put("email", decodedToken.getEmail());
            response.put("name", decodedToken.getName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    private void createUserDoc(String email) {
        Firestore db = FirestoreClient.getFirestore();
        String docId = email.split("@")[0];

        Map<String, Object> userDoc = new HashMap<>();
        userDoc.put("user", email);
        userDoc.put("totalScore", 0);
        userDoc.put("createdAt", Instant.now().toString());
        userDoc.put("updatedAt", Instant.now().toString());

        db.collection("races").document(docId).set(userDoc);
    }
}
