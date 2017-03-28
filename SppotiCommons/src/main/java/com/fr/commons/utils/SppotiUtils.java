package com.fr.commons.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.codec.Base64;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * Created by wdjenane on 16/02/2017.
 */
public class SppotiUtils {
    /**
     * Coder une chaine de caractères en 64 bit
     *
     * @param content string to code.
     * @return une chaine de caractère.
     */
    public static String encodeTo64(final String content) {
        final byte[] encodedBytes = Base64.encode(content.getBytes());
        return new String(encodedBytes);
    }

    /**
     * Décoder une chaine de caractères codée en 64bit.
     *
     * @param content string to decode.
     * @return une chaine de caractère.
     */
    public static String decode64ToString(final String content) {
        final byte[] decodedBytes = Base64.decode(content.getBytes());
        return new String(decodedBytes);
    }

    /**
     * Create a random long value.
     *
     * @param scale nombre de chiffre.
     * @return un Long random.
     */
    public static Long randomLong(final int scale) {
        return new Integer(randomString(scale).hashCode()).longValue();
    }

    /**
     * Create a random string value.
     *
     * @param size nombre de caractère
     * @return un String random
     */
    private static String randomString(final int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    /**
     * Create a random string value.
     *
     * @param size nombre de caractère.
     * @return un String random.
     */
    public static Integer randomInteger(final int size) {
        return randomString(size).hashCode();
    }

    /**
     * @return generated key used in all confirmations.
     */
    public static String generateConfirmationKey() {
        return UUID.randomUUID().toString() + "-" + SppotiUtils.randomString(40) + UUID.randomUUID().toString();

    }

    /**
     * @param dateToCheck date to verify.
     * @return true if expired.
     */
    public static boolean isDateExpired(Date dateToCheck) {

        LocalDate dateToVerify = dateToCheck.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return !dateToVerify.isAfter(LocalDate.now());
    }

    /**
     *
     * @param expiryDateNumber number of days before expiration.
     * @return expiry date.
     */
    public static Date generateExpiryDate(int expiryDateNumber){

        LocalDate expiryLocalDate = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return Date.from((expiryLocalDate.plusDays(expiryDateNumber)).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
