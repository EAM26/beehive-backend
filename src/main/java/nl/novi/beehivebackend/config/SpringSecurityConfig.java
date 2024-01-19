package nl.novi.beehivebackend.config;

import nl.novi.beehivebackend.filter.JwtRequestFilter;
import nl.novi.beehivebackend.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final CustomPasswordEncoder customPasswordEncoder;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter, CustomPasswordEncoder customPasswordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.customPasswordEncoder = customPasswordEncoder;
    }


    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(customPasswordEncoder.passwordEncoder())
                .and()
                .build();
    }


    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()


//                AUTHENTICATION REQUESTS

                .requestMatchers(HttpMethod.GET, "/authenticated").authenticated()
                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()

//              ******************************************************

//                USER REQUESTS
                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/{username}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/users/{username}").hasAnyRole("ADMIN", "MANAGER", "USER")
                .requestMatchers(HttpMethod.PUT, "/users/auth/{username}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")

//              ******************************************************


//                TEAM REQUESTS
                .requestMatchers(HttpMethod.GET, "/teams").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/teams/{teamName}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/teams/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/teams").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/teams/{teamName}").hasRole("ADMIN")

//              ******************************************************


//              SHIFT REQUESTS
                .requestMatchers(HttpMethod.GET, "/shifts").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/shifts/{id}").hasAnyRole("ADMIN", "MANAGER")

                .requestMatchers(HttpMethod.POST, "/shifts").hasAnyRole("ADMIN", "MANAGER")

                .requestMatchers(HttpMethod.PUT, "/shifts").hasAnyRole("ADMIN", "MANAGER")

//              ******************************************************


//                ABSENCE REQUESTS

                .requestMatchers(HttpMethod.GET, "/absences").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/absences/{id}").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/absences/**").hasAnyRole("ADMIN", "MANAGER")

                .requestMatchers(HttpMethod.POST, "/absences").hasAnyRole("ADMIN", "MANAGER")

                .requestMatchers(HttpMethod.DELETE, "/absences/{id}").hasAnyRole("ADMIN", "MANAGER")

//              ******************************************************


//                Employee requests
                .requestMatchers(HttpMethod.GET, "/employees").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/employees/profile").authenticated()
                .requestMatchers(HttpMethod.GET, "/employees/profile/{id}").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.POST, "/employees").hasAnyRole("ADMIN", "MANAGER")


                .anyRequest().denyAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
