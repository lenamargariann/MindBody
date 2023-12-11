package com.epam.xstack.mindbodytrainingservice.controller;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;
import com.epam.xstack.mindbodytrainingservice.service.impl.TrainerWorkloadServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workload")
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadController {

    private final TrainerWorkloadServiceImpl trainerWorkloadService;


    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid TrainerWorkloadRequest request) {
        log.info("Creating workload request: {}", request.toString());
        if (trainerWorkloadService.create(request) == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody @Valid TrainerWorkloadRequest request) {
        log.info("Deleting workload: {}", request.toString());
        return trainerWorkloadService.delete(request) ? ResponseEntity.ok("Successfully deleted!")
                : ResponseEntity.badRequest().body("Something went wrong!");

    }
}
