package ru.makarov.service;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
