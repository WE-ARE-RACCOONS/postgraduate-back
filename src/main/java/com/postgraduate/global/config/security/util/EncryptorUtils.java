package com.postgraduate.global.config.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class EncryptorUtils {
    private final AesBytesEncryptor encryptor;

    public String encryptData(String data) {
        byte[] encrypt = encryptor.encrypt(data.getBytes(UTF_8));
        return byteArrayToString(encrypt);
    }

    public String decryptData(String data) {
        byte[] decryptBytes = stringToByteArray(data);
        byte[] decrypt = encryptor.decrypt(decryptBytes);
        return new String(decrypt, UTF_8);
    }

    public String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte abyte : bytes) {
            sb.append(abyte);
            sb.append(" ");
        }
        return sb.toString();
    }

    public byte[] stringToByteArray(String byteString) {
        String[] split = byteString.split("\\s");
        ByteBuffer buffer = ByteBuffer.allocate(split.length);
        for (String s : split) {
            buffer.put((byte) Integer.parseInt(s));
        }
        return buffer.array();
    }
}
