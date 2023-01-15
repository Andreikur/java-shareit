package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.MailNotUniqueException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MemoryUserStorage implements UserStorage {

    private long id;
    private final Map<String, User> allUsers = new HashMap<>();

    public User addUser(User user) {
        //проверить на пользователя с такой почтой
        String email = user.getEmail();
        if (allUsers.containsKey(email)) {
            log.info("Пользователь с такой почтой уже существует");
            throw new MailNotUniqueException(String.format(
                    "Пользователь с такой почтой уже существует"));
        } else {
            id++;
            user.setId(id);
            allUsers.put(email, user);
            log.info("Пользователь добавлен");
            return allUsers.get(email);
        }
    }

    public User updateUser(User user) {
        String email = user.getEmail();
        return user;
    }

}
