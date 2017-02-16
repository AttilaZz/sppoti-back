package com.fr.security;

import com.fr.aop.TraceAuthentification;
import com.fr.entities.RoleEntity;
import com.fr.entities.UserEntity;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
public class AccountUserDetails implements MyUserDetails {

    private final UserEntity account;

    private static Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    public AccountUserDetails(UserEntity account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (RoleEntity userRole : account.getRoles()) {

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
    public int getUuid() {
        return account.getUuid();
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

    @Override
    public UserEntity getConnectedUserDetails() {
        return account;
    }
}
