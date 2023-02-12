package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@RepositoryRestResource(path = "items")
//@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
