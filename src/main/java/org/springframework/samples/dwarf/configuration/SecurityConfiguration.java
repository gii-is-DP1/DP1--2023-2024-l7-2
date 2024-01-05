package org.springframework.samples.dwarf.configuration;

import static org.springframework.security.config.Customizer.withDefaults;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.samples.dwarf.configuration.jwt.AuthEntryPointJwt;
import org.springframework.samples.dwarf.configuration.jwt.AuthTokenFilter;
import org.springframework.samples.dwarf.configuration.services.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	DataSource dataSource;

	private static final String ADMIN = "ADMIN";
	private static final String USER = "USER";

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http
				.cors(withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
				.exceptionHandling((exepciontHandling) -> exepciontHandling.authenticationEntryPoint(
						unauthorizedHandler))

				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
						.requestMatchers("/resources/**", "/webjars/**", "/static/**", "/swagger-resources/**")
						.permitAll()
						.requestMatchers("/", "/oups", "/api/v1/auth/**", "/v3/api-docs/**",
								"/swagger-ui.html", "/swagger-ui/**")
						.permitAll()
						.requestMatchers("/api/v1/developers").permitAll() // api developers

						// achievements rules
						.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/achievements/**"))
						.permitAll()
						.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/achievements"))
						.hasAuthority(ADMIN)
						.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/achievements"))
						.hasAuthority(ADMIN)
						.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/achievements"))
						.hasAuthority(ADMIN)
						.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()

						// game rules
						.requestMatchers("/api/v1/game").authenticated()
						.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/game/**")).authenticated()

						// online users
						// .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET,
						// "/api/v1/users/**"))
						// .permitAll()
						// .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT,
						// "/api/v1/users/**"))
						// .permitAll()
						// .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST,
						// "/api/v1/users/**"))
						// .permitAll()

						.anyRequest().authenticated())

				.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
