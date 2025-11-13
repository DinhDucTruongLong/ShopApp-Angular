package com.project.shopapp.configurations;

import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration

@RequiredArgsConstructor
public class Securityconfig {
    private final UserRepository userRepository;
    //user's detail object
    @Bean
    public UserDetailsService userDetailsService(){
        return phoneNumber ->userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Cannot find user with phone number: "+ phoneNumber));
        }
        @Bean
        // ma hoa mat khau ve 256
        public PasswordEncoder passwordEncoder(){
        // spring da ho tro cho t r
            return new BCryptPasswordEncoder();

        // neus banj muon tu bo tuan toan de ma hoa
//            return new PasswordEncoder() {
//                @Override
//                public String encode(CharSequence rawPassword) {
//                    return null;
//                }
//
//                @Override
//                public boolean matches(CharSequence rawPassword, String encodedPassword) {
//                    return false;
//                }
//            }
        }
        @Bean
    public AuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService());
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }

        @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
        }
    }
