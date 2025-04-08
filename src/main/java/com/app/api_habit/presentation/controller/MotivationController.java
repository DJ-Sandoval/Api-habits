package com.app.api_habit.presentation.controller;

import com.app.api_habit.service.implementation.HabitServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/motivation")
@Tag(name = "Motivación", description = "Endpoints para frases motivacionales")
public class MotivationController {

    @Autowired
    private HabitServiceImp habitService;

    @Operation(
            summary = "Obtener frase motivacional",
            description = "Devuelve una frase aleatoria para motivar al usuario en su seguimiento de hábitos"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Frase motivacional obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = String.class))
    )
    @GetMapping
    public String getPhrase() {
        return habitService.getMotivationalPhrase();
    }
}