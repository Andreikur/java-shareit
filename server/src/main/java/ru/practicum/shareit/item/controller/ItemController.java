package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * TODO Sprint add-controllers.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    /**
     * Добавляем вещь
     * @param item
     * @param userId
     * @return
     */
    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto item,
                           @RequestHeader (ShareItServer.HEADER) long userId) {
        return itemService.addItem(item, userId);
    }

    /**
     * Обновляем вещь
     * @param itemData
     * @param id
     * @param userId
     * @return
     */
    @PatchMapping("{id}")
    public ItemDto updateItem(@Valid @RequestBody Map<String, String> itemData,
                              @PathVariable Long id,
                              @RequestHeader (ShareItServer.HEADER) long userId) {
        return itemService.updateItem(id, itemData, userId);
    }

    /**
     * Получаем вещь
     * @param userId
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ItemBooking getItem(@RequestHeader (ShareItServer.HEADER) long userId,
                               @PathVariable Long id) {
        return itemService.getItem(userId, id);
    }

    /**
     * Получаем  список вещей по параметрам
     * @param userId
     * @return
     */
    @GetMapping
    public List<ItemBooking> getAllItem(@RequestHeader (ShareItServer.HEADER) long userId) {
        return itemService.getAllItemsUser(userId);
    }

    /**
     * Поиск вещей по описанию
     * @param userId
     * @param text
     * @return
     */
    @GetMapping({"/search"})
    public List<ItemDto> searchItem(@RequestHeader (ShareItServer.HEADER) long userId,
                                    @RequestParam String text) {
        return itemService.searchItem(userId, text);
    }

    /**
     * Добавляем коментарий
     * @param commentDto
     * @param userId
     * @param itemId
     * @return
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Valid @RequestBody CommentDto commentDto,
                                 @RequestHeader (ShareItServer.HEADER) long userId,
                                 @PathVariable long itemId) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
