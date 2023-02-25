package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BookingBadRequestException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShort;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
     * @param headers
     * @return
     */
    @PostMapping
    public ItemRequestDto addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                      @RequestHeader Map<String, String> headers) {
        String stringIdUserBooker = headers.get("x-sharer-user-id");
        long userId = Long.parseLong(stringIdUserBooker);
        return requestService.addRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestShort> getAllYourItemRequestDto(@RequestHeader Map<String, String> headers) {
        String stringIdUser = headers.get("x-sharer-user-id");
        long userId = Long.parseLong(stringIdUser);
        return requestService.getAllYourItemRequestDto(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestShort> getAllOthersItemRequestDto(@RequestHeader Map<String, String> headers,
                                                     @RequestParam(required = false) Long from, Long size) {
        String stringIdUser = headers.get("x-sharer-user-id");
        long userId = Long.parseLong(stringIdUser);

        if (from == null & size == null) {
            return requestService.getAllOthersItemRequestDto(userId);
        } else if (from < 0 || size <= 0) {
            throw new BookingBadRequestException(String.format("Значения должны быть не отрицательными"));
        } else {
            return requestService.getAllOthersItemRequestDtoPageByPage(userId, from, size);
        }
    }

    @GetMapping("{requestId}")
    public ItemRequestShort getItemRequestDto(@RequestHeader Map<String, String> headers,
                                              @PathVariable Long requestId) {
        String stringIdUser = headers.get("x-sharer-user-id");
        long userId = Long.parseLong(stringIdUser);
        return requestService.getItemRequest(userId, requestId);
    }

}
