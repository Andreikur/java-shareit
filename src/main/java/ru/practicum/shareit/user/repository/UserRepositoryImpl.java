package ru.practicum.shareit.user.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.user.repository.UserRepository;

public class UserRepositoryImpl  {
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
