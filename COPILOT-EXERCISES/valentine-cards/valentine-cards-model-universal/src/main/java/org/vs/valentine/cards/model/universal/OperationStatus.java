package org.vs.valentine.cards.model.universal;

/**
 * Enumeration of operation statuses for card operations.
 */
public enum OperationStatus {
    /**
     * Operation completed successfully
     */
    SUCCESS,

    /**
     * Operation failed
     */
    FAILURE,

    /**
     * Operation in progress
     */
    IN_PROGRESS,

    /**
     * Invalid input provided
     */
    INVALID_INPUT,

    /**
     * No hidden message found in image
     */
    NO_MESSAGE_FOUND,

    /**
     * Message encoding/decoding error
     */
    ENCODING_ERROR
}
