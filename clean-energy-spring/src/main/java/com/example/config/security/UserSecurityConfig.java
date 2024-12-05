package com.example.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.filters.JWTAuthFilter;
import com.example.service.UserLoginService;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {

    private JWTAuthFilter jwtAuthFilter;

    private UserLoginService userLoginService;
    
	 

	public UserSecurityConfig(@Lazy UserLoginService userLoginService, JWTAuthFilter jwtAuthFilter) {
		this.userLoginService = userLoginService;
		this.jwtAuthFilter = jwtAuthFilter;
	}
	
	

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
    	http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable()) // Add Customizer.withDefaults() as an argument
	    .authorizeRequests(requests -> requests
		    .antMatchers("/login/**","/register/**" ,"/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // Allow public access to auth paths and H2 console
		    .anyRequest().authenticated())
	    .sessionManagement(management -> management
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	    .headers(headers -> headers.frameOptions().disable()); // Disable frame options to allow H2 console

    return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userLoginService);
        
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:80");        // For local access
        config.addAllowedOrigin("http://localhost:8091");
        config.addAllowedOrigin("http://localhost");           // Without port
        config.addAllowedOrigin("http://frontend");           // Docker service name
        config.addAllowedOrigin("http://frontend:80");        // Docker service with port
        config.setAllowedMethods(Arrays.asList("POST", "OPTIONS", "GET", "DELETE", "PUT"));
        config.setExposedHeaders(Arrays.asList("Authorization", "content-type", "Content-Disposition"));
        config.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "Content-Disposition"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }




    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
