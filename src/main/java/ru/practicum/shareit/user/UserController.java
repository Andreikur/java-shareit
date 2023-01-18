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

    //Добавляем пользователя
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    //Обновляем пользователя
    @PatchMapping("{id}")
    public User update(@Valid @RequestBody Map<String, String> userData, @PathVariable("id") Long id){
        return userService.updateUser(id, userData);
    }

    //получаем пользователя по id
    @GetMapping("{id}")
    public User getUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }

    //получаем всех пользователей
    @GetMapping
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    //Удаляем пользователя
    @DeleteMapping("{id}")
    public void removeUser(@PathVariable("id") Long id){
        userService.removeUser(id);
    }


}
