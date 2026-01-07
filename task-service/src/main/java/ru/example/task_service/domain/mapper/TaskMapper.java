package ru.example.task_service.domain.mapper;

import org.mapstruct.*;
import ru.example.task_service.domain.Task;
import org.openapitools.model.TaskResponse;
import org.openapitools.model.TaskCreateRequest;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.openapitools.model.TaskPatchRequest;
import org.openapitools.model.TaskEvent;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface TaskMapper {

    @Mapping(target = "created", source = "created", qualifiedByName = "InstantToOffsetDateTime")
    @Mapping(target = "updated", source = "updated", qualifiedByName = "InstantToOffsetDateTime")
    TaskResponse toResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "completed", ignore = true)
    Task toEntity(TaskCreateRequest response);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "userID", ignore = true)
    void update(@MappingTarget Task task, TaskPatchRequest request);

    @Mapping(target = "created", source = "created", qualifiedByName = "InstantToOffsetDateTime")
    @Mapping(target = "updated", source = "updated", qualifiedByName = "InstantToOffsetDateTime")
    TaskEvent toEvent(Task task);

    /* =============== MAP CUSTOM METHODS =============== */

    @Named("InstantToOffsetDateTime")
    default OffsetDateTime mapInstantToOffsetDateTime(Instant instant) {
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Named("OffsetDateTimeToInstant")
    default Instant mapOffsetDateTimeToInstant(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toInstant();
    }
}
