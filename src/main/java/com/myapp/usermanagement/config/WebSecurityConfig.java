/*
package com.myapp.usermanagement.config;

import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAccountService userAccountService;

    public WebSecurityConfig(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // Load user details from the database
    @Bean
    public UserDetailsService userDetailsService() {
        return userAccountService::loadUserByUsername; // Method from our UserAccountService
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/admin/**").hasRole("ADMIN")  // Only admins can access /admin/** pages
                    .antMatchers("/users/**").authenticated()   // Users must be logged in
                    .antMatchers("/", "/login", "/register").permitAll()  // Public pages
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/users", true)
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
    }
}
*/
