package com.fr.commons.utils;

import com.fr.commons.exception.BusinessGlobalException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.codec.Base64;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by wdjenane on 16/02/2017.
 */
public class SppotiUtils
{
	/**
	 * Coder une chaine de caractères en 64 bit
	 *
	 * @param content
	 * 		string to code.
	 *
	 * @return une chaine de caractère.
	 */
	public static String encodeTo64(final String content)
	{
		final byte[] encodedBytes = Base64.encode(content.getBytes());
		return new String(encodedBytes);
	}
	
	/**
	 * Décoder une chaine de caractères codée en 64bit.
	 *
	 * @param content
	 * 		string to decode.
	 *
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
	 * @param scale
	 * 		nombre de chiffre.
	 *
	 * @return un Long random.
	 */
	public static Long randomLong(final int scale)
	{
		return new Integer(randomString(scale).hashCode()).longValue();
	}
	
	/**
	 * Create a random string value.
	 *
	 * @param size
	 * 		nombre de caractère
	 *
	 * @return un String random
	 */
	private static String randomString(final int size)
	{
		return RandomStringUtils.randomAlphanumeric(size);
	}
	
	/**
	 * Create a random string value.
	 *
	 * @param size
	 * 		nombre de caractère.
	 *
	 * @return un String random.
	 */
	public static Integer randomInteger(final int size)
	{
		return randomString(size).hashCode();
	}
	
	/**
	 * @return generated key used in all confirmations.
	 */
	public static String generateConfirmationKey()
	{
		return UUID.randomUUID().toString() + "-" + SppotiUtils.randomString(40) + UUID.randomUUID().toString();
		
	}
	
	/**
	 * @param dateToCheck
	 * 		date to verify.
	 *
	 * @return true if expired.
	 */
	public static boolean isDateExpired(final Date dateToCheck)
	{
		final LocalDate dateToVerify = dateToCheck.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return !dateToVerify.isAfter(LocalDate.now());
	}
	
	/**
	 * Check if account has been deactivated more than 90 days.
	 *
	 * @return true if account must be completly deleted.
	 */
	public static boolean isAccountReadyToBeCompletlyDeleted(final Date date, final int daysBeforeCompleteDelete) {
		
		final LocalDate dateToVerify = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
				.plusDays(daysBeforeCompleteDelete);
		
		return !dateToVerify.isAfter(LocalDate.now());
	}
	
	/**
	 * Geerate an expiry date for tokens.
	 *
	 * @param expiryDateNumber
	 * 		number of days before expiration.
	 *
	 * @return expiry date.
	 */
	public static Date generateExpiryDate(final int expiryDateNumber)
	{
		
		final LocalDateTime ldt = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())
				.plusDays(expiryDateNumber);
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * @param old
	 * 		days to minus from actrudal date.
	 *
	 * @return generated date.
	 */
	public static Date generateOldDate(final int old)
	{
		
		final LocalDateTime ldt = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())
				.minusDays(old);
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * Normalisation des noms et prénoms.
	 *
	 * @param noms
	 * 		liste des noms à normaliser
	 *
	 * @return tableau contenant dans l'ordre les noms et prénom normalisés.
	 */
	public static List<String> normaliserUnGroupeDeNom(final String... noms)
	{
		if (noms.length == 0) {
			return null;
		}
		
		final List<String> names = new ArrayList<>();
		for (final String nom : noms) {
			names.add(normaliser(nom));
		}
		return names;
	}
	
	/**
	 * Normaliser un prenom.
	 *
	 * @param prenom
	 * 		prenom à normaliser.
	 *
	 * @return prenom normalisé.
	 */
	public static String normaliser(String prenom)
	{
		// Saisie de plusieurs prénoms séparés par des espaces possible
		String[] prenoms = prenom.split(" ");
		// Filtre les chaînes vides (donc filtre les espaces redondants)
		prenoms = Arrays.stream(prenoms).filter(s -> !s.isEmpty()).toArray(String[]::new);
		
		// Pour chaque prénom saisi
		for (int i = 0; i < prenoms.length; i++) {
			// Prise en compte des cas de prénoms composés
			String[] prenomTab = prenoms[i].split("-");
			// Filtre les chaînes vides (donc filtre les traits d'union redondants)
			prenomTab = Arrays.stream(prenomTab).filter(s -> !s.isEmpty()).toArray(String[]::new);
			// Reconstitution du prénom composé avec la bonne casse
			
			if (prenomTab.length > 0) {
				prenoms[i] = prenomTab[0].substring(0, 1).toUpperCase() + prenomTab[0].substring(1).toLowerCase();
				for (int j = 1; j < prenomTab.length; j++) {
					prenoms[i] = prenoms[i] + "-" + prenomTab[j].substring(0, 1).toUpperCase() +
							prenomTab[j].substring(1).toLowerCase();
					;
				}
			}
		}
		
		// Reconstitution de la liste des prénoms
		if (prenoms.length > 0) {
			final StringBuilder prenomBuilder = new StringBuilder(prenoms[0]);
			for (int i = 1; i < prenoms.length; i++) {
				prenomBuilder.append(" ").append(prenoms[i]);
			}
			prenom = prenomBuilder.toString();
		}
		
		return prenom;
	}
	
	/**
	 * Add timeZone to a date.
	 *
	 * @param originDate
	 * 		date to transform.
	 * @param timeZone
	 * 		timeZone to add to the date.
	 *
	 * @return date with correct timeZone.
	 */
	public static Date dateWithTimeZone(final Date originDate, final String timeZone) {
		
		String[] timeZoneString = timeZone.split("\\+");
		boolean timeZonePlus = true;
		LocalDateTime correctDate = originDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		if (timeZoneString.length != 2) {
			timeZoneString = timeZone.split("-");
			timeZonePlus = false;
		}
		
		try {
			final int timeZoneNumber = Integer.parseInt(timeZoneString[1]);
			if (timeZonePlus) {
				correctDate = correctDate.plusHours(timeZoneNumber);
			} else {
				correctDate = correctDate.minusHours(timeZoneNumber);
			}
			
		} catch (final NumberFormatException e) {
			throw new BusinessGlobalException("Incorrect timeZone !!" + e.getMessage());
		}
		
		return Date.from(correctDate.atZone(ZoneId.systemDefault()).toInstant());
	}
}
