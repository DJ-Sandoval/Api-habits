package com.app.api_habit.presentation.controller;

import com.app.api_habit.persistence.entities.Habit;
import com.app.api_habit.persistence.entities.Reminder;
import com.app.api_habit.presentation.dto.HabitStatDTO;
import com.app.api_habit.service.implementation.HabitServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@Tag(name = "Hábitos", description = "Endpoints para la gestión de hábitos")
public class HabitController {

    @Autowired
    private HabitServiceImp habitService;

    @Operation(
            summary = "Listar hábitos paginados",
            description = "Obtiene una lista paginada de todos los hábitos registrados"
    )
    @Parameter(name = "page", description = "Número de página (0-based)", example = "0")
    @Parameter(name = "size", description = "Tamaño de la página", example = "5")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de hábitos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = Page.class))
    )
    @GetMapping()
    public Page<Habit> listPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return habitService.listHabitsPage(page, size);
    }

    @Operation(
            summary = "Crear nuevo hábito",
            description = "Registra un nuevo hábito en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Hábito creado exitosamente",
            content = @Content(schema = @Schema(implementation = Habit.class))
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public Habit create(@RequestBody Habit habit) {
        return habitService.createHabit(habit);
    }

    @Operation(
            summary = "Actualizar hábito",
            description = "Actualiza la información de un hábito existente"
    )
    @Parameter(name = "id", description = "ID del hábito a actualizar", required = true, example = "1")
    @ApiResponse(
            responseCode = "200",
            description = "Hábito actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = Habit.class))
    )
    @ApiResponse(responseCode = "404", description = "Hábito no encontrado")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public Habit update(@PathVariable Long id, @RequestBody Habit habit) {
        return habitService.updateHabit(id, habit);
    }

    @Operation(
            summary = "Eliminar hábito",
            description = "Elimina un hábito del sistema"
    )
    @Parameter(name = "id", description = "ID del hábito a eliminar", required = true, example = "1")
    @ApiResponse(responseCode = "204", description = "Hábito eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Hábito no encontrado")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public void delete(@PathVariable Long id) {
        habitService.deleteHabit(id);
    }

    @Operation(
            summary = "Programar recordatorio",
            description = "Agrega un recordatorio para un hábito específico"
    )
    @Parameter(name = "id", description = "ID del hábito", required = true, example = "1")
    @ApiResponse(
            responseCode = "200",
            description = "Recordatorio creado exitosamente",
            content = @Content(schema = @Schema(implementation = Reminder.class))
    )
    @PostMapping("/{id}/reminders")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public Reminder remind(@PathVariable Long id, @RequestBody Reminder reminder) {
        return habitService.programReminder(id, reminder);
    }

    @Operation(
            summary = "Registrar progreso",
            description = "Registra el seguimiento/completado de un hábito en la fecha actual"
    )
    @Parameter(name = "id", description = "ID del hábito", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "Progreso registrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Hábito no encontrado")
    @PostMapping("/{id}/track")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public void track(@PathVariable Long id) {
        habitService.trackProgress(id);
    }

    @Operation(
            summary = "Obtener estadísticas",
            description = "Devuelve estadísticas de seguimiento para un hábito específico"
    )
    @Parameter(name = "id", description = "ID del hábito", required = true, example = "1")
    @ApiResponse(
            responseCode = "200",
            description = "Estadísticas obtenidas exitosamente",
            content = @Content(schema = @Schema(implementation = HabitStatDTO.class))
    )
    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public HabitStatDTO stats(@PathVariable Long id) {
        return habitService.getStats(id);
    }

    @Operation(
            summary = "Listar recompensas",
            description = "Obtiene la lista de recompensas disponibles según días de progreso"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de recompensas obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
    )
    @GetMapping("/rewards")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public List<String> rewards() {
        return habitService.getRewards();
    }

    @Operation(
            summary = "Reagendar hábito",
            description = "Reinicia o ajusta el calendario de un hábito específico"
    )
    @Parameter(name = "id", description = "ID del hábito", required = true, example = "1")
    @ApiResponse(
            responseCode = "200",
            description = "Hábito reagendado exitosamente",
            content = @Content(schema = @Schema(implementation = String.class))
    )
    @PostMapping("/{id}/reschedule")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public String reschedule(@PathVariable Long id) {
        return habitService.reschedule(id);
    }


}
