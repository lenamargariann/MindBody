package com.epam.xstack.mindbodytrainingservice.controller;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkload;
import com.epam.xstack.mindbodytrainingservice.model.TrainingDTO;
import com.epam.xstack.mindbodytrainingservice.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService service;

    @GetMapping("/{username}")
    public ResponseEntity<?> list(@PathVariable("username") String username) {
        log.info("Creating training list request: {}", username);

        return service.list(username)
                .map((Function<TrainerWorkload, ResponseEntity<?>>) ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@NonNull @RequestHeader("auth") String header, @RequestBody TrainingDTO trainingDTO) {
        log.info("Creating training request: {}", trainingDTO.toString());
        Map<HttpStatus, String> response = service.add(header, trainingDTO);
        return ResponseEntity.status(response.keySet().stream().toList().get(0)).body(response.values().stream().toList().get(0));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@NonNull @RequestHeader("auth") String header, @RequestBody TrainingDTO trainingDTO) {
        log.info("Deleting training: {}", trainingDTO.toString());
        Map<HttpStatus, String> response = service.delete(header, trainingDTO);
        return ResponseEntity.status(response.keySet().stream().toList().get(0)).body(response.values().stream().toList().get(0));
    }
}
