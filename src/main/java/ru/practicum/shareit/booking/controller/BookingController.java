package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingBadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-bookings.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingDtoShort bookingDtoShort,
                                 @RequestHeader Map<String, String> headers) {
        String stringIdUserBooker = headers.get("x-sharer-user-id");
        long idUserBooker = Long.parseLong(stringIdUserBooker);
        return bookingService.addBooking(bookingDtoShort, idUserBooker);
    }

    @PatchMapping("{bookingId}")
    public BookingDto updateApproved(@PathVariable Long bookingId,
                                     @RequestHeader Map<String, String> headers,
                                     @RequestParam String approved) {
        String stringIdUserOwner = headers.get("x-sharer-user-id");
        long idUserOwner = Long.parseLong(stringIdUserOwner);
        boolean approvedBoolean = Boolean.parseBoolean(approved);
        return bookingService.updateApproved(bookingId, idUserOwner, approvedBoolean);
    }

    /**
     * Получение данных о конкретном бронировании (выполняется автором или владельцем вещи)
     *
     * @param id
     * @param headers
     * @return
     */

    @GetMapping("{id}")
    public BookingDto getBooking(@PathVariable Long id,
                                 @RequestHeader Map<String, String> headers) {
        String stringIdUser = headers.get("x-sharer-user-id");
        long idUser = Long.parseLong(stringIdUser);
        return bookingService.getBooking(id, idUser);
    }

    /**
     * Получение списка всех бронирований текущего пользователя
     * Параметр {state}:    ALL - все
     * CURRENT - текущее
     * PAST - завершенное
     * FUTURE - будущее
     * WAITING - ожидающее потверждение
     * REJECTED - отклоненный
     * отсортировано по дате от наиболее новых к наиболее старым
     *
     * @param headers
     * @return
     */

    @GetMapping
    public List<BookingDto> getAllBooking(@RequestHeader Map<String, String> headers,
                                          @RequestParam(defaultValue = "ALL", required = false) String state,
                                          @Min(0) @RequestParam(defaultValue = "0", required = false) int from,
                                          @Min(1) @RequestParam(defaultValue = "20", required = false) int size) {
        String stringIdUserOwner = headers.get("x-sharer-user-id");
        long idUserOwner = Long.parseLong(stringIdUserOwner);
        //Анотации @Min почему то не отрабатывают
        if (from < 0 || size <= 0) {
            throw new BookingBadRequestException(String.format("Значения должны быть не отрицательными"));
        }
        return bookingService.getAllBooking(idUserOwner, state, from, size);
    }

    /**
     * получение списка бронирования для всех вещей текущего пользователя
     * Параметр {state}:    ALL - все
     * CURRENT - текущее
     * PAST - завершенное
     * FUTURE - будущее
     * WAITING - ожидающее потверждение
     * REJECTED - отклоненный
     * отсортировано по дате
     *
     * @param headers
     * @return
     */

    @GetMapping({"/owner"})
    public List<BookingDto> getAllBookingOwner(@RequestHeader Map<String, String> headers,
                                               @RequestParam(defaultValue = "ALL", required = false) String state,
                                               @Min(0) @RequestParam (defaultValue = "0", required = false) int from,
                                               @Min(1) @RequestParam(defaultValue = "20", required = false) int size) {
        String stringIdUserOwner = headers.get("x-sharer-user-id");
        long idUserOwner = Long.parseLong(stringIdUserOwner);
        if (from < 0 || size <= 0) {
            throw new BookingBadRequestException(String.format("Значения должны быть не отрицательными"));
        }
        return bookingService.getAllBookingOwner(idUserOwner, state, from, size);
    }
}
