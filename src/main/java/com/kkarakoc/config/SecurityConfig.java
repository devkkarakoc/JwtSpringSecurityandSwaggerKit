package com.kkarakoc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kkarakoc.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String AUTHENTICATE = "/authenticate";
	private static final String REGISTER = "/register";
	private static final String REFRESH_TOKEN = "/refreshToken";
	private static final String[] SWAGGER_PATHS = { "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html" };

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests(request -> request.requestMatchers(AUTHENTICATE, REGISTER, REFRESH_TOKEN)
						.permitAll().requestMatchers(SWAGGER_PATHS).permitAll().anyRequest().authenticated())
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
