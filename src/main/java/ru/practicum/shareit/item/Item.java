package ru.practicum.shareit.item;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private long id;
    @NonNull
    @NotEmpty
    private String name;
    @NonNull
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available = null;
    private long owner;
    private String request;
}

