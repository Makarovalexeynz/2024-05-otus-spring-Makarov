package ru.makarov.service;

public interface LocalizedIOService extends LocalizedMessagesService, IOService {
    void printLineLocalized(String code);

    String printFormattedLineLocalized(String code, Object ...args);

    String readStringWithPromptLocalized(String promptCode);

    int readIntForRangeLocalized(int min, int max, String errorMessageCode);

    int readIntForRangeWithPromptLocalized(int min, int max, String promptCode, String errorMessageCode);
}
