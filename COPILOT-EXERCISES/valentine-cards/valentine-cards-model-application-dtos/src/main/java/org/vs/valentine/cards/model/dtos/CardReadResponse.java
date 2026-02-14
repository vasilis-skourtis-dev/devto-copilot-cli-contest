package org.vs.valentine.cards.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vs.valentine.cards.model.universal.OperationStatus;

/**
 * DTO for response after reading a card (decoding message).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardReadResponse {

    /**
     * The decoded secret message
     */
    private String message;

    /**
     * Status of the decode operation
     */
    private OperationStatus status;

    /**
     * Optional error message if decoding failed
     */
    private String errorMessage;

    /**
     * Indicates if a message was found
     */
    private boolean messageFound;
}
