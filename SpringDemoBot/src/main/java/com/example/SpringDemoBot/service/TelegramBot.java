package com.example.SpringDemoBot.service;

import com.example.SpringDemoBot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Получение имени пользователя
            String username = "";
            if (update.getMessage().getFrom() != null) {
                username = update.getMessage().getFrom().getUserName();
                if (username == null || username.isEmpty()) {
                    // Можно дополнительно взять имя или фамилию
                    username = update.getMessage().getFrom().getFirstName();
                }
            }

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, username);
                    break;
                case "/help":
                    sendMessage(chatId, "Список команд: /start, /help");
                    break;
                default:
                    sendMessage(chatId, "Команда не поддерживается");
            }
        }
    }

    private void startCommandReceived(long chatId, String username) {
        String answer = "Привет, " + username + "! Это учебный бот.";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
            logger.info("Сообщение отправлено: {}", textToSend);
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке сообщения: {}", textToSend, e);
        }
    }
}

