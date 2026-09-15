package com.example.supportTicketManagement.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;

import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

@Converter
public class Encryptor implements AttributeConverter<Object, String> {

    @Value("${encryption.key}")
    private String encryptionKey;

    @Value("${encryption.cipher}")
    private String encryptionCipher;

    private Key key;
    private Cipher cipher;

    // Getter method for creating object for Key
    private Key getKey() {
        if (key == null)
            key = new SecretKeySpec(encryptionKey.getBytes(), encryptionCipher);

        return key;
    }

    // Getter method for creating object for cipher
    private Cipher getCipher() throws GeneralSecurityException {
        if (cipher == null)
            cipher = Cipher.getInstance(encryptionCipher);

        return cipher;
    }

    // Initializing method for encryption or decryption
    private void initCipher(int encryptMode) throws GeneralSecurityException {
        getCipher().init(encryptMode, getKey());
    }


    // Stores the encrypted format in DB
    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Object attribute){
        if(attribute == null)
            return null;

        initCipher(Cipher.ENCRYPT_MODE);

        byte[] bytes = SerializationUtils.serialize(attribute);

        return Base64.getEncoder().encodeToString(getCipher().doFinal(bytes));
    }

    // Returns the decrypted format from db
    @SneakyThrows
    @Override
    public Object convertToEntityAttribute(String dbData) {
       if(dbData == null)
           return null;

       initCipher(Cipher.DECRYPT_MODE);

       byte[] encryptedBytes = Base64.getDecoder().decode(dbData);
       byte[] bytes = cipher.doFinal(encryptedBytes);

       try(ObjectInputStream ois = new ObjectInputStream(
               new ByteArrayInputStream(bytes)
       )) {
            return  ois.readObject();
       }
    }
}
