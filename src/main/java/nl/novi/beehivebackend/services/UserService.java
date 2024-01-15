package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.UserInputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDto;
import nl.novi.beehivebackend.exceptions.*;
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
        for (User user : userRepository.findAll()) {
            allUsersList.add(transferUserToUserOutputDto(user));
        }
        return allUsersList;
    }

    public List<UserOutputDto> getUsers(Boolean isDeleted) {
        List<UserOutputDto> allUsersList = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            if (user.getIsDeleted() == isDeleted) {
                allUsersList.add(transferUserToUserOutputDto(user));
            }
        }
        return allUsersList;
    }

    public UserOutputDto getSingleUser(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + username));
        return transferUserToUserOutputDto(user);
    }


    public UserOutputDto createUser(UserInputDto userInputDto) {
        if (userExists(userInputDto.getUsername())) {
            throw new IsNotUniqueException("Username is not unique");
        }
        userInputDto.setEmail(userInputDto.getEmail().toLowerCase());
        if (emailExists(userInputDto.getEmail())) {
            throw new IsNotUniqueException("Email is not unique");
        }
        if (!checkUserRoleExists(userInputDto.getUserRole())) {
            throw new BadRequestException("Unknown user role");
        }
        User user = adminTransferUserInputDtoToUser(new User(), userInputDto);
        UserRole userRole = UserRole.valueOf(userInputDto.getUserRole().toUpperCase());
        user.addAuthority(new Authority(user.getUsername(), userRole));
        userRepository.save(user);
        return transferUserToUserOutputDto(user);
    }

    public UserOutputDto updateUser(String username, UserInputDto userInputDto) {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("User with name " + username + " doesn't exist."));
        User currentUser = userRepository.findByUsername(getCurrentUserId());
        if (!username.equals(userInputDto.getUsername())) {
            throw new AccessDeniedException("Not allowed to change username.");
        }


//      check if authority of current user is Admin
        for (Authority auth : currentUser.getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_ADMIN")) {
                if(currentUser.getUsername().equals(userInputDto.getUsername()) && !userInputDto.getUserRole().toUpperCase().equals("ADMIN")) {
                    throw new AccessDeniedException("You can't demote your own authority role");
                }
                User updatedUser = adminTransferUserInputDtoToUser(user, userInputDto);
                UserRole userRole = UserRole.valueOf(userInputDto.getUserRole().toUpperCase());
                updatedUser.getAuthorities().clear();
                updatedUser.addAuthority(new Authority(user.getUsername(), userRole));
                userRepository.save(updatedUser);
                return transferUserToUserOutputDto(user);
            }
        }

//      check if current user is not owner
        if (!currentUser.getUsername().equals(username)) {
            throw new AccessDeniedException("Not allowed to change user data");
        }

        User updatedUser = ownerTransferUserInputDtoToUser(currentUser, userInputDto);
        userRepository.save(updatedUser);
        return transferUserToUserOutputDto(updatedUser);
    }


    public void addAuthority(String username, String roleName) {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("User with name " + username + " doesn't exist."));
        if (!checkUserRoleExists(roleName)) {
            throw new RecordNotFoundException("Role doesn't exist");
        }
        UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
        for (Authority auth : user.getAuthorities()) {
            if (auth.getAuthority().equals(userRole.getRoleAsString())) {
                throw new BadRequestException("Authority already present");
            }
        }
        user.addAuthority(new Authority(username, userRole));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String roleName) {
        boolean isRemoved = false;
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("User with name " + username + " doesn't exist."));
        if (!checkUserRoleExists(roleName)) {
            throw new RecordNotFoundException("Role doesn't exist");
        }
        if (getCurrentUserId().equals(username)) {
            throw new AccessDeniedException("Not Allowed to remove own authority");
        }
        UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
        for (Authority auth : user.getAuthorities()) {
            if (auth.getAuthority().equals(userRole.getRoleAsString())) {
                user.removeAuthority(auth);
                userRepository.save(user);
                isRemoved = true;
                break;
            }
        }

        if (!isRemoved) {
            throw new BadRequestException("Authority not found");
        }


