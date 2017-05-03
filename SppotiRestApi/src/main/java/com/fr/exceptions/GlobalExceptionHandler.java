package com.fr.exceptions;

import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NoRightToAcceptOrRefuseChallenge;
import com.fr.commons.exception.NotAdminException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

/**
 * This Class handle all Exceptions.
 *
 * Created by djenanewail on 3/7/17.
 */
@ControllerAdvice
public class GlobalExceptionHandler
{
	
	/** Class logger. */
	private final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * Catch All {@link NotAdminException} exceptions.
	 *
	 * @param e
	 * 		instace of {@link NotAdminException}
	 *
	 * @return 403 http status if exception was catched.
	 */
	@ExceptionHandler(value = NotAdminException.class)
	public ResponseEntity notTeamAdmin(final NotAdminException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}
	
	/**
	 * Catch All {@link BusinessGlobalException} exceptions.
	 *
	 * @param e
	 * 		instace of {@link BusinessGlobalException}
	 *
	 * @return 400 http status if exception was catched.
	 */
	@ExceptionHandler(value = BusinessGlobalException.class)
	public ResponseEntity globalException(final BusinessGlobalException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	/**
	 * Catch All {@link EntityNotFoundException} exceptions.
	 *
	 * @param e
	 * 		instace of {@link EntityNotFoundException}
	 *
	 * @return 404 http status if exception was catched.
	 */
	@ExceptionHandler(value = {EntityNotFoundException.class, IllegalArgumentException.class})
	public ResponseEntity globalException(final EntityNotFoundException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	/**
	 * Catch All {@link javax.persistence.EntityExistsException} exceptions.
	 * && {@link org.springframework.dao.DataIntegrityViolationException}
	 *
	 * @param e
	 * 		instace of {@link EntityNotFoundException}
	 *
	 * @return 404 http status if exception was catched.
	 */
	@ExceptionHandler(value = {EntityExistsException.class, DataIntegrityViolationException.class})
	public ResponseEntity conflictException(final EntityExistsException e, final DataIntegrityViolationException e1)
	{
		String message = null;
		if (e != null) {
			this.LOGGER.error(e.getMessage(), e);
			message = e.getMessage();
		} else if (e1 != null) {
			this.LOGGER.error(e1.getMessage(), e1);
			message = e1.getMessage();
		}
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
	}
	
	/**
	 * Catch All {@link NoRightToAcceptOrRefuseChallenge} exceptions.
	 *
	 * @param e
	 * 		instace of {@link NoRightToAcceptOrRefuseChallenge}
	 *
	 * @return 403 http status if exception was catched.
	 */
	@ExceptionHandler(value = NoRightToAcceptOrRefuseChallenge.class)
	public ResponseEntity globalException(final NoRightToAcceptOrRefuseChallenge e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}
	
	/**
	 * Catch any unhandled {@link RuntimeException}
	 *
	 * @param e
	 * 		a {@link RuntimeException}
	 *
	 * @return 500 http status
	 */
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity otherRuntimeExceptions(final RuntimeException e)
	{
		this.LOGGER.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
	
}
