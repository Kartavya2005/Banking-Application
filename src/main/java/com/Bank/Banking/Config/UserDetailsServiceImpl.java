package com.Bank.Banking.Config;

import com.Bank.Banking.Entity.AppUser;
import com.Bank.Banking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);
        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                {
                    log.warn("Authentication failed for email: {}", email);
                    return new UsernameNotFoundException(
                            "User not found with email: " + email
                    );
                });

        return new User(
                appUser.getEmail(),
                appUser.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name())
                )


        );
    }
}
