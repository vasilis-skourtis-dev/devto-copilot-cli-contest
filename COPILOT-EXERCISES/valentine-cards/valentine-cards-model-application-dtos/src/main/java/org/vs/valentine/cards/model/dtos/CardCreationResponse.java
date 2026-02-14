package org.vs.valentine.cards.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vs.valentine.cards.model.universal.OperationStatus;

/**
 * DTO for response after creating and sending a card.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardCreationResponse {

    /**
     * Status of the creation operation
     */
    private OperationStatus status;

    /**
     * Success or error message
     */
    private String message;

    /**
     * Indicates if the email was sent successfully
     */
    private boolean emailSent;

    /**
     * Optional: Base64 encoded preview of the generated image
     */
    private String imagePreview;
}
