package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
            User user = userRepository.save(UserMapper.toUser(userDto));
            return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(long id, Map<String, String> userData) {
        User currentUser = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));

        if (userData.containsKey("email")) {
            currentUser.setEmail(userData.get("email"));
        }
        if (userData.containsKey("name")) {
            currentUser.setName(userData.get("name"));
        }

        return UserMapper.toUserDto(currentUser);
    }

    @Transactional(readOnly = true)
    public UserDto getUser(long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() ->
                    new UserNotFoundException(String.format("Пользователь с таким id не найден")));
            return UserMapper.toUserDto(user);
        } catch (DataIntegrityViolationException e){
            if(e.getCause() instanceof ConstraintViolationException) {
                throw new UserNotFoundException(String.format(
                        "Пользователь с таким id не найден"));
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUser() {
       List<User> users = userRepository.findAll();
       return  UserMapper.toUserDto(users);
    }

    @Transactional
    public void removeUser(long id) {
        userRepository.deleteById(id);
        log.info("Пользователь удален");
    }
}
