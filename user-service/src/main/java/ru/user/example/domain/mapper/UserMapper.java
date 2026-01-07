package ru.user.example.domain.mapper;

import org.mapstruct.*;
import ru.user.example.domain.User;
import org.openapitools.model.UserResponse;
import org.openapitools.model.UserCreateRequest;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {

    @Mapping(target = "created", source = "created", qualifiedByName = "InstantToOffsetDateTime")
    @Mapping(target = "updated", source = "updated", qualifiedByName = "InstantToOffsetDateTime")
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    User toEntity(UserCreateRequest response);

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
