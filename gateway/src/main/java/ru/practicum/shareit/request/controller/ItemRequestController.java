package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestBody;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@Validated @RequestBody ItemRequestBody itemRequestDto,
                                             @RequestHeader(ShareItGateway.HEADER) long userId) {
        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsInfo(@RequestHeader(ShareItGateway.HEADER) long userId) {
        return itemRequestClient.getRequestsInfo(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestInfo(@RequestHeader(ShareItGateway.HEADER) long userId,
                                                 @PathVariable long requestId) {
        return itemRequestClient.getRequestInfo(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsList(@RequestHeader(ShareItGateway.HEADER) long userId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                                  @Positive @RequestParam(defaultValue = "10", required = false) int size) {
        return itemRequestClient.getRequestsList(userId, from, size);
    }
}
