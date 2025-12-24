package ru.user.example.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.user.example.entity.User;
import org.openapitools.model.UserResponse;
import org.openapitools.model.UserCreateRequest;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "active", ignore = true)
    UserResponse toResponseDto(User user);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toEntity(UserResponse response);

    UserCreateRequest toUserCreateDto(User user);

    User toEntity(UserCreateRequest response);

}
