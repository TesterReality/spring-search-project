package com.example.Viazmus.config;

import com.example.Viazmus.controller.MySimpleUrlAuthenticationSuccessHandler;
import com.example.Viazmus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder encoder()
    {
      return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/**", "/js/**", "/img/**","/fonts/**","/vendor/**").permitAll();//даю доступ всем к ресурсам. В ином случае - не подгружает стили пока не зарегаешься
        http
                .authorizeRequests()//включаем авторизацию
                    .antMatchers("/","/registration","/login","/view","/getfile","/search").permitAll()//полный доступ для этой странички

                .anyRequest().authenticated()//для всех остальных запросов - требует авторизацию
                .and()
                    .formLogin()//включаем логин
                    .loginPage("/login")//говорим что он по такому адресу
                    //.defaultSuccessUrl("/")//куда перенаправляет, когда пользователь зашел
                .successHandler(myAuthenticationSuccessHandler())

                .and()
                    .logout()//выход из аккаунта
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll();//тоже всем

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(encoder());
    }

}