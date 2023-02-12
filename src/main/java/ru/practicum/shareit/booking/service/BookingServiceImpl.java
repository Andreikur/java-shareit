package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Transactional
    @Override
    public BookingDto addBooking(BookingDtoShort bookingDtoShort, Long idUserOwner){
        ItemDto itemDto = itemService.getItem(bookingDtoShort.getItemId());
        if(!itemDto.getAvailable()){
            log.info("Вещь недоступна для бронирования");
            throw new BookingAvailableException(String.format("Вещь недоступна для бронирования"));
        }

        UserDto userDto = userService.getUser(idUserOwner);  //проверяем наличие пользователя

        if (bookingDtoShort.getEnd().compareTo(bookingDtoShort.getStart()) < 0){
            log.info("Дата начала бронирования позже даты конца");
            throw new BookingAvailableException(String.format("Дата начала бронирования позже даты конца"));
        }

        if (Objects.equals(itemDto.getOwner(), idUserOwner)){
            log.info("Вещь не может быть забронирована этим пользователем");
            throw new BookingNotCreatedByUserException(String.format(
                    "Вещь не может быть забронирована этим пользователем"));
        }

        bookingDtoShort.setBookerId(idUserOwner);
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDtoShort));
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        bookingDto.setItem(ItemMapper.toItem(itemDto));
        bookingDto.setBooker(UserMapper.toUser(userDto));
        return bookingDto;
    }

    public BookingDto updateApproved(Long bookingId, long idUserOwner, boolean approved){
        //Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
        //        new UserNotFoundException(String.format("Бронь с таким id не найдена")));

        BookingDto bookingDto = this.getBooking(bookingId, idUserOwner);

        UserDto userDto = userService.getUser(idUserOwner);  //проверяем наличие пользователя
        //ItemDto itemDto = itemService.getItem(bookingDto.getItem().getId());

        /*if (itemDto.getOwner() != idUserOwner){
            log.info("Бронирование не потверждено, данный пользователь не хозяин вещи");
            throw new BookingNotCreatedByUserException(String.format(
                    "Бронирование не потверждено, данный пользователь не хозяин вещи"));
        }*/

        if (approved) {
            bookingDto.setStatus(StatusBooking.APPROVED);
        } else {
            bookingDto.setStatus(StatusBooking.REJECTED);
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    public BookingDto getBooking(long id, long idUserOwner) {
        try {
            Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                    new BookingNotFoundException(String.format("Бронь с таким id не найдена")));

            return BookingMapper.toBookingDto(booking);
        } catch (DataIntegrityViolationException e){
            if(e.getCause() instanceof ConstraintViolationException) {
                throw new BookingNotFoundException(String.format(
                        "Бронь с таким id не найдена"));
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBooking() {
        List<Booking> bookings = bookingRepository.findAll();
        return  BookingMapper.toBookingDto(bookings);
    }
}
