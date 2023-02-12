package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
public class ItemRequest {
    private long id;
    private String description;
    private User requestor;             //пользователь создавший запрос
    private LocalDateTime created;      //время создания запроса
}