package com.cicd.credentials.security.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecretEncryptionConverterTest {

    private SecretEncryptionConverter converter;

    @BeforeEach
    void setUp() {
        converter = new SecretEncryptionConverter();
    }

    @Test
    void testEncryptAndDecrypt_Success() {
        // Arrange
        String originalSecret = "ghp_superSecretGitHubToken12345";
        String encrypted = converter.convertToDatabaseColumn(originalSecret);
        String decrypted = converter.convertToEntityAttribute(encrypted);

        assertNotNull(encrypted);
        assertNotEquals(originalSecret, encrypted, "Encrypted text should not match plain text");
        assertEquals(originalSecret, decrypted, "Decrypted text should match the original plain text");
    }

    @Test
    void testNullValues_ShouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }
}