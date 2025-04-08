package com.app.api_habit.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "habits")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String frequency;
    private LocalDate startDate;
    private int goalDays;
    private int currentStreak;
    private int completedDays;
}
