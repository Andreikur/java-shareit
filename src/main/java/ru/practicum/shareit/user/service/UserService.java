package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(long id, Map<String, String> userData);

    UserDto getUser(long id);

    List<UserDto> getAllUser();

    void removeUser(long id);
}