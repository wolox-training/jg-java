package wolox.training.config;

import io.swagger.models.HttpMethod;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailService userDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    protected void configure(final HttpSecurity http)throws Exception {
        http
            //HTTP Basic authentication
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers(String.valueOf(HttpMethod.POST),"/api/books").permitAll()
            .antMatchers(String.valueOf(HttpMethod.POST),"/api/users").permitAll()
            .antMatchers("/**").authenticated()
            .and()
            .csrf().disable()
            .formLogin().disable();

    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
            "/resources/**","/not-found","/errors",
            "/403");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}
