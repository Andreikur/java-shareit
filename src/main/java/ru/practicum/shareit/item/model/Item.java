package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Table(name = "items", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;
    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;
    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available = null;
    @ToString.Exclude
    @ManyToOne
    private User owner;
}

