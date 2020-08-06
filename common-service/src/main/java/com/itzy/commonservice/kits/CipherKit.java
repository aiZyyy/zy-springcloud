package com.itzy.commonservice.kits;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA
 *
 * @author MiaoWoo
 * Created on 2019/1/16 14:16.
 */
public class CipherKit {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        String iv = UUID.randomUUID().toString().split("-")[0];
        System.out.println(iv);
        String encrypt = CipherKit.AesCbc.encrypt("SixiEncryptTest", "BaseCustomerService", iv);
        System.out.println(encrypt);
        System.out.println(CipherKit.AesCbc.decrypt(encrypt, "BaseCustomerService", iv));
    }

    public static byte[] paddingKey(String iv, int base) {
        byte[] bytes = iv.getBytes(CHARSET);
        byte[] temp = new byte[base];
        Arrays.fill(temp, (byte) 0);
        System.arraycopy(bytes, 0, temp, 0, bytes.length > base ? base : bytes.length);
        return temp;
    }

    /**
     * AES-CBC加密工具类
     * Key长度不足用0补位
     * 内容使用PKCS5Padding补齐
     */
    public static class AesCbc {
        @SneakyThrows
        public static String encrypt(String srcStr, String keyStr, String ivStr) {
            return Base64.getEncoder().encodeToString(handle(srcStr.getBytes(CHARSET), keyStr, ivStr, Cipher.ENCRYPT_MODE));
        }

        public static String decrypt(String srcStr, String keyStr, String ivStr) {
            return new String(handle(Base64.getDecoder().decode(srcStr), keyStr, ivStr, Cipher.DECRYPT_MODE));
        }

        @SneakyThrows
        private static byte[] handle(byte[] srcByte, String keyStr, String ivStr, int mode) {
            Cipher cipher = getAesCbcCipher();
            cipher.init(mode, getSecretKeySpec(keyStr), getIvParameterSpec(ivStr));
            return cipher.doFinal(srcByte);
        }
    }

    private static IvParameterSpec getIvParameterSpec(String iv) {
        return new IvParameterSpec(paddingKey(iv, 16));
    }

    private static Key getSecretKeySpec(String keySeed) {
        return new SecretKeySpec(paddingKey(keySeed, 16), "AES");
    }

    @SneakyThrows
    private static Cipher getAesCbcCipher() {
        return Cipher.getInstance("AES/CBC/PKCS5Padding");
    }
}
