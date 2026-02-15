package org.valentines.cards.reader.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.valentines.cards.reader.dto.CardCreateResponse;
import org.valentines.cards.reader.dto.CardReadResponse;
import org.valentines.cards.reader.service.CardService;

/**
 * Main controller for the Valentine Love Heart Cards application.
 *
 * Routes:
 *   GET  /             → Home page (upload image to read)
 *   POST /cards/read   → Process uploaded image, decode message, redirect to view
 *   GET  /view         → View decoded message
 *   GET  /create       → Create card form
 *   POST /cards/create → Encode message into image, return downloadable PNG
 */
@Controller
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Home page — upload an image to decode a hidden message.
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * Process uploaded image: decode the hidden message and show it on the view page.
     */
    @PostMapping("/cards/read")
    public String readCard(@RequestParam("image") MultipartFile imageFile, Model model) {
        CardReadResponse response = cardService.readCard(imageFile);
        model.addAttribute("response", response);
        return "view";
    }

    /**
     * View page — display a decoded message (direct access without POST shows empty).
     */
    @GetMapping("/view")
    public String view(Model model) {
        // Direct GET access: show empty state
        CardReadResponse emptyResponse = new CardReadResponse();
        emptyResponse.setStatus("EMPTY");
        emptyResponse.setMessageFound(false);
        model.addAttribute("response", emptyResponse);
        return "view";
    }

    /**
     * Create page — form for writing a message and selecting a background image.
     */
    @GetMapping("/create")
    public String create() {
        return "create";
    }

    /**
     * Process card creation: encode message into image and return PNG download.
     */
    @PostMapping("/cards/create")
    public ResponseEntity<byte[]> createCard(
            @RequestParam("message") String message,
            @RequestParam("image") MultipartFile imageFile) {

        CardCreateResponse response = cardService.createCard(imageFile, message);

        if ("SUCCESS".equals(response.getStatus()) && response.getImageData() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", response.getOriginalFilename());
            headers.setContentLength(response.getImageData().length);
            return new ResponseEntity<>(response.getImageData(), headers, HttpStatus.OK);
        } else {
            // On error, return a simple error response
            String errorMsg = response.getErrorMessage() != null
                    ? response.getErrorMessage()
                    : "Failed to create card.";
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorMsg.getBytes());
        }
    }
}
