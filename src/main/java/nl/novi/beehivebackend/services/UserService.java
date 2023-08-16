package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.UserInputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.IsNotUniqueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.exceptions.UsernameNotFoundException;
import nl.novi.beehivebackend.models.Authority;
import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.models.UserRole;
import nl.novi.beehivebackend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UserOutputDto> getUsers() {
        List<UserOutputDto> allUsersList = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            allUsersList.add(transferUserToUserOutputDto(user));
        }
        return allUsersList;
    }

    public UserOutputDto getUser(String username) {
        User user = userRepository.findById(username).orElseThrow(()-> new RecordNotFoundException("No user found with name: " + username));
        return transferUserToUserOutputDto(user);
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUserName(UserInputDto userInputDto) {
        if(userExists(userInputDto.getUsername())) {
            throw new IsNotUniqueException("Username is not unique");
        }
        User newUser = userRepository.save(transerUserInputDtoToUser(userInputDto));
        return newUser.getUsername();
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserInputDto newUser) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException();
        if(userExists((newUser.getUsername())) && (!username.equalsIgnoreCase(newUser.getUsername()))) {
            throw new IsNotUniqueException("Username is not unique");
        }
        User user = userRepository.findById(username).get();
        user.setEmail(newUser.getEmail());
        // TODO: 16-8-2023 Encode password 
        user.setPassword(newUser.getPassword());
        userRepository.save(user);
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException("User not found with name: " + username);
        User user = userRepository.findById(username).get();
        UserOutputDto userOutputDto = transferUserToUserOutputDto(user);
        return userOutputDto.getAuthorities();
    }

    public void addAuthority(String username, String  roleName) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        try {
            UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
            user.addAuthority(new Authority(username, userRole));
        } catch (Exception e) {
            throw new RecordNotFoundException("No role found with name: " + roleName);
        }
        userRepository.save(user);
    }


    public void removeAuthority(String username, String role) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        for (Authority authority: user.getAuthorities()) {
            if(authority.getAuthority().equalsIgnoreCase("ROLE_" + checkUserRoleExists(role))) {
                user.removeAuthority(authority);
            }
        }
        userRepository.save(user);
    }

    public  UserOutputDto transferUserToUserOutputDto(User user){
        var outputDto = new UserOutputDto();
        outputDto.username = user.getUsername();
        outputDto.password = user.getPassword();
        outputDto.email = user.getEmail();
        outputDto.authorities = user.getAuthorities();
        if(user.getEmployee() != null) {
            outputDto.employeeId = user.getEmployee().getId();
        }
        return outputDto;
    }

    public User transerUserInputDtoToUser(UserInputDto userInputDto) {
        var user = new User();
        user.setUsername(userInputDto.getUsername());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setEmail(userInputDto.getEmail());
        return user;

    }

    private String checkUserRoleExists(String roleName) {
        boolean isMatch = false;
        for (UserRole userRole: UserRole.values()) {
            if(roleName.equalsIgnoreCase(userRole.name())) {
                isMatch = true;
                break;
            }
        }
        if(!isMatch) {
            throw new BadRequestException("No role found with name: " + roleName);
        }
        return roleName.toUpperCase();
    }


}

