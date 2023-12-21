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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*  Deze security is niet de enige manier om het te doen.
    In de andere branch van deze github repo staat een ander voorbeeld
 */
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
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()



//                Authentication requests
                .requestMatchers(HttpMethod.GET, "/authenticated").authenticated()
                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()

//                User requests
                .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/users/{username}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/users/{username}/authorities").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/users/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/users/{username}/{userrole}/authorities").hasRole("Admin")
                .requestMatchers(HttpMethod.POST,"/users/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT,"/users/{username}").hasAnyRole("ADMIN", "MANAGER", "USER")
                .requestMatchers(HttpMethod.PUT,"/users/**").hasRole("ADMIN")


                .requestMatchers(HttpMethod.DELETE, "/users/{username}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

//                .requestMatchers(HttpMethod.GET,"/profile").authenticated()
//                Employee requests
                .requestMatchers(HttpMethod.GET,"/employees").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET,"/employees/profile").authenticated()
                .requestMatchers(HttpMethod.GET,"/employees/profile/{id}").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.POST,"/employees").hasAnyRole("ADMIN", "MANAGER")

//                Roster requests
                .requestMatchers(HttpMethod.GET,"/rosters").hasAnyRole("ADMIN", "MANAGER", "USER")
//
                .anyRequest().denyAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
