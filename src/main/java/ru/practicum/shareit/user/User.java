package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    @NonNull
    @NotEmpty
    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message = "Пожалуйста укажите дейтвительный адрес")
    private String email;
    @NonNull
    @NotBlank
    private String name;
}