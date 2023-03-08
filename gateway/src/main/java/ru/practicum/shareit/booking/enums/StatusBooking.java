package ru.practicum.shareit.booking.enums;

public enum StatusBooking {
    WAITING,    //ожидает одобрения
    APPROVED,   //бронирование подтверждено владельцем
    REJECTED,   //бронирование отклонено владельцем
    CANCELED    //бронирование отменено создателем
}
