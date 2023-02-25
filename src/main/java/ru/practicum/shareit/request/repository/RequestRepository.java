package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT b FROM ItemRequest b " +
            "WHERE b.requestor.id = ?1 " +
            "ORDER BY b.created DESC")
    List<ItemRequest> findAllItemRequestByReguestor(long userId);

    @Query("SELECT b FROM ItemRequest b " +
            "WHERE b.requestor.id <>?1 " +
            "ORDER BY b.created DESC")
    List<ItemRequest>findAllItemRequestCreatedByOthers(long userId);

    @Query("SELECT b FROM ItemRequest b " +
            "WHERE b.id = ?1 ")
    ItemRequest findItemRequest(long requestId);
}
