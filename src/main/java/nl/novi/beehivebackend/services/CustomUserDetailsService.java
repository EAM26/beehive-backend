package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.exceptions.DisabledException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Authority;
import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("User not found with name: " + username));
        Set<Authority> authorities = user.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority: authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        if (user.getIsDeleted()) {

            throw new DisabledException("Accounts is disabled");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),grantedAuthorities);
    }

}
