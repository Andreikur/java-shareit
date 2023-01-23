package ru.practicum.shareit.user;

public class UserMapper {

    public static UserDto userDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }
}
