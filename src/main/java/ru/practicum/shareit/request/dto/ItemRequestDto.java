package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private long id;
    private String description;
    private User requestor;             //пользователь создавший запрос
    private LocalDateTime created;      //время создания запроса
    private List<Item> items;
}
