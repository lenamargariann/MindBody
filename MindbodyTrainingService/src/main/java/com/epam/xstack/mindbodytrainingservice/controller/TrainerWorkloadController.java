package com.epam.xstack.mindbodytrainingservice.controller;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkload;
import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;
import com.epam.xstack.mindbodytrainingservice.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workload")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TrainerWorkloadController {

    private final TrainerWorkloadService trainerWorkloadService;

    @GetMapping("/{username}")
    @ResponseStatus(value = HttpStatus.OK)
    public TrainerWorkload list(@PathVariable("username") String username) {
        return trainerWorkloadService.list(username);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public TrainerWorkload add(@RequestBody @Valid TrainerWorkloadRequest request) {
        return trainerWorkloadService.create(request);
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Valid TrainerWorkloadRequest request) {
        trainerWorkloadService.delete(request);
    }
}
