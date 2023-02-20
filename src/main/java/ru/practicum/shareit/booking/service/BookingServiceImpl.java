package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.enums.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto addBooking(BookingDtoShort bookingDtoShort, Long idUserBooker) {
        Item item = itemRepository.findById(bookingDtoShort.getItemId()).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь с таким id не найден")));

        if (!item.getAvailable()) {
            log.info("Вещь недоступна для бронирования");
            throw new BookingAvailableException(String.format("Вещь недоступна для бронирования"));
        }

        User userBooker = userRepository.findById(idUserBooker).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));     //пользователь осуществляющий бронирование

        User userOwner = item.getOwner();  //хозяин вещи

        if (LocalDateTime.now().isAfter(bookingDtoShort.getStart())) {
            log.info("Дата начала бронирования должна быть в будующем");
            throw new BookingAvailableException(String.format("Дата начала бронирования должна быть в будующем"));
        }

        if (bookingDtoShort.getEnd().compareTo(bookingDtoShort.getStart()) < 0) {
            log.info("Дата начала бронирования позже даты конца");
            throw new BookingAvailableException(String.format("Дата начала бронирования позже даты конца"));
        }

        if (userBooker.equals(userOwner)) {
            log.info("Вещь не может быть забронирована свом хозяином");
            throw new BookingNotCreatedByUserException(String.format(
                    "Вещь не может быть забронирована свом хозяином"));
        }

        BookingDto bookingDto = BookingMapper.toBookingDto(bookingDtoShort);
        bookingDto.setItem(item);
        bookingDto.setBooker(userBooker);
        bookingDto.setStatus(StatusBooking.WAITING);
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto));

        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto updateApproved(long bookingId, long idUserOwner, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new UserNotFoundException(String.format("Бронь с таким id не найдена")));
        Item item = booking.getItem();
        User user = userRepository.findById(idUserOwner).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        if (!item.getOwner().equals(user)) {
            log.info("Бронирование не потверждено, данный пользователь не хозяин вещи");
            throw new BookingNotCreatedByUserException(String.format(
                    "Бронирование не потверждено, данный пользователь не хозяин вещи"));
        }

        if (booking.getStatus().equals(StatusBooking.APPROVED)) {
            throw new BookingAvailableException(String.format("Данное бронировани уже имеет статус APPROVED"));
        }

        if (approved) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }
        Booking booking1 = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking1);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBooking(long idBooking, long idUser) {
        userRepository.findById(idUser).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));

        try {
            Booking booking = bookingRepository.findById(idBooking).orElseThrow(() ->
                    new BookingNotFoundException(String.format("Бронь с таким id не найдена")));

            if (booking.getBooker().getId() != idUser && booking.getItem().getOwner().getId() != idUser) {
                log.info("Пользователь не хозяин вещи или не осуществлял бронирование");
                throw new BookingNotCreatedByUserException(String.format(
                        "Пользователь не хозяин вещи или не осуществлял бронирование"));
            }

            return BookingMapper.toBookingDto(booking);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new BookingNotFoundException(String.format(
                        "Бронь с таким id не найдена"));
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBooking(long idUser, String state) {
        userRepository.findById(idUser).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        List<Booking> bookingsList = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingsList.addAll(bookingRepository.findAllByBookerOrder(idUser));
                break;
            case "CURRENT":
                bookingsList.addAll(bookingRepository.findByBookerCurrent(idUser, LocalDateTime.now()));
                break;
            case "PAST":
                bookingsList.addAll(bookingRepository.findByBookerPast(idUser, LocalDateTime.now()));
                break;
            case "FUTURE":
                bookingsList.addAll(bookingRepository.findByBookerFuture(idUser, LocalDateTime.now()));
                break;
            case "WAITING":
                bookingsList.addAll(bookingRepository.findByBookerAndState(idUser, StatusBooking.WAITING));
                break;
            case "REJECTED":
                bookingsList.addAll(bookingRepository.findByBookerAndState(idUser, StatusBooking.REJECTED));
                break;
            default:
                throw new BookingAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.toBookingDto(bookingsList);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBookingOwner(Long idUserOwner, String state) {
        userRepository.findById(idUserOwner).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        List<Booking> bookingsList = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingsList.addAll(bookingRepository.getByOwner(idUserOwner));
                break;
            case "CURRENT":
                bookingsList.addAll(bookingRepository.getByOwnerCurrent(idUserOwner, LocalDateTime.now()));
                break;
            case "PAST":
                bookingsList.addAll(bookingRepository.getByOwnerPast(idUserOwner, LocalDateTime.now()));
                break;
            case "FUTURE":
                bookingsList.addAll(bookingRepository.getByOwnerFuture(idUserOwner, LocalDateTime.now()));
                break;
            case "WAITING":
                bookingsList.addAll(bookingRepository.findByOwnerAndState(idUserOwner, StatusBooking.WAITING));
                break;
            case "REJECTED":
                bookingsList.addAll(bookingRepository.findByOwnerAndState(idUserOwner, StatusBooking.REJECTED));
                break;
            default:
                throw new BookingAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.toBookingDto(bookingsList);
    }
}
