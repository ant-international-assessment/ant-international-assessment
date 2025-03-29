package com.ant.assessment.simulation.controller;

import com.ant.assessment.simulation.model.CarListRequest;
import com.ant.assessment.simulation.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RaceController {

    @Autowired
    private RaceService raceService;

    @PostMapping("/start-race")
    public void startRace(@RequestBody CarListRequest request) throws InterruptedException {
        raceService.startRace(request.getCars());
    }

}