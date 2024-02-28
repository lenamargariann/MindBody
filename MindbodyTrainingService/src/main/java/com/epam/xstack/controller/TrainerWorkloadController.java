package com.epam.xstack.controller;

import com.epam.xstack.model.TrainerWorkload;
import com.epam.xstack.model.TrainerWorkloadMessage;
import com.epam.xstack.service.TrainerWorkloadService;
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
        return trainerWorkloadService.get(username);
    }


    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public TrainerWorkload add(@RequestBody @Valid TrainerWorkloadMessage request) {
        return trainerWorkloadService.create(request);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    public TrainerWorkload delete(@RequestBody @Valid TrainerWorkloadMessage request) {
        return trainerWorkloadService.delete(request);
    }


}
