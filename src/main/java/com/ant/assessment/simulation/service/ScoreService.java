package com.ant.assessment.simulation.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {

    public List<Map<String, Object>> getTopScores() throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection("users")
                .orderBy("totalScore", Query.Direction.DESCENDING)
                .limit(10)
                .get();

        List<Map<String, Object>> topUsers = new ArrayList<>();

        for (DocumentSnapshot doc : future.get().getDocuments()) {
            Map<String, Object> user = new HashMap<>();
            user.put("userId", doc.getId());
            user.put("totalScore", doc.getLong("totalScore"));
            topUsers.add(user);
        }

        return topUsers;
    }


    public void saveResult(String userId, List<String> finishOrder) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        // 1. Calculate score for this race
        int earnedScore = 0;
        for (int i = 0; i < finishOrder.size(); i++) {
            int points = Math.max(100 - (i * 10), 0);
            if (finishOrder.get(i).equalsIgnoreCase(userId)) {
                earnedScore = points;
                break;
            }
        }

        // 2. Fetch user's current score from Firestore
        DocumentReference userRef = db.collection("users").document(userId);
        DocumentSnapshot snapshot = userRef.get().get();

        int previousScore = 0;
        if (snapshot.exists() && snapshot.contains("totalScore")) {
            previousScore = snapshot.getLong("totalScore").intValue();
        }

        int updatedScore = previousScore + earnedScore;

        // 3. Save updated score back to Firestore
        Map<String, Object> userData = new HashMap<>();
        userData.put("totalScore", updatedScore);
        userRef.set(userData, SetOptions.merge());

        // 4. Save race result separately
        Map<String, Object> raceData = new HashMap<>();
        raceData.put("user", userId);
        raceData.put("finishOrder", finishOrder);
        raceData.put("earnedScore", earnedScore);
        raceData.put("timestamp", Instant.now().toString());

        db.collection("races").add(raceData);
    }
}
