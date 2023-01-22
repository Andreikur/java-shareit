package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(long id, Map<String, String> userData) {
        return userStorage.updateUser(id, userData);
    }

    public User getUser(long id) {
        return userStorage.getUser(id);
    }

    public List<User> getAllUser() {
        return userStorage.getAllUser();
    }

    public void removeUser(long id) {
        userStorage.removeUser(id);
    }


}
