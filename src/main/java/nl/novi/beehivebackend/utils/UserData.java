package nl.novi.beehivebackend.utils;

import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserData {

    private final UserRepository userRepository;


    public UserData(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public  User getLoggedInUser() {
        User user = userRepository.findByUsername(getLoggedInUsername());
        return user;
    }
    private  String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
