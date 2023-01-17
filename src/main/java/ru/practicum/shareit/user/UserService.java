package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user){
        return userStorage.addUser(user);
    }

    public User updateUser(long id, User user){
        return userStorage.updateUser(id, user);
    }


}
