package org.vs.valentine.cards.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.vs.valentine.cards.model.universal.CardConstants;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Service for sending Valentine cards via email with steganography images attached.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@valentine-cards.com}")
    private String fromEmail;

    @Value("${valentine.cards.email.enabled:false}")
    private boolean emailEnabled;

    /**
     * Sends a Valentine card via email with the encoded image attached.
     *
     * @param recipientEmail Recipient's email address
     * @param imageData PNG image data with hidden message
     * @param subject Email subject (optional, uses default if null)
     * @param senderName Sender's name (optional)
     * @return true if email sent successfully, false otherwise
     */
    public boolean sendCard(String recipientEmail, byte[] imageData, String subject, String senderName) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Set valentine.cards.email.enabled=true to enable.");
            // For development/testing, just log and return success
            log.info("SIMULATED: Email would be sent to {} with subject '{}'", recipientEmail, subject);
            return true;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, CardConstants.MESSAGE_ENCODING);

            // Set email properties
            helper.setFrom(fromEmail);
            helper.setTo(recipientEmail);
            helper.setSubject(subject != null ? subject : CardConstants.DEFAULT_EMAIL_SUBJECT);

            // Build email body
            String emailBody = buildEmailBody(senderName);
            helper.setText(emailBody, false);

            // Attach the steganography image
            helper.addAttachment("valentine-card.png", new ByteArrayResource(imageData));

            // Send the email
            mailSender.send(message);

            log.info("Successfully sent Valentine card to {}", recipientEmail);
            return true;

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", recipientEmail, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Builds the email body text.
     */
    private String buildEmailBody(String senderName) {
        if (senderName != null && !senderName.trim().isEmpty()) {
            return String.format(
                "Someone special has sent you a Valentine's Card with a hidden message!\n\n" +
                "From: %s\n\n" +
                "Download the attached image and upload it to our Valentine Cards app to reveal the secret message.\n\n" +
                "Visit: http://localhost:8080\n\n" +
                "With love,\nValentine Cards Team ❤️",
                senderName
            );
        }
        return CardConstants.EMAIL_BODY_TEMPLATE;
    }

    /**
     * Validates an email address format.
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Simple email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
