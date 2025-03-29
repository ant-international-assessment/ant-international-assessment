package com.ant.assessment.simulation.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

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

    /**
     * Calculate race scores for a list of car names in order of finish
     */
    public Map<String, Integer> calculateScores(List<String> finishOrder) {
        Map<String, Integer> scoreMap = new LinkedHashMap<>();

        for (int i = 0; i < finishOrder.size(); i++) {
            String carName = finishOrder.get(i);
            int points = Math.max(100 - (i * 10), 0);
            scoreMap.put(carName, points);
        }

        return scoreMap;
    }

    /**
     * Update Firestore with race result for the user
     */
    public void saveResult(String userId, List<String> finishOrder, int earnedScore) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        // Step 1: Query all previous races to sum up total score
        int previousScore = 0;

        ApiFuture<QuerySnapshot> future = db.collection("races")
                .whereEqualTo("user", userId)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            Long earned = doc.getLong("totalScore");
            if (earned != null) {
                previousScore += earned;
            }
        }

        int updatedScore = previousScore + earnedScore;

        // Step 2: Save this race result (with totalScore snapshot)
        Map<String, Object> raceData = new HashMap<>();
        raceData.put("user", userId);
        raceData.put("finishOrder", finishOrder);
        raceData.put("earnedScore", earnedScore);
        raceData.put("totalScore", updatedScore); // ✅ store snapshot
        raceData.put("timestamp", Instant.now().toString());

        db.collection("races").document(userId).set(raceData);
    }


}
