package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.MailNotUniqueException;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(long id, Map<String, String> userData);

    public UserDto getUser(long id);

    public List<UserDto> getAllUser();

    public void removeUser(long id);
}