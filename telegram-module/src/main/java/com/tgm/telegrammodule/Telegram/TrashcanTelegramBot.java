package com.tgm.telegrammodule.Telegram;


import com.tgm.telegrammodule.Config.TgBotConfig;
import com.tgm.telegrammodule.enums.Role;
import com.tgm.telegrammodule.services.AuthorizationKeyService;
import com.tgm.telegrammodule.services.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
@RequiredArgsConstructor
public class TrashcanTelegramBot extends TelegramLongPollingBot {

    private final TgBotConfig config;
    private final AuthorizationService authorizationService;
    private final AuthorizationKeyService keyService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().getText());
            System.out.println(update.getMessage().getFrom().toString());
            String msgText = update.getMessage().getText();
            Long clientId = update.getMessage().getFrom().getId();

            try {
                if (msgText.startsWith("/start")) {
                    if (authorizationService.isUserAuthorized(clientId)) {
                        execute(new SendMessage(
                                update.getMessage().getChatId().toString(),
                                "Добро пожаловать. Снова"));
                        return;
                    } else {
                        execute(new SendMessage(
                                update.getMessage().getChatId().toString(),
                                "Введите ключ"));
                    }
                    return;
                }

                if(!authorizationService.isUserAuthorized(clientId)) {
                     Role userRole = keyService.isKeyValid(msgText);
                     switch (userRole){
                         case USER ->{
                             execute(new SendMessage(
                                     update.getMessage().getChatId().toString(),
                                     "Привет бомжара"));
                             authorizationService.authorizeUser(clientId,Role.USER);
                         }
                         case ADMIN -> {
                             execute(new SendMessage(
                                     update.getMessage().getChatId().toString(),
                                     "Привет сверхбомжара"));
                             authorizationService.authorizeUser(clientId,Role.ADMIN);
                         }
                         case NOT_VALID -> {
                             execute(new SendMessage(
                                     update.getMessage().getChatId().toString(),
                                     "Ты кто епта"));
                             return;
                         }
                         default -> {
                             execute(new SendMessage(
                                     update.getMessage().getChatId().toString(),
                                     "Ты как сюда попал?"));

                         }
                     }
                     return;
                    }
                switch (msgText){
                    case "/exit" ->{
                        deauthorizeUser(clientId);
                        execute(new SendMessage(
                                update.getMessage().getChatId().toString(),
                                "Ты деавторизован"));
                        return;
                    }
                }
                execute(new SendMessage(
                        update.getMessage().getChatId().toString(),
                        "Ты уже авторизован"));


            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void deauthorizeUser(Long id){
        authorizationService.deauthorizeUser(id);
    }
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
