package com.trishna.library.utils;

import com.trishna.library.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.userDetailsService(userService);
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/student/**").hasAuthority(Constants.STUDENT_SELF_INFO_AUTHORITY)
                .antMatchers(HttpMethod.GET, "/student-by-id/**").hasAuthority(Constants.STUDENT_INFO_AUTHORITY)
                .antMatchers(HttpMethod.POST, "/admin/**").hasAuthority((Constants.CREATE_ADMIN_AUTHORITY))
                .antMatchers(HttpMethod.POST, "/book/**").hasAuthority(Constants.CREATE_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.GET, "/book/**").hasAuthority(Constants.READ_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.PATCH, "/update/book-by-id/**").hasAuthority(Constants.UPDATE_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.PATCH, "/delete/book-by-id/**").hasAuthority(Constants.DELETE_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.POST, "transaction/**").hasAuthority(Constants.INITIATE_TRANSACTION_AUTHORITY)
                .antMatchers(HttpMethod.POST, "/transaction/payment/**").hasAuthority(Constants.MAKE_PAYMENT_AUTHORITY)
                .antMatchers(HttpMethod.GET, "/transaction/showFine/**").hasAuthority(Constants.SHOW_FINE_AUTHORITY)
                .antMatchers("student/create/**").permitAll()
                .and()
                .formLogin();
    }


}
