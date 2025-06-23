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

    private final BotConfig botConfig;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

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

    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        logger.info("Получено сообщение из чата {}: {}", chatId, messageText);

        try {
            String[] parts = messageText.split(" ");
            double totalLength = 0;

            // Пробегаемся по всем элементам, кроме последнего
            for (int i = 0; i < parts.length - 1; i += 2) {
                double length = Double.parseDouble(parts[i]); // длина стороны в мм
                int quantity = Integer.parseInt(parts[i + 1]); // количество сторон
                totalLength += (length + 40) * quantity; // добавляем длину с учетом отходов
                logger.debug("Проанализированная длина: {}, количество: {}, текущая общая длина: {}", length, quantity, totalLength);
            }

            // Получаем множитель из последнего элемента
            double multiplier = Double.parseDouble(parts[parts.length - 1]);
            if (multiplier <= 0) {
                sendMessage(chatId, "Множитель должен быть положительным числом.");
                logger.warn("Указанный множитель не является положительным: {}", multiplier);
                return;
            }

            // Умножаем общую длину на множитель
            double totalLengthInMeters = (totalLength / 1000) * multiplier; // переводим в метры и применяем множитель
            String responseText = "Общая длина с учетом отходов и умноженная на " + multiplier + ": "
                    + totalLengthInMeters + " метров.";
            logger.info("Расчетная общая длина (в метрах): {}", totalLengthInMeters);

            sendMessage(chatId, responseText);
        } catch (Exception e) {
            sendMessage(chatId, "Пожалуйста, введите пары: длина (в мм)" +
                    " и количество одинаковых сторон через пробел," +
                    " а затем общее количеств изделий, например: 100 5 200 3 2.");
            logger.error("Сообщение об ошибке при обработке: {}", messageText, e);
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке сообщения в чат {}: {}", chatId, e.getMessage(), e);
        }
    }
}