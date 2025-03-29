package com.ant.assessment.simulation.controller;

import com.ant.assessment.simulation.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // or specify your frontend domain
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping("/update-score")
    public void updateScore(@RequestBody Map<String, Object> body) throws Exception {
        String userId = (String) body.get("userId");
        List<String> finishOrder = (List<String>) body.get("finishOrder");
        scoreService.saveResult(userId, finishOrder);
    }

    @GetMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderboard() throws Exception {
        return scoreService.getTopScores();
    }
}
