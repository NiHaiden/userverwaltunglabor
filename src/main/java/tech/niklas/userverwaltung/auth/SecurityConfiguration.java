package tech.niklas.userverwaltung.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static tech.niklas.userverwaltung.auth.Roles.ADMIN;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        basicSecurity(security);
    }

    private void basicSecurity(HttpSecurity security) throws Exception {
        security.
                authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/", "/admin/allquestions", "/admin/newquestion", "/admin/home").hasRole(ADMIN.name())
                .antMatchers("/").authenticated()
                .and()
                .formLogin()
                .and()
                .rememberMe();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

}
