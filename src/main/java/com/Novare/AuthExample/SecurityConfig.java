package com.Novare.AuthExample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User.builder().passwordEncoder(s -> passwordEncoder().encode(s))
                .username("user@example.com")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.builder().passwordEncoder(s -> passwordEncoder().encode(s))
                .username("admin@example.com")
                .password("password")
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET,"/", "/static/**","/api/signup").permitAll()
                .antMatchers(HttpMethod.POST ,"/api/login","/api/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/api/check/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin();
        return http.build();
    }

}