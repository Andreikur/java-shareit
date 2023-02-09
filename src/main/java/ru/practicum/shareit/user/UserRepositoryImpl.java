package ru.practicum.shareit.user;

import org.springframework.context.annotation.Lazy;

public class UserRepositoryImpl  {
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
