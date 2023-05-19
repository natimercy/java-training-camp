package com.acme;

import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String content = "hello world";
        Checksum checksum = new CRC32();
        byte[] bytes = content.getBytes();
        checksum.update(bytes, 0, bytes.length);
        System.out.println(checksum.getValue());
        // System.out.println(Integer.decode(DigestUtils.md5DigestAsHex()));
    }

}
