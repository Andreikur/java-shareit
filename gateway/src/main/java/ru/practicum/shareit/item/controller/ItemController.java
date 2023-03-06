package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.coment.dto.CommentDto;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemDto itemDto,
                                          @RequestHeader(ShareItGateway.HEADER) long userId) {
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(ShareItGateway.HEADER) long userId,
                                         @PathVariable long itemId,
                                         @RequestBody Map<String, String> itemData) {
        return itemClient.updateItem(userId, itemId, itemData);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(ShareItGateway.HEADER) long userId,
                                          @PathVariable long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItem(@RequestHeader(ShareItGateway.HEADER) long id,
                                          @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                          @Positive @RequestParam(defaultValue = "20", required = false) int size) {
        return itemClient.getAllItem(id, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(ShareItGateway.HEADER) long userId,
                                             @RequestParam String text,
                                             @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                             @Positive @RequestParam(defaultValue = "20", required = false) int size) {
        return itemClient.searchItem(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(ShareItGateway.HEADER) long userId,
                                             @PathVariable long itemId,
                                             @Validated @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}