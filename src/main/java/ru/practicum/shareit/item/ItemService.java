package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;

    public Item addItem(Item item, long idUserOwner){
        return itemStorage.addItem(item, idUserOwner);
    }

    public Item updateItem (long id, Map<String, String> itemDAta, long idUserOwner){
        return itemStorage.updateItem(id, itemDAta, idUserOwner);
    }

    public Item getItem(long id){
        return itemStorage.getItem(id);
    }

    public List<Item> getAllItem(){
        return itemStorage.getAllItem();
    }

    public List<Item> getAllItemsUser(long idUserOwner){
        return itemStorage.getAllItemsUser(idUserOwner);
    }

    public List<Item> searchItem (long idUserOwner, String text){
        return itemStorage.searchItem(idUserOwner, text);
    }
}
