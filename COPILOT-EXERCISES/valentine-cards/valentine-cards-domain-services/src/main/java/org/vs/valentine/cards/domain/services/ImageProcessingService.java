package org.vs.valentine.cards.domain.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vs.valentine.cards.model.universal.CardConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Service for processing and validating images for Valentine cards.
 */
@Slf4j
@Service
public class ImageProcessingService {

    /**
     * Loads an image from a MultipartFile and validates it.
     *
     * @param file The uploaded image file
     * @return BufferedImage representation
     * @throws IOException if file reading fails
     * @throws IllegalArgumentException if validation fails
     */
    public BufferedImage loadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        // Validate file size
        if (file.getSize() > CardConstants.MAX_IMAGE_SIZE_BYTES) {
            throw new IllegalArgumentException(
                String.format("Image file too large. Maximum size: %d MB", 
                    CardConstants.MAX_IMAGE_SIZE_BYTES / (1024 * 1024))
            );
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Read the image
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            
            if (image == null) {
                throw new IllegalArgumentException("Invalid or corrupted image file");
            }

            log.info("Successfully loaded image: {}x{} pixels, size: {} bytes", 
                image.getWidth(), image.getHeight(), file.getSize());

            return image;
        }
    }

    /**
     * Loads an image from byte array.
     *
     * @param imageBytes Image data as byte array
     * @return BufferedImage representation
     * @throws IOException if image reading fails
     */
    public BufferedImage loadImage(byte[] imageBytes) throws IOException {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("Image data is required");
        }

        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bais);

        if (image == null) {
            throw new IllegalArgumentException("Invalid or corrupted image data");
        }

        return image;
    }

    /**
     * Converts a BufferedImage to byte array in PNG format.
     *
     * @param image The image to convert
     * @return PNG image as byte array
     * @throws IOException if conversion fails
     */
    public byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean written = ImageIO.write(image, CardConstants.DEFAULT_IMAGE_FORMAT, baos);
        
        if (!written) {
            throw new IOException("Failed to write image to PNG format");
        }

        return baos.toByteArray();
    }

    /**
     * Validates if an image is suitable for steganography.
     * Checks dimensions and capacity.
     *
     * @param image The image to validate
     * @param messageLength Length of the message to hide
     * @return true if image can hold the message
     */
    public boolean canHoldMessage(BufferedImage image, int messageLength) {
        if (image == null) {
            return false;
        }

        // Calculate available bits (3 color channels per pixel)
        int availableBits = image.getWidth() * image.getHeight() * 3;
        
        // Calculate required bits (including marker and length metadata)
        String fullPayload = CardConstants.STEGO_MARKER + "|" + messageLength + "|";
        int metadataBytes = fullPayload.getBytes().length;
        int requiredBits = (metadataBytes + messageLength) * 8;

        return availableBits >= requiredBits;
    }

    /**
     * Gets the maximum message size that can be hidden in an image.
     *
     * @param image The carrier image
     * @return Maximum message length in characters
     */
    public int getMaxMessageCapacity(BufferedImage image) {
        if (image == null) {
            return 0;
        }

        int availableBits = image.getWidth() * image.getHeight() * 3;
        int availableBytes = availableBits / 8;
        
        // Subtract metadata overhead
        int metadataOverhead = CardConstants.STEGO_MARKER.length() + 10; // marker + length field
        
        return Math.max(0, availableBytes - metadataOverhead);
    }

    /**
     * Checks if a file extension is supported.
     *
     * @param filename The filename to check
     * @return true if the file type is supported
     */
    public boolean isSupportedFormat(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        String extension = getFileExtension(filename);
        return Arrays.asList(CardConstants.SUPPORTED_IMAGE_FORMATS).contains(extension);
    }

    /**
     * Extracts file extension from filename.
     */
    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filename.length() - 1) {
            return filename.substring(lastDot + 1);
        }
        return "";
    }
}
