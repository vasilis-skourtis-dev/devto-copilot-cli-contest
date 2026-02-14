package org.vs.valentine.cards.domain.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vs.valentine.cards.model.universal.CardConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Service for hiding and revealing messages in images using LSB Steganography.
 * Uses Least Significant Bit (LSB) algorithm to embed messages in PNG images.
 */
@Slf4j
@Service
public class SteganographyService {

    private static final int BITS_PER_BYTE = 8;
    private static final int MARKER_BIT_LENGTH = CardConstants.STEGO_MARKER.length() * BITS_PER_BYTE;

    /**
     * Encodes a secret message into an image using LSB steganography.
     *
     * @param sourceImage The carrier image
     * @param message The secret message to hide
     * @return Byte array of the PNG image with hidden message
     * @throws IllegalArgumentException if message is too long or image too small
     * @throws IOException if image processing fails
     */
    public byte[] encodeMessage(BufferedImage sourceImage, String message) throws IOException {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        if (message.length() > CardConstants.MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException(
                "Message too long. Maximum length: " + CardConstants.MAX_MESSAGE_LENGTH + " characters"
            );
        }

        // Create a copy of the image to avoid modifying the original
        BufferedImage imageWithMessage = copyImage(sourceImage);

        // Prepare the full payload: MARKER + MESSAGE_LENGTH + MESSAGE
        String fullPayload = CardConstants.STEGO_MARKER + "|" + message.length() + "|" + message;
        byte[] messageBytes = fullPayload.getBytes(StandardCharsets.UTF_8);

        // Calculate required capacity
        int requiredBits = messageBytes.length * BITS_PER_BYTE;
        int availableBits = imageWithMessage.getWidth() * imageWithMessage.getHeight() * 3; // RGB channels

        if (requiredBits > availableBits) {
            throw new IllegalArgumentException(
                "Image too small to hold message. Required: " + requiredBits + " bits, Available: " + availableBits + " bits"
            );
        }

        log.info("Encoding message of {} bytes into image of size {}x{}", 
            messageBytes.length, imageWithMessage.getWidth(), imageWithMessage.getHeight());

        // Encode the message into the image
        embedBits(imageWithMessage, messageBytes);

        // Convert to PNG byte array
        return imageToBytes(imageWithMessage);
    }

    /**
     * Decodes a secret message from an image using LSB steganography.
     *
     * @param imageBytes The byte array of the image containing the hidden message
     * @return The decoded secret message, or null if no message found
     * @throws IOException if image processing fails
     */
    public String decodeMessage(byte[] imageBytes) throws IOException {
        BufferedImage image = bytesToImage(imageBytes);
        return decodeMessage(image);
    }

