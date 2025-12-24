package ru.user.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.user.example.entity.User;
import ru.user.example.exception.AlreadyExistsException;
import ru.user.example.exception.InvalidPasswordLength;
import ru.user.example.mapper.UserMapper;
import ru.user.example.repository.UserRepository;
import org.openapitools.model.UserCreateRequest;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
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

        log.debug("Starting user creation - username: {}, email: {}", username, email);

        try {
            validateUserCreation(request);

            log.debug("Mapping a dto to user entity: {}", username);
            User user = userMapper.toEntity(request);
            user.setActive(true);
            user.setCreated(Instant.now());
            user.setUpdated(Instant.now());
            user.setTasks(new HashSet<>());

            User savedUser = userRepository.save(user);
            log.info("User created successfully - id: {}, username: {}, email: {}",
                    savedUser.getId(), username, email);

            return userMapper.toResponseDto(savedUser);
        } catch (AlreadyExistsException e) {
            log.warn("User creation failed - username: {}, email: {}, reason: {}",
                    username, email, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to create a user: username: {}, email: {}",
                    username, email, e);
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
            throw new InvalidPasswordLength("The password length must be more than 6 characters.");
        }
    }
}
