package com.letrasypapeles.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.letrasypapeles.backend.security.jwt.JwtAuthenticationEntryPoint;
import com.letrasypapeles.backend.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	}

	@Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
			.csrf(csrf -> csrf.disable())
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			)
			.sessionManagement(sessions -> sessions
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(authz -> authz
				.requestMatchers("/api/authentication/register").permitAll()
				.requestMatchers("/api/authentication/login").permitAll()
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
				.requestMatchers("/api/client").hasAnyRole("ADMIN", "CLIENT")
				.requestMatchers("/api/order").hasAnyRole("ADMIN", "CLIENT")
				.requestMatchers("/api/product").hasAnyRole("ADMIN", "CLIENT")
				.requestMatchers("/api/role").hasRole("ADMIN")
				.requestMatchers("/api/user").hasRole("ADMIN")
				.requestMatchers("/api/developer").hasRole( "DEVELOPER")
				
				.anyRequest().authenticated()
				
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