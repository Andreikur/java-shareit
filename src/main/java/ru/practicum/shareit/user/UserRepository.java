package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;


@RepositoryRestResource(path = "user")
public interface UserRepository extends JpaRepository<User, Long> {

    /*@RestResource(path = "emails")
    List<User> checkEmail (@Param("email") String emailSearch);*/
}
