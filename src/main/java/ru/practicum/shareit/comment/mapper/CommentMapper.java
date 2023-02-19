package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toComment(User author, Item item, CommentDto commentDto) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                item,
                author);
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName());
    }
}