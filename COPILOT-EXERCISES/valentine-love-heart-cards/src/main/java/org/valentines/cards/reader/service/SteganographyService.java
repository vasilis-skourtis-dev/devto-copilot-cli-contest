package org.valentines.cards.reader.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * LSB (Least Significant Bit) Steganography Service.
 *
 * Encodes and decodes secret messages into PNG images by manipulating
 * the least significant bits of pixel RGB values.
 *
 * Format:
 *   - First 32 bits: message length (in bytes)
 *   - Remaining bits: UTF-8 encoded message bytes
 *   - Each bit is stored in the LSB of one color channel (R, G, or B)
 */
@Service
public class SteganographyService {

    /**
     * Encodes a text message into an image using LSB steganography.
     *
     * @param imageBytes the original image as byte array (PNG or JPEG)
     * @param message    the secret message to hide
     * @return PNG byte array with the message encoded
     * @throws IOException              if image cannot be read/written
     * @throws IllegalArgumentException if message is too large for the image
     */
    public byte[] encode(byte[] imageBytes, String message) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (image == null) {
            throw new IOException("Cannot read image from provided bytes.");
        }

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        int messageLength = messageBytes.length;

        // Total bits needed: 32 (length header) + message bits
        int totalBitsNeeded = 32 + (messageLength * 8);
        int availableBits = image.getWidth() * image.getHeight() * 3; // 3 channels per pixel

        if (totalBitsNeeded > availableBits) {
            throw new IllegalArgumentException(
                "Message too large for this image. Need " + totalBitsNeeded
                + " bits but only " + availableBits + " available."
            );
        }

        int bitIndex = 0;

        // Step 1: Encode message length (32 bits, big-endian)
        for (int i = 31; i >= 0; i--) {
            int bit = (messageLength >> i) & 1;
            setPixelBit(image, bitIndex, bit);
            bitIndex++;
        }

        // Step 2: Encode message bytes
        for (byte b : messageBytes) {
            for (int i = 7; i >= 0; i--) {
                int bit = (b >> i) & 1;
                setPixelBit(image, bitIndex, bit);
                bitIndex++;
            }
        }

        // Write to PNG
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return output.toByteArray();
    }

    /**
     * Decodes a hidden message from an image using LSB steganography.
     *
     * @param imageBytes the image containing the hidden message
     * @return the decoded message, or null if no valid message found
     * @throws IOException if image cannot be read
     */
    public String decode(byte[] imageBytes) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (image == null) {
            throw new IOException("Cannot read image from provided bytes.");
        }

        int availableBits = image.getWidth() * image.getHeight() * 3;
        if (availableBits < 32) {
            return null; // Image too small to contain a message
        }

        int bitIndex = 0;

        // Step 1: Read message length (32 bits)
        int messageLength = 0;
        for (int i = 31; i >= 0; i--) {
            int bit = getPixelBit(image, bitIndex);
            messageLength |= (bit << i);
            bitIndex++;
        }

        // Sanity check: message length must be reasonable
        if (messageLength <= 0 || messageLength > 1000000) {
            return null; // No valid message
        }

        int messageBitsNeeded = messageLength * 8;
        if ((32 + messageBitsNeeded) > availableBits) {
            return null; // Message length exceeds image capacity
        }

        // Step 2: Read message bytes
        byte[] messageBytes = new byte[messageLength];
        for (int byteIdx = 0; byteIdx < messageLength; byteIdx++) {
            byte b = 0;
            for (int i = 7; i >= 0; i--) {
                int bit = getPixelBit(image, bitIndex);
                b |= (bit << i);
                bitIndex++;
            }
            messageBytes[byteIdx] = b;
        }

        return new String(messageBytes, StandardCharsets.UTF_8);
    }

    /**
     * Sets a single bit in the image at the given bit index.
     * Bit index maps to pixel (x, y) and channel (R=0, G=1, B=2).
     */
    private void setPixelBit(BufferedImage image, int bitIndex, int bit) {
        int pixelIndex = bitIndex / 3;
        int channel = bitIndex % 3;

        int x = pixelIndex % image.getWidth();
        int y = pixelIndex / image.getWidth();

        int rgb = image.getRGB(x, y);

        int[] channels = new int[]{
            (rgb >> 16) & 0xFF, // R
            (rgb >> 8) & 0xFF,  // G
            rgb & 0xFF          // B
        };

        // Clear LSB and set new bit
        channels[channel] = (channels[channel] & 0xFE) | bit;

        int newRgb = (rgb & 0xFF000000) // preserve alpha
            | (channels[0] << 16)
            | (channels[1] << 8)
            | channels[2];

        image.setRGB(x, y, newRgb);
    }

    /**
     * Gets a single bit from the image at the given bit index.
     */
    private int getPixelBit(BufferedImage image, int bitIndex) {
        int pixelIndex = bitIndex / 3;
        int channel = bitIndex % 3;

        int x = pixelIndex % image.getWidth();
        int y = pixelIndex / image.getWidth();

        int rgb = image.getRGB(x, y);

        int[] channels = new int[]{
            (rgb >> 16) & 0xFF, // R
            (rgb >> 8) & 0xFF,  // G
            rgb & 0xFF          // B
        };

        return channels[channel] & 1;
    }
}
