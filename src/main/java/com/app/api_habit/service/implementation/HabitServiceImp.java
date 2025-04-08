package com.app.api_habit.service.implementation;

import com.app.api_habit.persistence.entities.Habit;
import com.app.api_habit.persistence.entities.MotivationPhrase;
import com.app.api_habit.persistence.entities.Reminder;
import com.app.api_habit.persistence.repository.HabitRepository;
import com.app.api_habit.presentation.dto.HabitStatDTO;
import com.app.api_habit.service.exception.ResourceNotFoundException;
import com.app.api_habit.service.interfaces.IHabitService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class HabitServiceImp implements IHabitService {
    // Creamos repo con mapas
    private final Map<Long, Habit> habits = new HashMap<>();
    private final List<MotivationPhrase> phrases = List.of(
            new MotivationPhrase(null, "Sigue asi!"),
            new MotivationPhrase(null, "Ya casi lo logras"),
            new MotivationPhrase(null, "Llevas varios dias seguidos"),
            new MotivationPhrase(null, "¡Cada día cuenta, sigue adelante!"),
            new MotivationPhrase(null, "La constancia es la clave del éxito"),
            new MotivationPhrase(null, "¡Increíble progreso, no pares ahora!"),
            new MotivationPhrase(null, "Pequeños pasos llevan a grandes logros"),
            new MotivationPhrase(null, "¡Tu esfuerzo hoy es tu recompensa mañana!"),
            new MotivationPhrase(null, "Vas por buen camino, ¡continúa!"),
            new MotivationPhrase(null, "La disciplina supera al talento"),
            new MotivationPhrase(null, "¡No rompas la racha, tú puedes!"),
            new MotivationPhrase(null, "Cada día eres más fuerte que ayer"),
            new MotivationPhrase(null, "El éxito es la suma de pequeños esfuerzos"),
            new MotivationPhrase(null, "¡Lo estás haciendo genial!"),
            new MotivationPhrase(null, "La motivación te trajo aquí, el hábito te mantendrá"),
            new MotivationPhrase(null, "¡Mira cuánto has avanzado!"),
            new MotivationPhrase(null, "El hábito se está convirtiendo en parte de ti"),
            new MotivationPhrase(null, "¡Eres imparable cuando te lo propones!"),
            new MotivationPhrase(null, "La excelencia no es un acto, es un hábito"),
            new MotivationPhrase(null, "Cada repetición te acerca a la maestría"),
            new MotivationPhrase(null, "¡Tu futuro yo te lo agradecerá!"),
            new MotivationPhrase(null, "Los grandes cambios comienzan con pequeños pasos"),
            new MotivationPhrase(null, "¡Esa racha se ve hermosa, continúa!")
    );
    private final JavaMailSender mailSender;
    @Autowired
    private HabitRepository habitRepository;

    @Cacheable(value = "habits", key = "#page + '-' + #size")
    @Override
    public Page<Habit> listHabitsPage(int page, int size) {
        return habitRepository.findAll(PageRequest.of(page, size));
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public Habit createHabit(Habit habit) {
        habit.setId((long) (habits.size() + 1));
        habit.setStartDate(LocalDate.now());
        habits.put(habit.getId(), habit);
        return habit;
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public Habit updateHabit(Long id, Habit habit) {
        Habit existing = habits.get(id);
        if (existing == null) throw new ResourceNotFoundException("Hábito no encontrado");
        existing.setFrequency(habit.getFrequency());
        return existing;
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public void deleteHabit(Long id) {
        habits.remove(id);
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public Reminder programReminder(Long habitId, Reminder reminder) {
        Habit habit = habits.get(habitId);
        if (habit == null) throw new ResourceNotFoundException("Hábito no encontrado"); {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(reminder.getEmail());
                helper.setSubject("Recordatorio de habito");
                helper.setText("!Recuerda tu habito: " + habit.getName() + "!");
                mailSender.send(message);
            } catch (Exception e) {
                throw new RuntimeException("No se pudo enviar el correo");
            }
            return reminder;
        }
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public void trackProgress(Long habitId) {
        Habit habit = habits.get(habitId);
        if (habit == null) throw new ResourceNotFoundException("Hábito no encontrado");
        habit.setCompletedDays(habit.getCompletedDays() + 1);
        habit.setCurrentStreak(habit.getCurrentStreak() + 1);
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public HabitStatDTO getStats(Long habitId) {
        Habit habit = habits.get(habitId);
        if (habit == null) throw new ResourceNotFoundException("Hábito no encontrado");
        return new HabitStatDTO(habit.getName(), habit.getCompletedDays(), habit.getGoalDays());
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public String getMotivationalPhrase() {
        return phrases.get(new Random().nextInt(phrases.size())).getPhrase();
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public List<String> getRewards() {
        return List.of(
                // Recompensas a corto plazo (1-7 días)
                "Después de 1 día: Date un momento para respirar y celebrar",
                "Después de 3 días: Disfruta de tu canción favorita como premio",
                "Después de 7 días: Tómate un café especial o tu bebida favorita",

                // Recompensas a medio plazo (2-4 semanas)
                "Después de 15 días: Date un paseo relax o tiempo en la naturaleza",
                "Después de 21 días: Compra algo pequeño que te guste (un libro, una planta)",
                "Después de 30 días: Ve una película o serie que querías ver",

                // Recompensas a largo plazo (2+ meses)
                "Después de 60 días: Celebra con una comida en tu restaurante favorito",
                "Después de 90 días: Regálate una experiencia (masaje, concierto, taller)",
                "Después de 180 días: Invierte en algo que refuerce tu hábito (equipo, curso)",
                "Después de 1 año: ¡Planifica un viaje o retiro para celebrar!",

                // Recompensas no materiales
                "Cada semana completa: Escribe un logro del que estés orgulloso",
                "Cada mes: Comparte tu progreso con alguien importante para ti",
                "Al cumplir tu meta: Dedica tiempo a reflexionar sobre tu crecimiento");
    }

    @CacheEvict(value = "habits", allEntries = true)
    @Override
    public String reschedule(Long habitId) {
        Habit habit = habits.get(habitId);
        if (habit == null) throw new ResourceNotFoundException("Hábito no encontrado");
        habit.setCurrentStreak(0);
        return "¡Ánimo! Hemos reiniciado tu racha. ¡A empezar de nuevo!";
    }
}
