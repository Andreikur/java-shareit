package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Table(name = "requests", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemRequest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description", nullable = false)
    @NonNull
    private String description;

    @ToString.Exclude
    @ManyToOne
    private User requestor;             //пользователь создавший запрос

    @Column(name = "created", nullable = false)
    private LocalDateTime created;      //время создания запроса

    @OneToMany
    private List<Item> items;
}
