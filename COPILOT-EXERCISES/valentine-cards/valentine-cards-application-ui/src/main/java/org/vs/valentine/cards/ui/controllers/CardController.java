package org.vs.valentine.cards.ui.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vs.valentine.cards.domain.services.EmailService;
import org.vs.valentine.cards.domain.services.ImageProcessingService;
import org.vs.valentine.cards.domain.services.SteganographyService;
import org.vs.valentine.cards.model.dtos.CardCreationRequest;
import org.vs.valentine.cards.model.dtos.CardCreationResponse;
import org.vs.valentine.cards.model.dtos.CardReadResponse;
import org.vs.valentine.cards.model.universal.CardConstants;
import org.vs.valentine.cards.model.universal.OperationStatus;

import java.awt.image.BufferedImage;
import java.util.Base64;

/**
 * Controller for Valentine card operations (create, read, send).
 */
@Slf4j
@Controller
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final SteganographyService steganographyService;
    private final ImageProcessingService imageProcessingService;
    private final EmailService emailService;

    /**
     * Reads and decodes a Valentine card from an uploaded image.
     */
    @PostMapping("/read")
    @ResponseBody
    public ResponseEntity<CardReadResponse> readCard(@RequestParam("image") MultipartFile imageFile) {
        try {
            log.info("Reading card from uploaded image: {}", imageFile.getOriginalFilename());

            // Load and validate the image
            BufferedImage image = imageProcessingService.loadImage(imageFile);

            // Decode the hidden message
            String decodedMessage = steganographyService.decodeMessage(image);

            if (decodedMessage == null || decodedMessage.isEmpty()) {
                return ResponseEntity.ok(CardReadResponse.builder()
                    .status(OperationStatus.NO_MESSAGE_FOUND)
                    .errorMessage("No hidden message found in this image. Make sure it's a valid Valentine card.")
                    .messageFound(false)
                    .build());
            }

            log.info("Successfully decoded message of {} characters", decodedMessage.length());

            return ResponseEntity.ok(CardReadResponse.builder()
                .status(OperationStatus.SUCCESS)
                .message(decodedMessage)
                .messageFound(true)
                .build());

        } catch (IllegalArgumentException e) {
            log.warn("Invalid input for reading card: {}", e.getMessage());
            return ResponseEntity.badRequest().body(CardReadResponse.builder()
                .status(OperationStatus.INVALID_INPUT)
                .errorMessage(e.getMessage())
                .messageFound(false)
                .build());

        } catch (Exception e) {
            log.error("Error reading card: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CardReadResponse.builder()
                    .status(OperationStatus.FAILURE)
                    .errorMessage("Failed to read card: " + e.getMessage())
                    .messageFound(false)
                    .build());
        }
    }

    /**
     * Creates a new Valentine card with a hidden message.
     */
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<CardCreationResponse> createCard(
            @RequestParam("message") String message,
            @RequestParam("recipientEmail") String recipientEmail,
            @RequestParam(value = "senderName", required = false) String senderName,
            @RequestParam(value = "emailSubject", required = false) String emailSubject,
            @RequestParam("backgroundImage") MultipartFile backgroundImage) {

        try {
            log.info("Creating Valentine card with message length: {}", message.length());

            // Validate inputs
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(CardCreationResponse.builder()
                    .status(OperationStatus.INVALID_INPUT)
                    .message("Message cannot be empty")
                    .emailSent(false)
                    .build());
            }

            if (message.length() > CardConstants.MAX_MESSAGE_LENGTH) {
                return ResponseEntity.badRequest().body(CardCreationResponse.builder()
                    .status(OperationStatus.INVALID_INPUT)
                    .message("Message too long. Maximum " + CardConstants.MAX_MESSAGE_LENGTH + " characters")
                    .emailSent(false)
                    .build());
            }

            if (!emailService.isValidEmail(recipientEmail)) {
                return ResponseEntity.badRequest().body(CardCreationResponse.builder()
                    .status(OperationStatus.INVALID_INPUT)
                    .message("Invalid email address")
                    .emailSent(false)
                    .build());
            }

            // Load the background image
            BufferedImage backgroundImg = imageProcessingService.loadImage(backgroundImage);

            // Check if image can hold the message
            if (!imageProcessingService.canHoldMessage(backgroundImg, message.length())) {
                return ResponseEntity.badRequest().body(CardCreationResponse.builder()
                    .status(OperationStatus.INVALID_INPUT)
                    .message("Background image is too small to hold this message. Try a larger image or shorter message.")
                    .emailSent(false)
                    .build());
            }

            // Encode the message into the image
            byte[] encodedImage = steganographyService.encodeMessage(backgroundImg, message);

            // Send via email
            String subject = (emailSubject != null && !emailSubject.trim().isEmpty()) 
                ? emailSubject 
                : CardConstants.DEFAULT_EMAIL_SUBJECT;

//            boolean emailSent = false;
//            emailSent = true;
            boolean emailSent = emailService.sendCard(recipientEmail, encodedImage, subject, senderName);


            // Create base64 preview for display
            String imagePreview = Base64.getEncoder().encodeToString(encodedImage);

            if (emailSent) {
                log.info("Successfully created and sent Valentine card to {}", recipientEmail);
                return ResponseEntity.ok(CardCreationResponse.builder()
                    .status(OperationStatus.SUCCESS)
                    .message("Valentine card created and sent successfully!")
                    .emailSent(true)
                    .imagePreview(imagePreview)
                    .build());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CardCreationResponse.builder()
                        .status(OperationStatus.FAILURE)
                        .message("Card created but failed to send email. Please check email configuration.")
                        .emailSent(false)
                        .imagePreview(imagePreview)
                        .build());
            }

        } catch (IllegalArgumentException e) {
            log.warn("Invalid input for creating card: {}", e.getMessage());
            return ResponseEntity.badRequest().body(CardCreationResponse.builder()
                .status(OperationStatus.INVALID_INPUT)
                .message(e.getMessage())
                .emailSent(false)
                .build());

        } catch (Exception e) {
            log.error("Error creating card: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CardCreationResponse.builder()
                    .status(OperationStatus.FAILURE)
                    .message("Failed to create card: " + e.getMessage())
                    .emailSent(false)
                    .build());
        }
    }

    /**
     * Downloads the created card image.
     */
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadCard(
            @RequestParam("message") String message,
            @RequestParam("backgroundImage") MultipartFile backgroundImage) {

        try {
            // Load the background image
            BufferedImage backgroundImg = imageProcessingService.loadImage(backgroundImage);

            // Encode the message
            byte[] encodedImage = steganographyService.encodeMessage(backgroundImg, message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "valentine-card.png");

            return new ResponseEntity<>(encodedImage, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error downloading card: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
