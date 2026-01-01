package ru.user.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.user.example.domain.User;
import ru.user.example.exception.AlreadyExistsException;
import ru.user.example.exception.InvalidPasswordLengthException;
import ru.user.example.exception.NotExistsException;
import ru.user.example.domain.mapper.UserMapper;
import ru.user.example.domain.repository.UserRepository;
import org.openapitools.model.UserCreateRequest;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import org.openapitools.model.UserResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Создаёт нового пользователя с предоставленными данными.
     *
     * @param request Запрос с данными пользователя
     * @return UserResponse Созданный пользователь
     */
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();

        log.debug("Starting user creation: username={}, email={}", username, email);

        try {
            validateUserCreation(request);
            log.debug("Mapping a dto to User entity: username={}", username);

            User user = userMapper.toEntity(request);

            OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
            user.setCreated(now.toInstant());
            user.setUpdated(now.toInstant());
            user.setActive(true);
            user.setTasks(new HashSet<>());

            User savedUser = userRepository.save(user);

            log.info("User created successfully: id={}, username={}, email={}",
                    savedUser.getId(), username, email);

            return userMapper.toResponse(savedUser);
        } catch (AlreadyExistsException e) {
            log.warn("User creation failed: username={}, email={}, reason={}",
                    username, email, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to create a user: username={}, email={}",
                    username, email, e);
            throw e;
        }
    }

    @Transactional
    public UserResponse addTaskForUser(UUID id, UUID taskId) {
        log.debug("Start adding a task to a user: task={}, user={}", taskId, id);

        try {
            User user = userRepository
                    .findById(id)
                    .orElseThrow(() -> new NotExistsException(
                            String.format("A user with the id %s not exists.", id)
                    ));

            user.getTasks().add(taskId);
            User savedUser = userRepository.save(user);
            return userMapper.toResponse(savedUser);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to adding a task: task_id={}", taskId, e);
            throw e;
        }
    }

    /**
     * Ищет пользователя по ID.
     *
     * @param id уникальный идентификатор пользователя
     * @return пользователь
     */
    public UserResponse getUserById(UUID id) {
        log.debug("Trying to get user: id={}", id);

        try {
            User user = userRepository
                    .findById(id)
                    .orElseThrow(() -> new NotExistsException(
                            String.format("A user with the id %s not exists.", id)
                    ));
            log.info("User successfully received: id={}", id);
            return userMapper.toResponse(user);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to get user: id={}", id);
            throw e;
        }
    }

    public boolean isExist(UUID id) {
        log.debug("Trying to check for existence user: id={}", id);

        try {
            User user = userRepository
                    .findById(id)
                    .orElseThrow(() -> new NotExistsException(
                            String.format("A user with the id %s not exists.", id)
                    ));

            return !Objects.isNull(user);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to check for existence user: id={}", id);
            throw e;
        }
    }

    @Transactional
    public UserResponse softDelete(UUID id) {
        log.debug("Trying to soft delete user: id={}", id);
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NotExistsException(
                        String.format("A user with the id %s not exists.", id)
                ));

        try {
            user.setActive(false);
            user.setUpdated(Instant.now());
            userRepository.save(user);
            return userMapper.toResponse(user);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to soft delete user {}", id);
            throw e;
        }
    }

    /**
     * Проверяет запрос с данными пользователя на корректность:
     * <ul>
     *     <li>Существования - не должен быть null</li>
     *     <li>Почты - не должна существовать</li>
     *     <li>Пользовательского имени - не должно существовать</li>
     *     <li>Длину пароля - должна быть более 6 символов</li>
     * </ul>
     *
     * @param request Запрос с данными пользователя
     */
    private void validateUserCreation(UserCreateRequest request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("User create dto cannot be null.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException(
                    String.format("A user with the email address %s already exists.", request.getEmail())
            );
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException(
                    String.format("A user with the name %s already exists.", request.getUsername())
            );
        }

        if (request.getPassword().length() < 6) {
            throw new InvalidPasswordLengthException("The password length must be more than 6 characters.");
        }

        if (request.getPassword().length() > 100) {
            throw new InvalidPasswordLengthException("The password length must be lower than 100 characters.");
        }
    }
}
