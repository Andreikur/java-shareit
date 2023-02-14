package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Transactional
    @Override
    public BookingDto addBooking(BookingDtoShort bookingDtoShort, Long idUserBooker){
        Item item = itemRepository.findById(bookingDtoShort.getItemId()).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь с таким id не найден")));

        if(!item.getAvailable()){
            log.info("Вещь недоступна для бронирования");
            throw new BookingAvailableException(String.format("Вещь недоступна для бронирования"));
        }

        User userBooker = userRepository.findById(idUserBooker).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));     //пользователь осуществляющий бронирование

        User userOwner = item.getOwner();  //хозяин вещи

        if (bookingDtoShort.getEnd().compareTo(bookingDtoShort.getStart()) < 0){
            log.info("Дата начала бронирования позже даты конца");
            throw new BookingAvailableException(String.format("Дата начала бронирования позже даты конца"));
        }

        if (userBooker.equals(userOwner)){
            log.info("Вещь не может быть забронирована свом хозяином");
            throw new BookingNotCreatedByUserException(String.format(
                    "Вещь не может быть забронирована свом хозяином"));
        }

        BookingDto bookingDto = BookingMapper.toBookingTdo(bookingDtoShort);
        bookingDto.setItem(item);
        bookingDto.setBooker(userBooker);
        bookingDto.setStatus(StatusBooking.WAITING);
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto));

        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto updateApproved(long bookingId, long idUserOwner, boolean approved){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new UserNotFoundException(String.format("Бронь с таким id не найдена")));

        Item item = booking.getItem();

        User user = userRepository.findById(idUserOwner).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));

        if (!item.getOwner().equals(user)){
            log.info("Бронирование не потверждено, данный пользователь не хозяин вещи");
            throw new BookingNotCreatedByUserException(String.format(
                    "Бронирование не потверждено, данный пользователь не хозяин вещи"));
        }

        if (approved) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }
        Booking booking1 = bookingRepository.save(booking);   //??????????????????????

        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);
        return bookingDto;
    }

    @Transactional (readOnly = true)
    @Override
    public BookingDto getBooking(long idBooking, long idUser) {
         userRepository.findById(idUser).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));

        try {
            Booking booking = bookingRepository.findById(idBooking).orElseThrow(() ->
                    new BookingNotFoundException(String.format("Бронь с таким id не найдена")));

            if (booking.getBooker().getId() != idUser && booking.getItem().getOwner().getId() != idUser){
                log.info("Пользователь не хозяин вещи или не осуществлял бронирование");
                throw new BookingNotCreatedByUserException(String.format(
                        "Пользователь не хозяин вещи или не осуществлял бронирование"));
            }

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
    public List<BookingDto> getAllBooking(Long idUser) {
        User userBooker = userRepository.findById(idUser).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));

        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        //List<Booking> bookings = bookingRepository.findAll(sort);

        List<Booking> bookings = bookingRepository.findAllByBookerId(userBooker.getId(), sort);

        return  BookingMapper.toBookingDto(bookings);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBookingOwner(Long idUserOwner) {
        userRepository.findById(idUserOwner).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таким id не найден")));
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDto> bookingDtoList = BookingMapper.toBookingDto(bookings);
        List<BookingDto> bookingDtoListSort = new ArrayList<>();
        for (BookingDto bookingDto : bookingDtoList){
            if (bookingDto.getItem().getOwner().getId() != idUserOwner){
                bookingDtoListSort.add(bookingDto);
            }
        }
        return  BookingMapper.toBookingDto(bookings);
    }
}
