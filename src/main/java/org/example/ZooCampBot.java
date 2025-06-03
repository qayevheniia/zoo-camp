package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalTime;

public class ZooCampBot {
    private static final String URL = "https://bilety.zoo.wroclaw.pl/rezerwacja/termin-polkolonie.html?d=4&idg=3";
    private static final int CHECK_INTERVAL_SECONDS = 60;

    // ⚠️ Твій Telegram токен (зміні після тесту)
    private static final String TELEGRAM_TOKEN = "7695325528:AAH6pwZI0ipygafJlD4u0iFqhsXwgAo03kE";

    // Спочатку вручну отримай chat_id за допомогою getUpdates
    private static final long CHAT_ID = 467310292; // ← сюди встав свій реальний chat_id

    private static final TelegramBot bot = new TelegramBot(TELEGRAM_TOKEN);

    public static void main(String[] args) {
        System.out.println("ZooCampBot запущено!");

        while (true) {
            try {
                Document doc = Jsoup.connect(URL)
                        .userAgent("Mozilla/5.0")
                        .get();

                Elements cards = doc.select(".card");
                boolean available = false;

                for (var card : cards) {
                    var capacity = card.selectFirst(".card-title.mb-1:contains(Liczba dostępnych miejsc)").text();
                    if (!capacity.contains("0")) {
                        sendTelegramMessage("🎉 Знайдено вільні місця! ➡ " + capacity);
                        available = true;
                    }
                }

                if (!available) {
                    System.out.println("⛔ Місць немає (" + LocalTime.now() + ")");
                }

                Thread.sleep(CHECK_INTERVAL_SECONDS * 50000);
            } catch (IOException | InterruptedException e) {
                System.err.println("❌ Помилка: " + e.getMessage());
            }
        }
    }

    private static void sendTelegramMessage(String message) {
        bot.execute(new SendMessage(CHAT_ID, message));
    }
}
