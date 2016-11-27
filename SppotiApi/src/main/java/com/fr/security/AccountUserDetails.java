package com.fr.security;

import com.fr.aop.TraceAuthentification;
import com.fr.pojos.UserRoles;
import com.fr.pojos.Users;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@SuppressWarnings("serial")
public class AccountUserDetails implements MyUserDetails {

	private final Users account;

	private static Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

	public AccountUserDetails(Users account) {
		this.account = account;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (UserRoles userRole : account.getUserProfile()) {

			authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getName()));
		}

		for (GrantedAuthority grantedAuthority : authorities) {

			LOGGER.info("authorities : {}" + grantedAuthority.getAuthority());

		}
		return authorities;
	}

	@Override
	public Long getId() {
		return account.getId();
	}

	@Override
	public String getPassword() {
		return account.getPassword();
	}

	@Override
	public String getUsername() {
		if (!account.getEmail().isEmpty()) {
			return account.getEmail();
		} else if (!account.getTelephone().isEmpty()) {
			return account.getTelephone();
		}
		return account.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (!account.isConfirmed())
			LOGGER.info("Email not yet confirmed");

		return account.isConfirmed();
	}

}
