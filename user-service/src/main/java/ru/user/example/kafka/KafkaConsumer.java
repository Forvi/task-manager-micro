package ru.user.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.user.example.service.UserService;
import org.openapitools.model.TaskEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final UserService userService;

    @KafkaListener(topics = "task-created", containerFactory = "taskEventListenerFactory")
    public void listen(TaskEvent taskEvent) {
        log.info("[KafkaConsumer] - Received task event: {}", taskEvent);

        try {
            userService.addTaskForUser(taskEvent.getUserID(), taskEvent.getId());
            log.info("[KafkaConsumer] - Task successfully added to user");
        } catch (Exception e) {
            log.error("[KafkaConsumer] - Failed to process task event: {}", taskEvent, e);
            throw e;
        }
    }

}
