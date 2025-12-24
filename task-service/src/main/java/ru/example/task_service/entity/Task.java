package ru.example.task_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ru.example.task_service.enums.Level;
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
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "completed", nullable = false)
    private boolean completed;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "importance_level", nullable = false)
    private Level importanceLevel = Level.NEUTRAL;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userID;
}
