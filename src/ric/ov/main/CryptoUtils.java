package ric.ov.main;

import java.security.*;

final class CryptoUtils
{
    //========================================================================= INITIALIZE
    private CryptoUtils()
    {
        throw new AssertionError();
    }

    //========================================================================= FUNCTIONS
    public static String md5(String str) throws NoSuchAlgorithmException
    {
        return hash(str, "MD5");
    }
    private static String hash(String str, String algorithm) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] bytes = digest.digest(str.getBytes());

        StringBuilder builder = new StringBuilder();
        for (byte b : bytes)
            builder.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));

        return builder.toString();
    }
}
