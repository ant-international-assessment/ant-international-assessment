package com.ant.assessment.simulation.controller;

import com.ant.assessment.simulation.component.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class CryptoController {

    @Autowired
    private RSAUtil rsaUtil;

    @GetMapping("/api/public-key")
    public ResponseEntity<String> getPublicKey() {
        return ResponseEntity.ok(rsaUtil.getPublicKey());
    }
}
