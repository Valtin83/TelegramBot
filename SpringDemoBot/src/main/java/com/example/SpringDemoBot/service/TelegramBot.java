package com.example.SpringDemoBot.service;

import com.example.SpringDemoBot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            update.getMessage().hasText();
        }
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if (messageText.equals("/start")) {
            startCommandReceived(chatId, update.getMessage().getChatId());
        } else {
            sendMessage(chatId, "Команда не поддерживается");
        }


    }

    public void startCommandReceived(long chatId, Long id) {

        String answer = "Привет, это учебный бот";

        sendMessage(chatId, answer);
}

    private void sendMessage(long chatId, String textToSand) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSand);
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }


    }
}

