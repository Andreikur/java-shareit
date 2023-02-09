package ru.practicum.shareit.item;

import org.springframework.context.annotation.Lazy;

public class ItemRepositoryImpl {
    private final ItemRepository itemRepository;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }
}
