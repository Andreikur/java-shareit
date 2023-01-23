package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public UserDto getUser(long id) {
        return UserMapper.userDto(userStorage.getUser(id));
    }

    public List<UserDto> getAllUser() {
        List<User> userList = userStorage.getAllUser();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(UserMapper.userDto(user));
        }
        return userDtoList;
    }

    public void removeUser(long id) {
        userStorage.removeUser(id);
    }
}
