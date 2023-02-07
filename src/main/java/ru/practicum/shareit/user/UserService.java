package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(long id, Map<String, String> userData);

    //public UserDto getUser(long id);

    public List<UserDto> getAllUser();

    //public void removeUser(long id);
}