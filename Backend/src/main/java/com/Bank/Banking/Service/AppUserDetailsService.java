package com.Bank.Banking.Service;

import com.Bank.Banking.Entity.AuthUser;
import com.Bank.Banking.Repository.UserRepository;
import com.Bank.Banking.Security.AppUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService  {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String email){
        AuthUser authUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return new AppUserPrincipal(authUser);
    }

}
