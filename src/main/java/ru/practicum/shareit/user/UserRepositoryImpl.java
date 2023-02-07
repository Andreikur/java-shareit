package ru.practicum.shareit.user;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.exception.MailNotUniqueException;
import ru.practicum.shareit.exception.UserNotFoundException;

public class UserRepositoryImpl  {
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }



}
