package com.cicd.credentials.security.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * JPA AttributeConverter responsible for encrypting and decrypting sensitive secret values
 * before storing them in the database and after reading them from it.
 * The converter uses AES encryption with a symmetric key.
 */
@Converter
public class SecretEncryptionConverter implements AttributeConverter<String, String> {

    private static final Logger log = LoggerFactory.getLogger(SecretEncryptionConverter.class);
    private static final String ALGORITHM = "AES";

    // IMPORTANT: For demo purposes only - hardcoded key is NOT secure for production use.
    // The key MUST be 16 bytes long for AES-128 encryption.
    // In real applications this should come from environment variables or a secure vault.
    private static final byte[] KEY_BYTES = "Cccccccccccccccc".getBytes(java.nio.charset.StandardCharsets.UTF_8);

    /**
     * Temp method
     * @param mode
     * @return
     * @throws Exception
     */
    private Cipher createCipher(int mode) throws Exception {
        Key key = new SecretKeySpec(KEY_BYTES, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, key);
        return cipher;
    }
    @Override
    public String convertToDatabaseColumn(String plainText) {
        Cipher cipher;
        if (plainText == null) return null;
        try {
            cipher = createCipher(Cipher.ENCRYPT_MODE);
            log.debug("Successfully encrypted sensitive database column data.");
            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(plainText.getBytes())
            );
        } catch (Exception e) {
            log.error("Crypto Error: Failed to encrypt entity data. Target AES configuration might be corrupted.", e);
            throw new IllegalStateException("Error encrypting secret data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encryptedText) {
        Cipher cipher;
        if (encryptedText == null) return null;
        try {
            cipher = createCipher(Cipher.DECRYPT_MODE);
            log.debug("Successfully decrypted sensitive column data into plaintext.");
            return new String(
                    cipher.doFinal(Base64.getDecoder().decode(encryptedText))
            );
        } catch (Exception e) {
            log.error("Crypto Error: Decryption failed. The encryption key or data padding might be invalid.", e);
            throw new IllegalStateException("Error decrypting secret data", e);
        }
    }
}