package org.valentines.cards.reader.dto;

/**
 * Response DTO returned after decoding a steganography image.
 * Contains the decoded message and status information.
 */
public class CardReadResponse {

    private String message;
    private boolean messageFound;
    private String status;
    private String errorMessage;

    public CardReadResponse() {
    }

    public CardReadResponse(String message, boolean messageFound, String status) {
        this.message = message;
        this.messageFound = messageFound;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMessageFound() {
        return messageFound;
    }

    public void setMessageFound(boolean messageFound) {
        this.messageFound = messageFound;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
