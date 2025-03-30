package com.ant.assessment.simulation.controller;

import com.ant.assessment.simulation.component.RSAUtil;
import com.ant.assessment.simulation.model.CarListRequest;
import com.ant.assessment.simulation.service.RaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RaceController {

    @Autowired
    private RaceService raceService;

    @Autowired
    private RSAUtil rsaUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/start-race")
    public ResponseEntity<String> startRace(@RequestBody CarListRequest carListRequest) {
        try {
//            String encrypted = body.get("encrypted");
//            String decryptedJson = rsaUtil.decrypt(encrypted);
//            CarListRequest request = objectMapper.readValue(body, CarListRequest.class);

            new Thread(() -> {
                try {
                    raceService.startRace(carListRequest.getCars());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();

            return ResponseEntity.ok("Race started");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to decrypt or parse request");
        }
    }
}
