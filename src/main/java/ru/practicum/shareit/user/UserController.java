package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
     * @param user
     * @return
     */
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Обновляем пользователя
     * @param userData
     * @param id
     * @return
     */
    @PatchMapping("{id}")
    public User update(@Valid @RequestBody Map<String, String> userData, @PathVariable("id") Long id) {
        return userService.updateUser(id, userData);
    }

    /**
     * Получаем пользователя по id
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    /**
     * Получаем всех пользователей
     * @return
     */
    @GetMapping
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    /**
     * Удаляем пользователя
     * @param id
     */
    @DeleteMapping("{id}")
    public void removeUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
    }
}
