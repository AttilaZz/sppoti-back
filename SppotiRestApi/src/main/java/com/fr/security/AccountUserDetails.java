package com.fr.security;

import com.fr.aop.TraceAuthentification;
import com.fr.entities.RoleEntity;
import com.fr.entities.UserEntity;
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
	/** account entity. */
	private final UserEntity account;
	
	/** Class logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TraceAuthentification.class);
	
	/** Init class. */
	public AccountUserDetails(final UserEntity account)
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
		
		for (final RoleEntity userRole : this.account.getRoles()) {
			
			authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getName()));
		}
		
		for (final GrantedAuthority grantedAuthority : authorities) {
			
			LOGGER.info("authorities : {}" + grantedAuthority.getAuthority());
			
		}
		return authorities;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getId()
	{
		return this.account.getId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUuid()
	{
		return this.account.getUuid();
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
		} else if (!this.account.getTelephone().isEmpty()) {
			return this.account.getTelephone();
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
	public UserEntity getConnectedUserDetails()
	{
		return this.account;
	}
}
