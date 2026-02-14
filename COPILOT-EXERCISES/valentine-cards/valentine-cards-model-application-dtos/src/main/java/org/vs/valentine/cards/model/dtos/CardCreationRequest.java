package org.vs.valentine.cards.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new Valentine card with hidden message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCreationRequest {

    /**
     * The secret message to hide in the image
     */
    private String message;

    /**
     * Email address to send the card to
     */
    private String recipientEmail;

    /**
     * Optional: Sender's name (for email)
     */
    private String senderName;

    /**
     * Optional: Custom email subject
     */
    private String emailSubject;
}
