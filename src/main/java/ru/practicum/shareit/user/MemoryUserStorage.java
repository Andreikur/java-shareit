package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.MailNotUniqueException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MemoryUserStorage implements UserStorage {

    private long id;
    private final Map<Long, User> allUsers = new HashMap<>();
    private final List<String> allEmail = new ArrayList<>();

    //добавление пользователя
    public User addUser(User user) {
        String email = user.getEmail();
        checkEmail(user.getEmail());
        allEmail.add(email);
        id++;
        user.setId(id);
        allUsers.put(id, user);
        log.info("Пользователь добавлен");
        return allUsers.get(id);
    }

    //Обновление пользователя
    public User updateUser(long id, Map<String, String> userData) {
        checkUser(id);
        checkEmail(userData.get("email"));
        User currentUser = allUsers.get(id);
        if(userData.containsKey("email")) {
            allEmail.remove(currentUser.getEmail());
            currentUser.setEmail(userData.get("email"));
            allEmail.add(currentUser.getEmail());
        }
        if(userData.containsKey("name")) {
            currentUser.setName(userData.get("name"));
        }
        return currentUser;
    }

    //получаем пользователя по id
    public User getUser(long id){
        return allUsers.get(id);
    }

    //получаем всех пользователей
    public List<User> getAllUser(){
        return List.copyOf(allUsers.values());
    }

    public void removeUser(long id){
        allEmail.remove(allUsers.get(id).getEmail());
        allUsers.remove(id);
    }

    private void checkEmail(String email) {
        if (allEmail.contains(email)) {
            log.info("Пользователь с такой почтой уже существует");
            throw new MailNotUniqueException(String.format(
                    "Пользователь с такой почтой уже существует"));
        }
    }

    private void checkUser(Long id){
        if(!allUsers.containsKey(id)){
            log.info("Пользователь с таким id отсутствует в базе");
            throw new UserNotFoundException(String.format(
                    "Пользователь с id отсутствует в базе", id));
        }
    }
}