//        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
//        User user = userRepository.findById(username).get();
//        if(checkUserRoleExists(role)) {
//            for (Authority authority: user.getAuthorities()) {
//                if(authority.getAuthority().equalsIgnoreCase("ROLE_" + checkUserRoleExists(role))) {
//                    user.removeAuthority(authority);
//                }
//            }
//            userRepository.save(user);
//        }
    }


    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

//    public void updateUser(String username, UserInputDto userInputDto) {
//        User user = userRepository.findById(username).orElseThrow(()-> new RecordNotFoundException("User with name " + username + " doesn't exist."));
//        if(!(user.getUsername().equals(userInputDto.getUsername()))) {
//            throw new BadRequestException("Not allowed to change user name.");
//        }
//        // TODO: 18-8-2023 is dit nodig?
////        if(userExists((newUser.getUsername())) && (!username.equalsIgnoreCase(newUser.getUsername()))) {
////            throw new IsNotUniqueException("Username is not unique");
////        }
//
//        // TODO: 18-8-2023 check of ingelogde user gelijk is aan de te wijzigen user
//        String authUserName = SecurityContextHolder.getContext().getAuthentication().getName();
//        if(!(user.getUsername().equals(authUserName))) {
//            throw new BadRequestException("Not allowed to change other user");
//        }
//        user.setEmail(userInputDto.getEmail());
//        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
//        userRepository.save(user);
//    }


//    Helper methods

    private boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    private boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserOutputDto transferUserToUserOutputDto(User user) {
        UserOutputDto outputDto = new UserOutputDto();
        outputDto.setUsername(user.getUsername());
        outputDto.setIsDeleted(user.getIsDeleted());
        outputDto.setEmail(user.getEmail());
        outputDto.setAuthorities(user.getAuthorities());
        if (user.getEmployee() != null) {
            outputDto.setEmployeeId(user.getEmployee().getId());
        }
        return outputDto;
    }

    private User adminTransferUserInputDtoToUser(User user, UserInputDto userInputDto) {
        userInputDto.setEmail(userInputDto.getEmail().toLowerCase());
        if(emailExists(userInputDto.getEmail())) {
            throw new IsNotUniqueException("Email is not unique");
        }
        if(getCurrentUserId().equals(userInputDto.getUsername()) && userInputDto.getIsDeleted() == true) {
            throw new AccessDeniedException("You can't delete your own account");
        }

        user.setUsername(userInputDto.getUsername());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setEmail(userInputDto.getEmail());
        if (userInputDto.getIsDeleted() != null) {
            user.setIsDeleted(userInputDto.getIsDeleted());
        }
        return user;
    }

    private User ownerTransferUserInputDtoToUser(User currentUser, UserInputDto userInputDto) {
//        if (!currentUser.getAuthorities().equals(userInputDto.getAuthorities())) {
//            throw new AccessDeniedException("Not allowed to change authorities");
//        }
        if (currentUser.getIsDeleted() != userInputDto.getIsDeleted()) {
            throw new AccessDeniedException("Not allowed to change user status.");
        }

        userInputDto.setEmail(userInputDto.getEmail().toLowerCase());
        if (!userInputDto.getEmail().equals(currentUser.getEmail()) && emailExists(userInputDto.getEmail())) {
            throw new IsNotUniqueException("Email is not unique");
        }
        currentUser.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        currentUser.setEmail(userInputDto.getEmail());
        return currentUser;
    }

    private boolean checkUserRoleExists(String roleName) {
        boolean isMatch = false;
        for (UserRole userRole : UserRole.values()) {
            if (roleName.equalsIgnoreCase(userRole.name())) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }

    private Set<Authority> addAuthoritySet(User user, String highestRole) {
        Set<Authority> authorities = new HashSet<>();
        return authorities;
    }

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}

