package com.whu.checky.config;


import com.whu.checky.auth.filter.CorsFilter;
import com.whu.checky.auth.filter.TokenAuthenticationFilter;
import com.whu.checky.auth.TokenAuthenticationProvider;
import com.whu.checky.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private TokenService tokenService;

    @Configuration
    @Order(1)
    class PredictorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(new TokenAuthenticationProvider(tokenService));
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http

//                    .antMatcher("/**")
//                    .addFilterAfter(new BodyReaderFilter(),BasicAuthenticationFilter.class)
                    .addFilterBefore(new CorsFilter(),UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(new TokenAuthenticationFilter(),
                            BasicAuthenticationFilter.class)
//                    .addFilterAfter(new ResultExceptionTranslationFilter(),
//                            ExceptionTranslationFilter.class)
                    .authorizeRequests()

//                    .antMatchers("/**").permitAll()

//                    暂时关闭权限认证
                    .antMatchers("/wechat/login","/admin/login","/resources/**","/socket/**",
                            "/userAndHobby/getServiceTerms", "/essay/queryEssayById", "/essay/displayEssay",
                            "/essay/queryComments", "/task/taskDetail",
                            "/addApiTest/**", "/admin/**").permitAll()
                    .antMatchers("/admin","/**/admin/**").hasRole("ADMIN")
                    .antMatchers("/**").hasRole("USER")
                    .and()
                    .csrf()
                    .disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }
}
