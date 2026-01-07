package ru.example.task_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.example.task_service.domain.Task;
import java.util.UUID;
import org.openapitools.model.TaskEvent;
import ru.example.task_service.domain.mapper.TaskMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEvents {

    private final KafkaTemplate<UUID, TaskEvent> kafkaTemplate;
    private final TaskMapper taskMapper;

    public void sendCreationTaskEvent(Task task) {
        var future = kafkaTemplate.send(
                "task-created", task.getId(), taskMapper.toEvent(task)
        );

        future.whenComplete((result, e) -> {
           if (e == null) {
               log.info("Task event sent: partition={}, offset={}",
                       result.getRecordMetadata().partition(),
                       result.getRecordMetadata().offset());
           } else {
               log.error("Failed to send task event: {}", e.getMessage(), e);
           }
        });
    }

}
