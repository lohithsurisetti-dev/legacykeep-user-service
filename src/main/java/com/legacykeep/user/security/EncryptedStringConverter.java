package com.legacykeep.user.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * JPA Attribute Converter for encrypting/decrypting sensitive string data.
 * 
 * This converter automatically encrypts sensitive data when saving to the database
 * and decrypts it when reading from the database, providing transparent encryption
 * for entity fields.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Converter
@Component
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    @Value("${user.encryption.secret-key:default-encryption-key-change-in-production}")
    private String secretKey;

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * Converts a plain text string to an encrypted string for database storage.
     * 
     * @param plainText the plain text string to encrypt
     * @return the encrypted string, or null if input is null
     */
    @Override
    public String convertToDatabaseColumn(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            return null;
        }
        
        try {
            SecretKeySpec secretKeySpec = generateKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting sensitive data", e);
        }
    }

    /**
     * Converts an encrypted string from the database to plain text.
     * 
     * @param encryptedText the encrypted string from the database
     * @return the decrypted plain text string, or null if input is null
     */
    @Override
    public String convertToEntityAttribute(String encryptedText) {
        if (encryptedText == null || encryptedText.trim().isEmpty()) {
            return null;
        }
        
        try {
            SecretKeySpec secretKeySpec = generateKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting sensitive data", e);
        }
    }

    /**
     * Generates a secret key from the configured secret key string.
     * 
     * @return the SecretKeySpec for encryption/decryption
     */
    private SecretKeySpec generateKey() {
        byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
        // Ensure key is exactly 16 bytes for AES-128
        byte[] keyBytes = new byte[16];
        System.arraycopy(key, 0, keyBytes, 0, Math.min(key.length, 16));
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
}
