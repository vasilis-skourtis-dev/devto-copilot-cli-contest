package org.valentines.cards.reader.dto;

/**
 * Response DTO returned after creating a steganography card.
 * Contains the encoded image as a byte array for download.
 */
public class CardCreateResponse {

    private byte[] imageData;
    private String status;
    private String errorMessage;
    private String originalFilename;

    public CardCreateResponse() {
    }

    public CardCreateResponse(byte[] imageData, String status) {
        this.imageData = imageData;
        this.status = status;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
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

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
}
