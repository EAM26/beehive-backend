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

    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
    // Je kunt dit ook in een aparte configuratie klasse zetten.
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


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
                // Wanneer je deze uncomments, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
//                .requestMatchers("/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/users/{username}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/employees").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET,"/rosters").hasAnyRole("ADMIN", "MANAGER", "USER")
//                .requestMatchers(HttpMethod.POST, "/cimodules").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/cimodules/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.POST, "/remotecontrollers").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/remotecontrollers/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.POST, "/televisions").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/televisions/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.POST, "/wallbrackets").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/wallbrackets/**").hasRole("ADMIN")
//                // Je mag meerdere paths tegelijk definieren
//                .requestMatchers("/cimodules", "/remotecontrollers", "/televisions", "/wallbrackets").hasAnyRole("ADMIN", "USER")
//                .requestMatchers("/authenticated").authenticated()
//                .requestMatchers("/authenticate").permitAll()
                .anyRequest().denyAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
