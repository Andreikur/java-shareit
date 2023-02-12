package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;


@RepositoryRestResource(path = "users")
//@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
