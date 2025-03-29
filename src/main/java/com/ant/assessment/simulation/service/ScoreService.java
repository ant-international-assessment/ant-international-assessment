package com.ant.assessment.simulation.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScoreService {
    private final Map<String, Integer> scoreBoard = new ConcurrentHashMap<>();

    public void updateRanking(java.util.List<String> ranking) {
        for (int i = 0; i < ranking.size(); i++) {
            String carName = ranking.get(i);
            int points = Math.max(100 - (i * 10), 0);
            scoreBoard.merge(carName, points, Integer::sum);
        }
    }

    public Map<String, Integer> getAllScores() {
        return scoreBoard;
    }
}