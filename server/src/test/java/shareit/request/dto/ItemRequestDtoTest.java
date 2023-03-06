package shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import static org.assertj.core.api.Assertions.assertThat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private ItemRequestDto itemRequestDto;
    List<Item> items;
    User user;

    @BeforeEach
    void beforeEach() {
        LocalDateTime data = LocalDateTime.now();
        items = new ArrayList<>();
        user = new User(1L, "user1@mail.com", "User1");

        ItemRequest itemRequest = new ItemRequest(1, "ItemRequest_description", user, data);

        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        Item item = new Item(1, "Item1", "Item_description", null, user, itemRequest);

        itemRequestDto.setItems(List.of(item));
    }

    @Test
    void testSerialize() throws Exception {
        JsonContent<ItemRequestDto> response = json.write(itemRequestDto);

        Integer id = Math.toIntExact(itemRequestDto.getId());

        assertThat(response).hasJsonPath("$.id");
        assertThat(response).hasJsonPath("$.description");
        assertThat(response).hasJsonPath("$.created");
        assertThat(response).hasJsonPath("$.items");

        assertThat(response).extractingJsonPathNumberValue("$.id").isEqualTo(id);
        assertThat(response).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
        assertThat(response).extractingJsonPathArrayValue("$.items").isNotEmpty();
    }
}
