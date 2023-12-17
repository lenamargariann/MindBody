package com.epam.xstack.controller;

import com.epam.xstack.model.TrainingType;
import com.epam.xstack.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/training-type")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "TrainingTypeController", description = "Operations pertaining to training types")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping("/list")
    @Operation(summary = "List all training types", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<TrainingType> list() {
        return trainingTypeService.list();
    }
}
