package com.ant.assessment.simulation.controller;

import com.ant.assessment.simulation.service.FirebaseAuthService;
import com.google.cloud.firestore.DocumentReference;
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
@CrossOrigin(origins = "*") // adjust if needed for frontend domain
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");

            if (email == null || password == null || email.isBlank() || password.isBlank()) {
                return ResponseEntity.badRequest().body("Email or password is missing or empty");
            }

            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            // Use email prefix as userId for easier leaderboard linking
            String userId = email.split("@")[0];
            createUserDoc(email);

            Map<String, Object> response = new HashMap<>();
            response.put("uid", userId); // return the readable ID
            response.put("email", email);

            return ResponseEntity.ok(response);

        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public void createUserDoc(String email) {
        Firestore db = FirestoreClient.getFirestore();

        // Use email prefix (before "@") as document ID
        String docId = email.split("@")[0];

        Map<String, Object> userDoc = new HashMap<>();
        userDoc.put("user", email);
        userDoc.put("totalScore", 0);
        userDoc.put("createdAt", Instant.now().toString());
        userDoc.put("updatedAt", Instant.now().toString());

        db.collection("races").document(docId).set(userDoc); // ✅ use email prefix as doc ID
    }



    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");

            String token = firebaseAuthService.login(email, password);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> body) {
        try {
            String idToken = body.get("token");

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            Map<String, Object> response = new HashMap<>();
            response.put("uid", decodedToken.getUid());
            response.put("email", decodedToken.getEmail());
            response.put("name", decodedToken.getName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }


}
