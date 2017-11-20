package com.example.demo;

import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService participantService;

    @Bean
    AmazonCognitoIdentity cognito() {
        return AmazonCognitoIdentityClientBuilder.defaultClient();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.participantService).passwordEncoder(this.passwordEncoder());
    }

    private PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        //Implementing Token based authentication in this filter
        final TokenAuthenticationFilter tokenFilter = new TokenAuthenticationFilter();
        http.addFilterBefore(tokenFilter, BasicAuthenticationFilter.class);


        http.authorizeRequests()
            .antMatchers("/").authenticated()
            .antMatchers("/token").permitAll()
            .anyRequest().denyAll();
    }
}
