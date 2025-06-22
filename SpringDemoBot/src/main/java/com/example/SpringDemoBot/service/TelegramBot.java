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


    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        try {
            String[] parts = messageText.split(" ");
            double totalLength = 0;

            for (int i = 0; i < parts.length; i += 2) {
                double length = Double.parseDouble(parts[i]); // длина стороны в мм
                int quantity = Integer.parseInt(parts[i + 1]); // количество сторон
                totalLength += (length + 40) * quantity; // добавляем длину с учетом отходов
            }

            double totalLengthInMeters = totalLength / 1000; // переводим в метры
            String responseText = "Общий расход материалов: " + totalLengthInMeters + " метров.";

            sendMessage(chatId, responseText);
        } catch (Exception e) {
            sendMessage(chatId, "Пожалуйста, введите пары длина (в мм) и количество через пробел, например: 100 5 200 3.");
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}