    /**
     * Decodes a secret message from an image using LSB steganography.
     *
     * @param image The image containing the hidden message
     * @return The decoded secret message, or null if no message found
     */
    public String decodeMessage(BufferedImage image) {
        try {
            // First, try to read the marker
            String marker = extractMarker(image);
            
            if (!CardConstants.STEGO_MARKER.equals(marker)) {
                log.warn("Invalid or missing steganography marker. Expected: {}, Found: {}", 
                    CardConstants.STEGO_MARKER, marker);
                return null;
            }

            log.info("Valid marker found. Extracting message...");

            // Extract message length
            int markerBytes = CardConstants.STEGO_MARKER.getBytes(StandardCharsets.UTF_8).length;
            int messageLengthStart = (markerBytes + 1) * BITS_PER_BYTE; // +1 for "|" separator
            
            // Read until next "|" to get message length
            StringBuilder lengthStr = new StringBuilder();
            int bitIndex = messageLengthStart;
            
            while (true) {
                byte b = extractByte(image, bitIndex);
                char c = (char) (b & 0xFF);
                if (c == '|') {
                    break;
                }
                lengthStr.append(c);
                bitIndex += BITS_PER_BYTE;
            }

            int messageLength = Integer.parseInt(lengthStr.toString());
            log.info("Message length: {}", messageLength);

            if (messageLength <= 0 || messageLength > CardConstants.MAX_MESSAGE_LENGTH) {
                log.warn("Invalid message length: {}", messageLength);
                return null;
            }

            // Extract the actual message
            int messageStart = bitIndex + BITS_PER_BYTE; // Skip the "|" separator
            byte[] messageBytes = new byte[messageLength];
            
            for (int i = 0; i < messageLength; i++) {
                messageBytes[i] = extractByte(image, messageStart + (i * BITS_PER_BYTE));
            }

            String message = new String(messageBytes, StandardCharsets.UTF_8);
            log.info("Successfully decoded message of {} characters", message.length());
            
            return message;

        } catch (Exception e) {
            log.error("Error decoding message: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Embeds message bits into the image's pixels using LSB.
     */
    private void embedBits(BufferedImage image, byte[] messageBytes) {
        int width = image.getWidth();
        int height = image.getHeight();
        int byteIndex = 0;
        int bitIndex = 0;

        outerLoop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (byteIndex >= messageBytes.length) {
                    break outerLoop;
                }

                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Embed bits in R, G, B channels
                if (byteIndex < messageBytes.length) {
                    red = embedBit(red, getBit(messageBytes[byteIndex], bitIndex++));
                    if (bitIndex >= BITS_PER_BYTE) {
                        bitIndex = 0;
                        byteIndex++;
                    }
                }

                if (byteIndex < messageBytes.length) {
                    green = embedBit(green, getBit(messageBytes[byteIndex], bitIndex++));
                    if (bitIndex >= BITS_PER_BYTE) {
                        bitIndex = 0;
                        byteIndex++;
                    }
                }

                if (byteIndex < messageBytes.length) {
                    blue = embedBit(blue, getBit(messageBytes[byteIndex], bitIndex++));
                    if (bitIndex >= BITS_PER_BYTE) {
                        bitIndex = 0;
                        byteIndex++;
                    }
                }

                // Reconstruct pixel with embedded bits
                int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newPixel);
            }
        }
    }

    /**
     * Extracts the steganography marker from the image.
     */
    private String extractMarker(BufferedImage image) {
        byte[] markerBytes = CardConstants.STEGO_MARKER.getBytes(StandardCharsets.UTF_8);
        byte[] extractedBytes = new byte[markerBytes.length];

        for (int i = 0; i < markerBytes.length; i++) {
            extractedBytes[i] = extractByte(image, i * BITS_PER_BYTE);
        }

        return new String(extractedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Extracts a single byte from the image starting at the given bit position.
     */
    private byte extractByte(BufferedImage image, int startBitPosition) {
        int width = image.getWidth();
        int height = image.getHeight();
        byte result = 0;

        for (int bitIdx = 0; bitIdx < BITS_PER_BYTE; bitIdx++) {
            int absoluteBitPos = startBitPosition + bitIdx;
            int pixelIndex = absoluteBitPos / 3; // 3 channels per pixel
            int channelIndex = absoluteBitPos % 3;

            int x = pixelIndex % width;
            int y = pixelIndex / width;

            if (y >= height) {
                break; // Out of bounds
            }

            int pixel = image.getRGB(x, y);
            int channelValue;

            switch (channelIndex) {
                case 0: channelValue = (pixel >> 16) & 0xFF; break; // Red
                case 1: channelValue = (pixel >> 8) & 0xFF; break;  // Green
                default: channelValue = pixel & 0xFF; break;         // Blue
            }

            int bit = channelValue & 1; // Extract LSB
            result = (byte) (result | (bit << bitIdx));
        }

        return result;
    }

    /**
     * Gets the bit at the specified position in a byte.
     */
    private int getBit(byte b, int position) {
        return (b >> position) & 1;
    }

    /**
     * Embeds a single bit into a color channel value.
     */
    private int embedBit(int colorValue, int bit) {
        return (colorValue & 0xFE) | bit; // Clear LSB and set new bit
    }

    /**
     * Creates a deep copy of a BufferedImage.
     */
    private BufferedImage copyImage(BufferedImage source) {
        BufferedImage copy = new BufferedImage(
            source.getWidth(), 
            source.getHeight(), 
            BufferedImage.TYPE_INT_ARGB
        );
        copy.getGraphics().drawImage(source, 0, 0, null);
        return copy;
    }

    /**
     * Converts BufferedImage to byte array (PNG format).
     */
    private byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, CardConstants.DEFAULT_IMAGE_FORMAT, baos);
        return baos.toByteArray();
    }

    /**
     * Converts byte array to BufferedImage.
     */
    private BufferedImage bytesToImage(byte[] imageBytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(bais);
    }
}
