package ru.practicum.shareit.user;

public interface UserStorage {
    User addUser (User user);
    User updateUser(long id, User user);
}
