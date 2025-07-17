package com.letrasypapeles.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.letrasypapeles.backend.security.jwt.JwtAuthEntryPoint;
import com.letrasypapeles.backend.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthEntryPoint jwtAuthEntryPoint;

	@Autowired
	public SecurityConfig(JwtAuthEntryPoint jwtAuthEntryPoint) {
		this.jwtAuthEntryPoint = jwtAuthEntryPoint;
	}

	@Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
			.csrf(csrf -> csrf.disable())
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint(jwtAuthEntryPoint)
			)
			.sessionManagement(sessions -> sessions
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(authz -> authz
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").hasRole("DEVELOPER") 
				.requestMatchers("/api/authentication/login").permitAll()
				.requestMatchers("/api/authentication/registro").hasRole("ADMIN") 
				.requestMatchers("/api/user").hasRole("DEVELOPER") 
				// .anyRequest().authenticated()
				.anyRequest().permitAll()
			);

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
}