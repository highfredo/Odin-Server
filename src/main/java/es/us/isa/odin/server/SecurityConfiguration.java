package es.us.isa.odin.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SpringSocialConfigurer;

import es.us.isa.odin.server.security.UserAccountRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private UserAccountRepository userRepository;
 
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**", "/scripts/**", "/styles/**", "/views/**", 
        		"/fonts/**", "/bower_components/**", "/*.html", "/*.ico", "/*.txt");
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.authorizeRequests().antMatchers("/**").permitAll();
        http	
        	.csrf().disable()
        	.formLogin()
            	.loginPage("/auth/twitter")
                .loginProcessingUrl("/login/authenticate")
                .failureUrl("/login?error=bad_credentials")
            .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            .and()
                .authorizeRequests()
                .antMatchers(
                	"/",	
                    "/auth/**",
                    "/login",
                	"/signup/**",
                	"/signin/**",
                	"/user/**"
                ).permitAll()
                .antMatchers("/**").hasRole("USER")
            .and()
            	.apply(new SpringSocialConfigurer());        
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService())
            .passwordEncoder(passwordEncoder());
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
