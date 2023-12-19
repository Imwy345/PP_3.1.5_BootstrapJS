package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final SuccessUserHandler successUserHandler;

        public WebSecurityConfig(SuccessUserHandler successUserHandler) {
            this.successUserHandler = successUserHandler;
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .formLogin()
                    .successHandler(successUserHandler)
                    .loginProcessingUrl("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll();
            httpSecurity
                    .authorizeRequests()
                    .antMatchers("/login").anonymous()
                    .antMatchers("/").access("hasRole('ROLE_ADMIN')")
                    .antMatchers("/user/**").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')").anyRequest().authenticated();
            httpSecurity.logout()
                    .permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .and().csrf().disable();
        }


    }