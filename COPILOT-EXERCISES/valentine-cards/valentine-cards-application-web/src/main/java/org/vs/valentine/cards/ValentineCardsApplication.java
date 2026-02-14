package org.vs.valentine.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Valentine Cards Application - Send secret messages with love!
 * 
 * This application uses steganography to hide messages in images,
 * allowing users to create and share Valentine cards with hidden messages.
 * 
 * Main features:
 * - LSB Steganography for encoding/decoding messages
 * - Beautiful UI with animated hearts
 * - Email delivery of cards
 * - Drag-and-drop image upload
 * 
 * @author Valentine Cards Team
 * @version 1.0.0
 * @since 2026-02-15
 */
@SpringBootApplication(scanBasePackages = {
    "org.vs.valentine.cards.domain.services",
    "org.vs.valentine.cards.ui.controllers"
})
public class ValentineCardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValentineCardsApplication.class, args);
        System.out.println("\n" +
            "💌════════════════════════════════════════════════════════════════💌\n" +
            "                  Valentine Cards Application Started!              \n" +
            "                                                                     \n" +
            "    🌐 Access the application at: http://localhost:8080             \n" +
            "                                                                     \n" +
            "    Features:                                                       \n" +
            "    ❤️  Create cards with hidden messages                          \n" +
            "    📧 Send cards via email                                         \n" +
            "    🔓 Read and decode secret messages                              \n" +
            "                                                                     \n" +
            "    Spread love with secret messages! ❤️                            \n" +
            "💌════════════════════════════════════════════════════════════════💌\n"
        );
    }
}
