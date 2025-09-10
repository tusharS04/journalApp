package net.engineeringdigest.journalApp.config;

import net.engineeringdigest.journalApp.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    //TODO :
    /**
     * 1. A user entity to represent user model
     * 2. A userRepository to interact with mongodb
     * 3. vice implementation to fetch user details
     * 4. A configuration SecurityConfig to integrate everything with spring security
     */

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers("/journal/**", "/user/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic();

//        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
