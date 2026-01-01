package ru.example.task_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.task_service.client.UserClient;
import ru.example.task_service.domain.Task;
import ru.example.task_service.domain.mapper.TaskMapper;
import ru.example.task_service.domain.repository.TaskRepository;
import org.openapitools.model.TaskResponse;
import org.openapitools.model.TaskCreateRequest;
import ru.example.task_service.exception.NotExistsException;
import org.openapitools.model.TaskPatchRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.openapitools.model.TaskAddRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserClient userClient;

    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        String name = request.getName();
        log.debug("Starting task creation: task={}", name);

        try {
            log.debug("Mapping a dto to Task entity: name={}", name);

            if (!userClient.checkExistsUser(request.getUserID())) {
                throw new NotExistsException("User is not exists: id=" + request.getUserID());
            }

            Task task = taskMapper.toEntity(request);
            Instant time = Instant.now();
            task.setCompleted(false);
            task.setActive(true);
            task.setCreated(time);
            task.setUpdated(time);
            Task savedTask = taskRepository.save(task);

            TaskAddRequest taskDto = new TaskAddRequest();
            taskDto.setTaskId(savedTask.getId());
            System.out.println("saved task id: " + savedTask.getUserID() + " task dto id: " + taskDto.getTaskId());
            userClient.addTaskForUser(savedTask.getUserID(), taskDto);

            log.info("Task created successfully: id={}, name={}", name, savedTask.getId());
            return taskMapper.toResponse(savedTask);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to create task: taskname={}", name);
            throw e;
        }
    }

    @Transactional
    public TaskResponse getTaskById(UUID id) {
        log.debug("Trying to get task: id={}", id);

        try {
            Task task = taskRepository
                    .findById(id)
                    .orElseThrow(() -> new NotExistsException(
                            String.format("A task with the id %s not exists.", id)
                    ));
            log.info("task successfully received: id={}", id);
            return taskMapper.toResponse(task);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to get task: id={}", id);
            throw e;
        }
    }

    /**
     * Находит задачу по дате.
     *
     * @param date дата
     * @return список задач
     */
    @Transactional
    public List<TaskResponse> getTasksByDate(LocalDate date) {
        log.debug("Trying to get task by date={}", date.toString());

        try {
            List<Task> tasks = taskRepository.findAllByDate(date);
            log.info("Found {} tasks by date {}", tasks.size(), date);

            return tasks
                    .stream()
                    .map(taskMapper::toResponse)
                    .toList();
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to get task by date {}",
                    date.toString());
            throw e;
        }
    }

    /**
     * Частично обновляет сущность.
     *
     * @param id уникальный идентификатор задачи
     * @param updates обновления; передаются только поля, которые необходимо обновить
     * @return обновлённая сущность
     */
    @Transactional
    public TaskResponse updateTaskChanges(UUID id, TaskPatchRequest updates) {
        log.debug("Trying to update Task");
        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new NotExistsException(
                        String.format("A task with the id %s not exists.", id)
                ));

        try {
            taskMapper.update(task, updates);
            taskRepository.save(task);
            task.setUpdated(Instant.now());
            return taskMapper.toResponse(task);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to update Task {}", task.getId());
            throw e;
        }
    }

    @Transactional
    public TaskResponse deleteTaskById(UUID id) {
        log.debug("Trying to delete Task: id={}", id);

        try {
            Task task = taskRepository
                    .findById(id)
                    .orElseThrow(() -> new NotExistsException(
                            String.format("A task with the id %s not exists.", id)
                    ));

            taskRepository.delete(task);
            return taskMapper.toResponse(task);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to delete Task {}", id);
            throw e;
        }
    }

}
