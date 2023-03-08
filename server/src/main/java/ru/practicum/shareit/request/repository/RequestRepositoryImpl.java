package ru.practicum.shareit.request.repository;

import org.springframework.context.annotation.Lazy;

public class RequestRepositoryImpl {
    private final RequestRepository requestRepository;

    public RequestRepositoryImpl(@Lazy RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }
}
