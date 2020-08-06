package com.itzy.commonservice.kits;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA
 *
 * @author MiaoWoo
 * Created on 2018/9/7 10:23.
 */
public class DigestKit {
    private static final ThreadLocal<MessageDigest> MD5 = ThreadLocal.withInitial(() -> getMessageDigestInstance("MD5"));
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    @SneakyThrows
    private static MessageDigest getMessageDigestInstance(String algorithm) {
        return MessageDigest.getInstance(algorithm);
    }

    @SneakyThrows
    public static String md5(String srcStr) {
        return toHex(MD5.get().digest(srcStr.getBytes(StandardCharsets.UTF_8)));
    }

    private static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }
}
