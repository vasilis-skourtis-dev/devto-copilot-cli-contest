package org.vs.valentine.cards.model.universal;

/**
 * Constants for Valentine Cards application.
 * Defines limits and constraints for steganography and image processing.
 */
public final class CardConstants {

    private CardConstants() {
        // Utility class - prevent instantiation
    }

    /**
     * Maximum message length in characters (to fit in standard images)
     */
    public static final int MAX_MESSAGE_LENGTH = 10000;

    /**
     * Minimum message length
     */
    public static final int MIN_MESSAGE_LENGTH = 1;

    /**
     * Maximum image file size in bytes (5MB)
     */
    public static final long MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;

    /**
     * Supported image formats
     */
    public static final String[] SUPPORTED_IMAGE_FORMATS = {"PNG", "png"};

    /**
     * Default image format for generated cards
     */
    public static final String DEFAULT_IMAGE_FORMAT = "PNG";

    /**
     * Steganography marker to identify encoded messages
     */
    public static final String STEGO_MARKER = "VCSTEGO";

    /**
     * Character encoding for messages
     */
    public static final String MESSAGE_ENCODING = "UTF-8";

    /**
     * Maximum email subject length
     */
    public static final int MAX_EMAIL_SUBJECT_LENGTH = 200;

    /**
     * Default email subject
     */
    public static final String DEFAULT_EMAIL_SUBJECT = "💌 You've received a Valentine's Card!";

    /**
     * Email body template
     */
    public static final String EMAIL_BODY_TEMPLATE = 
        "Someone special has sent you a Valentine's Card with a hidden message!\n\n" +
        "Download the attached image and upload it to our Valentine Cards app to reveal the secret message.\n\n" +
        "With love,\nValentine Cards Team ❤️";
}
