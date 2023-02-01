package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    User updateUser(long id, Map<String, String> userData);

    User getUser(long id);

    List<User> getAllUser();

    void removeUser(long id);
}
