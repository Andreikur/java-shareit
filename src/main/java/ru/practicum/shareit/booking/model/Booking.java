package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.enums.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Entity
@Table(name = "bookings", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_date", nullable = false)
    //@Future
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    //@Future
    private LocalDateTime end;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker; //пользователь который осуществил бронирование

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusBooking status;

}