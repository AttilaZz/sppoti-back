package com.fr.commons.utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.codec.Base64;
/**
 * Created by wdjenane on 16/02/2017.
 */
public class SppotiUtils
{
    /**
     * Coder une chaine de caractères en 64 bit
     *
     * @param content
     * @return une chaine de caractère.
     */
    public static String convertTo64(final String content)
    {
        final byte[] encodedBytes = Base64.encode(content.getBytes());
        return new String(encodedBytes);
    }

    /**
     * Décoder une chaine de caractères codée en 64bit
     *
     * @param content
     * @return une chaine de caractère.
     */
    public static String decode64ToString(final String content)
    {
        final byte[] decodedBytes = Base64.decode(content.getBytes());
        return new String(decodedBytes);
    }

    /**
     * Create a random long value.
     *
     * @param scale nombre de chiffre
     * @return un Long random
     */
    public static Long randomLong(final int scale)
    {
        return new Integer(randomString(scale).hashCode()).longValue();
    }

    /**
     * Create a random string value.
     *
     * @param size nombre de caractère
     * @return un String random
     */
    public static String randomString(final int size)
    {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    /**
     * Create a random string value.
     *
     * @param size nombre de caractère.
     * @return un String random.
     */
    public static Integer randomInteger(final int size)
    {
        return randomString(size).hashCode();
    }
}
