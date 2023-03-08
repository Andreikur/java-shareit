package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingBadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class  BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingDtoShort bookingDtoShort,
                                 @RequestHeader (ShareItServer.HEADER) long idUserBooker) {
        return bookingService.addBooking(bookingDtoShort, idUserBooker);
    }

    @PatchMapping("{bookingId}")
    public BookingDto updateApproved(@PathVariable Long bookingId,
                                     @RequestHeader (ShareItServer.HEADER) long idUserOwner,
                                     @RequestParam Boolean approved) {
        return bookingService.updateApproved(bookingId, idUserOwner, approved);
    }

    /**
     * Получение данных о конкретном бронировании (выполняется автором или владельцем вещи)
     *
     * @param id
     * @param idUser
     * @return
     */

    @GetMapping("{id}")
    public BookingDto getBooking(@PathVariable Long id,
                                 @RequestHeader (ShareItServer.HEADER) long idUser) {
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
     * @param idUserOwner
     * @return
     */

    @GetMapping
    public List<BookingDto> getAllBooking(@RequestHeader (ShareItServer.HEADER) long idUserOwner,
                                          @RequestParam(defaultValue = "ALL", required = false) String state,
                                          @Min(0) @RequestParam(defaultValue = "0", required = false) int from,
                                          @Min(1) @RequestParam(defaultValue = "20", required = false) int size) {
        //Анотации @Min почему то не отрабатывают
        if (from < 0 || size <= 0) {
            throw new BookingBadRequestException(String.format("Значения должны быть не отрицательными"));
        }
        return bookingService.getAllBooking(idUserOwner, state, from, size);
    }

    /**
     * * получение списка бронирования для всех вещей текущего пользователя
     *      * Параметр {state}:    ALL - все
     *      * CURRENT - текущее
     *      * PAST - завершенное
     *      * FUTURE - будущее
     *      * WAITING - ожидающее потверждение
     *      * REJECTED - отклоненный
     *      * отсортировано по дате
     * @param idUserOwner
     * @param state
     * @param from
     * @param size
     * @return
     */


    @GetMapping({"/owner"})
    public List<BookingDto> getAllBookingOwner(@RequestHeader (ShareItServer.HEADER) long idUserOwner,
                                               @RequestParam(defaultValue = "ALL", required = false) String state,
                                               @Min(0) @RequestParam (defaultValue = "0", required = false) int from,
                                               @Min(1) @RequestParam(defaultValue = "20", required = false) int size) {
        if (from < 0 || size <= 0) {
            throw new BookingBadRequestException(String.format("Значения должны быть не отрицательными"));
        }
        return bookingService.getAllBookingOwner(idUserOwner, state, from, size);
    }
}
