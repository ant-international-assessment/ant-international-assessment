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

    @GetMapping("/leaderboard")
    public List<Map<String, Object>> getTopScores() throws Exception {
        return scoreService.getTopScores();
    }
}
