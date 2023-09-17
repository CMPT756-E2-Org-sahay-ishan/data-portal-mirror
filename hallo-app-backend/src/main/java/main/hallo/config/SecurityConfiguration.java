package main.hallo.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import main.hallo.config.CustomPassword;
import main.hallo.jwt.JwtFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	UserDetailsService userDetailService;
	@Autowired 
	private CustomPassword passwordEncoder;
	@Autowired
	JwtFilter jwtFilter;
	
	@Override @Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder( passwordEncoder.passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().disable().
		csrf().disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().exceptionHandling()
		.authenticationEntryPoint((request, response, ex)->{
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,ex.getMessage());
		}).and()
		.authorizeHttpRequests()
		.antMatchers("/api/smru/**").authenticated()
		.antMatchers("/api/auth/**").permitAll()
		.antMatchers("/api/users/register").permitAll()
		.anyRequest().authenticated();
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
}

