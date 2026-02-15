package org.valentines.cards.reader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.valentines.cards.reader.dto.CardCreateResponse;
import org.valentines.cards.reader.dto.CardReadResponse;

import java.io.IOException;

/**
 * High-level card service that orchestrates reading and creating
 * Valentine cards using steganography.
 */
@Service
public class CardService {

    private final SteganographyService steganographyService;

    public CardService(SteganographyService steganographyService) {
        this.steganographyService = steganographyService;
    }

    /**
     * Reads (decodes) a hidden message from an uploaded image.
     *
     * @param imageFile the uploaded image file
     * @return CardReadResponse with decoded message or error info
     */
    public CardReadResponse readCard(MultipartFile imageFile) {
        CardReadResponse response = new CardReadResponse();

        if (imageFile == null || imageFile.isEmpty()) {
            response.setStatus("ERROR");
            response.setMessageFound(false);
            response.setErrorMessage("No image file provided.");
            return response;
        }

        try {
            byte[] imageBytes = imageFile.getBytes();
            String decodedMessage = steganographyService.decode(imageBytes);

            if (decodedMessage != null && !decodedMessage.isEmpty()) {
                response.setMessage(decodedMessage);
                response.setMessageFound(true);
                response.setStatus("SUCCESS");
            } else {
                response.setMessageFound(false);
                response.setStatus("NO_MESSAGE");
                response.setErrorMessage("No hidden message found in this image.");
            }
        } catch (IOException e) {
            response.setStatus("ERROR");
            response.setMessageFound(false);
            response.setErrorMessage("Failed to process image: " + e.getMessage());
        }

        return response;
    }

    /**
     * Creates a Valentine card by encoding a message into an image.
     *
     * @param imageFile the background image file
     * @param message   the secret message to encode
     * @return CardCreateResponse with encoded image bytes or error info
     */
    public CardCreateResponse createCard(MultipartFile imageFile, String message) {
        CardCreateResponse response = new CardCreateResponse();

        if (imageFile == null || imageFile.isEmpty()) {
            response.setStatus("ERROR");
            response.setErrorMessage("No image file provided.");
            return response;
        }

        if (message == null || message.trim().isEmpty()) {
            response.setStatus("ERROR");
            response.setErrorMessage("Message cannot be empty.");
            return response;
        }

        try {
            byte[] imageBytes = imageFile.getBytes();
            byte[] encodedImage = steganographyService.encode(imageBytes, message);

            response.setImageData(encodedImage);
            response.setStatus("SUCCESS");

            String originalName = imageFile.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
                response.setOriginalFilename(baseName + "_valentine.png");
            } else {
                response.setOriginalFilename("valentine_card.png");
            }
        } catch (IllegalArgumentException e) {
            response.setStatus("ERROR");
            response.setErrorMessage(e.getMessage());
        } catch (IOException e) {
            response.setStatus("ERROR");
            response.setErrorMessage("Failed to process image: " + e.getMessage());
        }

        return response;
    }
}
