package com.app.api_habit.service.interfaces;

import com.app.api_habit.persistence.entities.Habit;
import com.app.api_habit.persistence.entities.Reminder;
import com.app.api_habit.presentation.dto.HabitStatDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IHabitService {
    Page<Habit> listHabitsPage(int page, int size);
    Habit createHabit(Habit habit);
    Habit updateHabit(Long id, Habit habit);
    void deleteHabit(Long id);
    Reminder programReminder(Long habitId, Reminder reminder);
    void trackProgress(Long habitId);
    HabitStatDTO getStats(Long habitId);
    String getMotivationalPhrase();
    List<String> getRewards();
    String reschedule(Long habitId);
}
