package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerOrder(long userId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.start < ?2 " +
            "AND b.end> ?2 " +
            "AND b.booker.id = ?1 " +
            "ORDER BY b.start")
    List<Booking> findByBookerCurrent(long userId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.end < ?2 " +
            "AND b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findByBookerPast(long userId, LocalDateTime endTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.start >?2 " +
            "AND b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findByBookerFuture(long userId, LocalDateTime startTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY b.status DESC")
    List<Booking> findByBookerAndState(long userId, StatusBooking status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.end > ?2 " +
            "AND b.start < ?2 " +
            "ORDER BY b.start")
    List<Booking> getByOwnerCurrent(long userId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.end < ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> getByOwnerPast(long userId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> getByOwnerFuture(long userId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> getByOwner(long userId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findByOwnerAndState(long userId, StatusBooking status);

    Optional<Booking> findByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, LocalDateTime end);

    @Query("SELECT DISTINCT b from Booking b " +
            "WHERE b.end < ?2 " +
            "AND b.item.id = ?1 " +
            "AND b.item.owner.id = ?3 " +
            "ORDER BY b.start DESC")
    Optional<Booking> findLastBooking(long itemId, LocalDateTime now, long userId);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "WHERE b.start > ?2 " +
            "AND b.item.id = ?1 " +
            "AND b.item.owner.id = ?3 " +
            "ORDER BY b.start ")
    Optional<Booking> findNextBooking(long itemId, LocalDateTime now, long userId);
}
