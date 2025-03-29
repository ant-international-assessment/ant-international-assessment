package com.ant.assessment.simulation.controller;

import com.ant.assessment.simulation.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping("/update-score")
    public void updateScore(@RequestBody List<String> ranking) {
        scoreService.updateRanking(ranking);
    }

    @GetMapping("/scores")
    public Map<String, Integer> getScores() {
        return scoreService.getAllScores();
    }
}