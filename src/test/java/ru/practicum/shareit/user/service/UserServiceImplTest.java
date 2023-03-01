package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    Map<String, String> userData;

    @BeforeEach
    void beforeEach() {
        user1 = new User(1L,  "user1@mail.com", "User1");
        userData = new HashMap<>() {{
            put("email", "user1update@mail.com");
            put("name", "User1");
        }};
    }

    @Test
    void addUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(user1);

        UserDto userDto = userService.addUser(
                UserMapper.toUserDto(user1));

        assertEquals(1, userDto.getId());
        assertEquals("User1", userDto.getName());
        assertEquals("user1@mail.com", userDto.getEmail());
    }

    @Test
    void updateUserWithEmailFormatTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        userService.updateUser(1, userData);
        User userCur = userRepository.findById(1L).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        UserDto userDto = UserMapper.toUserDto(userCur);

        assertEquals(1, userDto.getId());
        assertEquals("User1", userDto.getName());
        assertEquals("user1update@mail.com", userDto.getEmail());
    }

    @Test
    void updateUserWithNoUser() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(1, userData));

        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void getUserTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));

        UserDto userDto = userService.getUser(user1.getId());

        assertEquals(1, userDto.getId());
        assertEquals("User1", userDto.getName());
        assertEquals("user1@mail.com", userDto.getEmail());
    }

    @Test
    void getUserWrongIdTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUser(2));

        assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void getAllUsersTest() {
        when(userRepository.findAll())
                .thenReturn(List.of(user1));

        List<UserDto> userDto = userService.getAllUser();

        assertEquals(1, userDto.size());
        assertEquals(1, userDto.get(0).getId());
        assertEquals("User1", userDto.get(0).getName());
        assertEquals("user1@mail.com", userDto.get(0).getEmail());
    }

    @Test
    void getAllUsersWhenUserFoundThenUserNotFoundExceptionThrown() {
        long userId = 0L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    void deleteUserTest() {
        when(userRepository.findById(anyLong()))
                .thenThrow(new  UserNotFoundException("Пользователь с таким id не найден"));
        userRepository.save(user1);
        userService.removeUser(1);
        assertThatThrownBy(() -> userRepository.findById(1L)).isInstanceOf(UserNotFoundException.class);
    }
}
