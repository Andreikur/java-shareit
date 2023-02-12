package ru.practicum.shareit.item.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.item.repository.ItemRepository;

public class ItemRepositoryImpl {
    private final ItemRepository itemRepository;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }
}
