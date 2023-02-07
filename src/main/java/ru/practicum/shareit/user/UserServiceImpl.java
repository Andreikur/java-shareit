package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.MailNotUniqueException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final List<String> allEmail = new ArrayList<>();

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        String email = userDto.getEmail();
        checkEmail(email);
        allEmail.add(email);
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(long id, Map<String, String> userData) {
        checkEmail(userData.get("email"));
        User currentUser = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с id %s не найден")));

        if (userData.containsKey("email")) {
            allEmail.remove(currentUser.getEmail());
            currentUser.setEmail(userData.get("email"));
            allEmail.add(currentUser.getEmail());
        }
        if (userData.containsKey("name")) {
            currentUser.setName(userData.get("name"));
        }

        return UserMapper.toUserDto(currentUser);
    }

    /*public UserDto getUser(long id) {
        return UserMapper.toUserDto(userStorage.getUser(id));
    }*/

    public List<UserDto> getAllUser() {
       List<User> users = userRepository.findAll();
       return  UserMapper.toUserDto(users);
    }

    /*public void removeUser(long id) {
        userStorage.removeUser(id);
    }*/


    private void checkEmail(String email) {
        if (allEmail.contains(email)) {
            log.info("Пользователь с такой почтой уже существует");
            throw new MailNotUniqueException(String.format(
                    "Пользователь с такой почтой уже существует"));
        }
    }
}
