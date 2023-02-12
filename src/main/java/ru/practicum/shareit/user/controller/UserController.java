package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    /**
     * Добавляем пользователя
     *
     * @param user
     * @return
     */
    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto user) {
        return userService.addUser(user);
    }

    /**
     * Обновляем пользователя
     *
     * @param userData
     * @param id
     * @return
     */
    @PatchMapping("{id}")
    public UserDto update(@Valid @RequestBody Map<String, String> userData, @PathVariable("id") Long id) {
        return userService.updateUser(id, userData);
    }

    /**
     * Получаем пользователя по id
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    /**
     * Получаем всех пользователей
     *
     * @return
     */
    @GetMapping
    public List<UserDto> getAllUser() {
        return userService.getAllUser();
    }

    /**
     * Удаляем пользователя
     *
     * @param id
     */
    @DeleteMapping("{id}")
    public void removeUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
    }
}
