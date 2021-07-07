package org.aleksid.wikime;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//оно лежало без дела, решил прикрутить.
public class WikimeTelegramBot {
    private static int offset = 0;
    private static Set<Long> alive_chats_id = new HashSet<>();
    private static TelegramBot bot = new TelegramBot("1046879930:AAGcW1fCKsGBmcK6R7XBsLvcuvnx5OuX56I");

    public static void main(String[] args) {

        while (true) {

            GetUpdates getUpdates = new GetUpdates().limit(1).offset(offset).timeout(0);
            GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
            List<Update> updates = updatesResponse.updates();

            if (!updates.isEmpty()) {
                Update update = updates.get(0);
                System.out.println(update);

                Message message = update.message();
                alive_chats_id.add(message.chat().id());
                String answer = "Hello, this is announcement bot for wikime project. I will tell you when something happen";

                if ("/start".equals(message.text())) {

                } else {
                    answer = "I don't know this command";
                }
                SendMessage sendMessage = new SendMessage(message.chat().id(), answer);

                bot.execute(sendMessage);

                offset = update.updateId() + 1;
            }
        }
    }
//увы, это не работает. Бот может только отвечать на сообщения
    public static void send(String message) {
        for(Long id : alive_chats_id){
            SendResponse response = bot.execute(new SendMessage(id, message));
        }
    }
}


