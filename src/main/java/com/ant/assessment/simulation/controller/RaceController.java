package com.ant.assessment.simulation.controller;

import com.ant.assessment.simulation.model.CarListRequest;
import com.ant.assessment.simulation.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RaceController {

    @Autowired
    private RaceService raceService;

    @PostMapping("/start-race")
    public ResponseEntity<String> startRace(@RequestBody CarListRequest request) {
        new Thread(() -> {
            try {
                raceService.startRace(request.getCars());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return ResponseEntity.ok("Race started");
    }

}