package com.dumanskiy.timur.gradebook.configure.security;

import com.dumanskiy.timur.gradebook.dao.DAOWebLogic;
import com.dumanskiy.timur.gradebook.entity.UserInfo;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@EnableWebSecurity
@ComponentScan("com.dumanskiy.timur.gradebook")
@EnableGlobalMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        List<UserInfo> userInfos = dao.getUsers();
        Logger.getLogger(WebSecurityConfig.class).debug("Received " + userInfos.size() + " users");
        for (UserInfo userInfo : userInfos) {
            manager.createUser(User.withDefaultPasswordEncoder()
                    .username(userInfo.getUsername())
                    .password(userInfo.getPassword())
                    .roles(userInfo.getRole())
                    .build());
        }
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/teacher/**").hasAnyRole("TEACHER")
                .antMatchers("/student/**").hasAnyRole("STUDENT")
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/cabinet")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }
}
