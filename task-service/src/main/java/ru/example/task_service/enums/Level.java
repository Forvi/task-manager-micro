package ru.example.task_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Level {
    HIGH("Высокий"),
    MIDDLE("Средний"),
    LOW("Низкий"),
    NEUTRAL("Нейтральный");

    private final String description;
}
