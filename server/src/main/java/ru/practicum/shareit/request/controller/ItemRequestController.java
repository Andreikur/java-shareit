package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.exception.BookingBadRequestException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShort;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestService requestService;

    /**
     * Создание запроса на вещь
     * @param itemRequestDto
     * @param userId
     * @return
     */
    @PostMapping
    public ItemRequestDto addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader (ShareItServer.HEADER) long userId) {
        return requestService.addRequest(itemRequestDto, userId);
    }

    /**
     * Получить все запросы этого пользователя
     * @param userId
     * @return
     */
    @GetMapping
    public List<ItemRequestShort> getAllYourItemRequest(@RequestHeader (ShareItServer.HEADER) long userId) {
        return requestService.getAllYourItemRequest(userId);
    }

    /**
     * Получить все запросы других пользователей
     * @param userId
     * @param from
     * @param size
     * @return
     */
    @GetMapping("/all")
    public List<ItemRequestShort> getAllOthersItemRequestDto(@RequestHeader (ShareItServer.HEADER) long userId,
                                                     @RequestParam(required = false) Long from, Long size) {

        if (from == null & size == null) {
            return requestService.getAllOthersItemRequest(userId);
        } else if (from < 0 || size <= 0) {
            throw new BookingBadRequestException(String.format("Значения должны быть не отрицательными"));
        } else {
            return requestService.getAllOthersItemRequestDtoPageByPage(userId, from, size);
        }
    }

    @GetMapping("{requestId}")
    public ItemRequestShort getItemRequestDto(@RequestHeader (ShareItServer.HEADER) long userId,
                                              @PathVariable Long requestId) {
        return requestService.getItemRequest(userId, requestId);
    }
}
