package com.SJY.O2O_Automatic_Store_System_Demo.config.security;

import com.SJY.O2O_Automatic_Store_System_Demo.config.tocken.TokenHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final TokenHelper accessTokenHelper;
    public CustomUserDetailsService(@Qualifier("accessTokenHelper")TokenHelper accessTokenHelper) {
        this.accessTokenHelper = accessTokenHelper;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        CustomUserDetails customUserDetails = accessTokenHelper.parse(token)
                .map(privateClaims -> convert(privateClaims))
                .orElse(null);
        return customUserDetails;
    }

    private CustomUserDetails convert(TokenHelper.PrivateClaims privateClaims) {
        return new CustomUserDetails(
                privateClaims.getMemberId(),
                privateClaims.getRoleTypes().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet())
        );
    }
}