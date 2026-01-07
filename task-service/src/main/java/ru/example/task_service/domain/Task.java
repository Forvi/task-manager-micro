package ru.example.task_service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ru.example.task_service.enums.Level;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks", schema = "task")
public class Task extends BaseEntity {
    @NotNull
    @Size(max = 64)
    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("false")
    @JsonProperty("completed")
    @Column(name = "completed", nullable = false)
    private boolean completed;

    @NotNull
    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @JsonProperty("importance_level")
    @Column(name = "importance_level", nullable = false)
    private Level importanceLevel = Level.NEUTRAL;

    @NotNull
    @JsonProperty("date")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @JsonProperty("user_id")
    @Column(name = "user_id", nullable = false)
    private UUID userID;
}
