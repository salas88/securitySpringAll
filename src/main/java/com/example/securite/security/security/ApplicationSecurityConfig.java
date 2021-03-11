package com.example.securite.security.security;

import com.example.securite.security.auoth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.concurrent.TimeUnit;
import static com.example.securite.security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class  ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                    .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                    .antMatchers("/api/**").hasRole(STUDENT.name())
                // повесил те же самые запреты на методы, а сюда просто накинул анатацию @EnableGlobalMethodSecurity(prePostEnabled = true)
//                    .antMatchers(HttpMethod.DELETE,"/manager/api/**").hasAnyAuthority( COURSE_WRITE.getPermission())
//                    .antMatchers(HttpMethod.PUT,"/manager/api/**").hasAnyAuthority( COURSE_WRITE.getPermission())
//                    .antMatchers(HttpMethod.POST,"/manager/api/**").hasAnyAuthority( COURSE_WRITE.getPermission())
//                    .antMatchers(HttpMethod.GET,"/manager/api/**").hasAnyRole(ADMIN.name(), ADMINISTRATION.name())
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin().loginPage("/login").permitAll()
                    .defaultSuccessUrl("/courses", true)
                .and()
                    .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("somethingVerySecret")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
    return provider;
    }
}
