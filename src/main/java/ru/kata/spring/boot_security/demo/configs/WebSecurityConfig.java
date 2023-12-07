package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
                    .csrf()
                    .disable()
                    .authorizeRequests()
                    .antMatchers("/").not().fullyAuthenticated()
                    .antMatchers("/admin/**").hasAnyRole("ADMIN")
                    .antMatchers("/user/**").hasAnyRole("USER")/*hasAnyRole("USER","ADMIN")*/
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .successHandler(successUserHandler)
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll();
        }

    }