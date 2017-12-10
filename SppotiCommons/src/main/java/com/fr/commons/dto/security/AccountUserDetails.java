package com.fr.commons.dto.security;

import com.fr.commons.dto.RoleDTO;
import com.fr.commons.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
public class AccountUserDetails implements MyUserDetails
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountUserDetails.class);
	
	private final UserDTO account;
	
	public AccountUserDetails(final UserDTO account)
	{
		this.account = account;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		final ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (final RoleDTO userRole : this.account.getUserRoles()) {
			
			authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getName()));
		}
		
		LOGGER.info("logged user <{}> with authorities : {}", this.account.getEmail(), authorities.toString());
		
		return authorities;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getId()
	{
		return this.account.getTechId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUuid()
	{
		return this.account.getId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPassword()
	{
		return this.account.getPassword();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUsername()
	{
		
		if (!this.account.getEmail().isEmpty()) {
			return this.account.getEmail();
		} else if (!this.account.getPhone().isEmpty()) {
			return this.account.getPhone();
		}
		
		return this.account.getUsername();
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled()
	{
		if (!this.account.isConfirmed())
			LOGGER.info("Email not yet confirmed");
		
		return this.account.isConfirmed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTimeZone() {
		return this.account.getTimeZone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasActivatedEmail() {
		return this.account.getCanReceiveEmails();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasActivatedNotifications() {
		return this.account.getCanReceiveNotifications();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirebaseToken() {
		return this.account.getFirebaseToken();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFirebaseToken(final String token) {
		this.account.setFirebaseToken(token);
	}
}