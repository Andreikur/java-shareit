package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private final String HEADERS = "x-sharer-user-id";

    /**
     * Добавляем вещь
     *
     * @param item
     * @param headers
     * @return
     */
    @PostMapping
    public Item addItem(@Valid @RequestBody Item item, @RequestHeader Map<String, String> headers) {
        String stringIdUserOwner1 = headers.get(HEADERS);
        long idUserOwner = Long.parseLong(stringIdUserOwner1);
        return itemService.addItem(item, idUserOwner);
    }

    /**
     * Обновляем вещь
     *
     * @param itemData
     * @param id
     * @param headers
     * @return
     */
    @PatchMapping("{id}")
    public Item updateItem(@Valid @RequestBody Map<String, String> itemData, @PathVariable("id") Long id, @RequestHeader Map<String, String> headers) {
        String stringIdUserOwner1 = headers.get(HEADERS);
        long idUserOwner = Long.parseLong(stringIdUserOwner1);
        return itemService.updateItem(id, itemData, idUserOwner);
    }

    /**
     * Получаем вещь
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ItemDto getItem(@PathVariable("id") Long id) {
        return itemService.getItem(id);
    }

    /**
     * Получаем  список вещей по параметрам
     *
     * @param headers
     * @param text
     * @return
     */
    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader Map<String, String> headers,
                                    @RequestParam(required = false) String text) {
        if (headers == null && text == null) {
            return itemService.getAllItem();
        } else {
            String stringIdUserOwner = headers.get(HEADERS);
            long idUserOwner = Long.parseLong(stringIdUserOwner);
            return itemService.getAllItemsUser(idUserOwner);
        }
    }

    /**
     * Поиск вещей по описанию
     *
     * @param headers
     * @param text
     * @return
     */
    @GetMapping({"/search"})
    public List<ItemDto> searchItem(@RequestHeader Map<String, String> headers,
                                    @RequestParam String text) {
        String stringIdUserOwner = headers.get(HEADERS);
        long idUserOwner = Long.parseLong(stringIdUserOwner);
        return itemService.searchItem(idUserOwner, text.toLowerCase());
    }
}
