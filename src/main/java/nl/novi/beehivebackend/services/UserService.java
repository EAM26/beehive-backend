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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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



//    public UserOutputDto createUser(UserInputDto userInputDto, String roleName) {
//        if(userExists(userInputDto.getUsername())) {
//            throw new IsNotUniqueException("Username is not unique");
//        }
//        if(roleName == null || !checkUserRoleExists(roleName)){
//            throw new RecordNotFoundException("Role does not exist.");
//        }
//        User user = transferUserInputDtoToUser(userInputDto);
//        UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
//        user.addAuthority(new Authority(user.getUsername(), userRole));
//        userRepository.save(user);
//        return transferUserToUserOutputDto(user);
//    }

    public UserOutputDto createUser(UserInputDto userInputDto) {
        System.out.println("Service Test new user");
        if(userExists(userInputDto.getUsername())) {
            throw new IsNotUniqueException("Username is not unique");
        }
        userInputDto.setEmail(userInputDto.getEmail().toLowerCase());
        if (userRepository.existsByEmail(userInputDto.getEmail())) {
            throw new IsNotUniqueException("Email is not unique");
        }

        String highestRole = userInputDto.getHighestRole();
        if(highestRole == null || !checkUserRoleExists(highestRole)){
            throw new RecordNotFoundException("Role does not exist.");
        }
        User user = transferUserInputDtoToUser(userInputDto);
        UserRole userRole = UserRole.valueOf(highestRole.toUpperCase());
        user.addAuthority(new Authority(user.getUsername(), userRole));
        userRepository.save(user);
        return transferUserToUserOutputDto(user);
    }

    private Set<Authority> addAuthoritySet(User user, String highestRole) {
        Set<Authority> authorities = new HashSet<>();
        return authorities;
    }



    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserInputDto userInputDto) {
        User user = userRepository.findById(username).orElseThrow(()-> new RecordNotFoundException("User with name " + username + " doesn't exist."));
        if(!(user.getUsername().equals(userInputDto.getUsername()))) {
            throw new BadRequestException("Not allowed to change user name.");
        }
        // TODO: 18-8-2023 is dit nodig? 
//        if(userExists((newUser.getUsername())) && (!username.equalsIgnoreCase(newUser.getUsername()))) {
//            throw new IsNotUniqueException("Username is not unique");
//        }

        // TODO: 18-8-2023 check of ingelogde user gelijk is aan de te wijzigen user
        String authUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!(user.getUsername().equals(authUserName))) {
            throw new BadRequestException("Not allowed to change other user");
        }
        user.setEmail(userInputDto.getEmail());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        userRepository.save(user);
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException("User not found with name: " + username);
        User user = userRepository.findById(username).get();
        UserOutputDto userOutputDto = transferUserToUserOutputDto(user);
        return userOutputDto.getAuthorities();
    }

    public void addAuthority(String username, String  roleName) {
        User user = userRepository.findById(username).orElseThrow(()-> new UsernameNotFoundException(username));
//        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepository.findById(username).get();
        if(roleName == null || !checkUserRoleExists(roleName)){
            throw new RecordNotFoundException("Role does not exist.");
        }
        UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
        user.addAuthority(new Authority(username, userRole));
//        try {
//            UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
//            user.addAuthority(new Authority(username, userRole));
//        } catch (Exception e) {
//            throw new RecordNotFoundException("No role found with name: " + roleName);
//        }
        userRepository.save(user);
    }


    public void removeAuthority(String username, String role) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        if(checkUserRoleExists(role)) {
            for (Authority authority: user.getAuthorities()) {
                if(authority.getAuthority().equalsIgnoreCase("ROLE_" + checkUserRoleExists(role))) {
                    user.removeAuthority(authority);
                }
            }
            userRepository.save(user);
        }
    }

    public  UserOutputDto transferUserToUserOutputDto(User user){
        UserOutputDto outputDto = new UserOutputDto();
        outputDto.setUsername(user.getUsername());
//        outputDto.password = user.getPassword();
        outputDto.setEmail(user.getEmail());
        outputDto.setAuthorities(user.getAuthorities());
        if(user.getEmployee() != null) {
            outputDto.setEmployeeId(user.getEmployee().getId());
        }
        return outputDto;
    }

    public User transferUserInputDtoToUser(UserInputDto userInputDto) {
        User user = new User();
        user.setUsername(userInputDto.getUsername());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setEmail(userInputDto.getEmail());
        return user;

    }

    private boolean checkUserRoleExists(String roleName) {
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
        return true;
    }


}

