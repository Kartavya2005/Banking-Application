package com.Bank.Banking.Security;

import com.Bank.Banking.Entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class AppUserPrincipal implements UserDetails {

    private final AppUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(
                new SimpleGrantedAuthority("ROLE_"+user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return true;
    }


}
