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
        for (User user : userRepository.findAllByIsDeleted(isDeleted)) {
            allUsersList.add(transferUserToUserOutputDto(user));
        }
        return allUsersList;


    }

    public UserOutputDto getSingleUser(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + username));
        return transferUserToUserOutputDto(user);
    }


    public UserOutputDto createUser(UserInputDto userInputDto) {

        User user = adminTransferUserInputDtoToUser(new User(), userInputDto);
        userRepository.save(user);
        return transferUserToUserOutputDto(user);
    }

    public UserOutputDto updateUser(String username, UserInputDto userInputDto) {
        User userToUpdate = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("User with name " + username + " doesn't exist."));
        User currentUser = userRepository.findByUsername(getCurrentUserId());

        if (!username.equals(userInputDto.getUsername())) {
            throw new AccessDeniedException("Not allowed to change username.");
        }

//      Check is Self or Admin

        if (isSelf(userToUpdate)) {
            System.out.println("USER IS SELF");
            return updateAsSelf(userToUpdate, userInputDto);
        } else if (isAdmin(currentUser)) {
            System.out.println("USER IS ADMIN");
            return updateAsAdmin(userToUpdate, userInputDto);
        } else {
            throw new AccessDeniedException("Insufficient permission for updating user.");
        }
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
    }


    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }


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
            outputDto.setShortName(user.getEmployee().getShortName());
        }
        return outputDto;
    }

    private User adminTransferUserInputDtoToUser(User user, UserInputDto userInputDto) {
        if (userExists(userInputDto.getUsername())) {
            throw new BadRequestException("Username is not unique");
        }
        if (!checkUserRoleExists(userInputDto.getUserRole())) {
            throw new BadRequestException("Unknown user role");
        }

//        Check email for new user
        userInputDto.setEmail(userInputDto.getEmail().toLowerCase());
        if(user.getEmail() == null) {
            if(emailExists(userInputDto.getEmail())) {
                throw new BadRequestException("Email is not unique");
            }
        }
//        Check email for existing user if self
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(userInputDto.getEmail()) && emailExists(userInputDto.getEmail())) {
                throw new BadRequestException("Email is not unique");
            }
        }

//        Id check for removing own account
        if (getCurrentUserId().equals(userInputDto.getUsername()) && userInputDto.getIsDeleted()) {
            throw new AccessDeniedException("You can't delete your own account");
        }

        user.setUsername(userInputDto.getUsername());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setEmail(userInputDto.getEmail());
        if (userInputDto.getIsDeleted() != null) {
            user.setIsDeleted(userInputDto.getIsDeleted());
        }
        UserRole userRole = UserRole.valueOf(userInputDto.getUserRole().toUpperCase());
        user.getAuthorities().clear();
        user.addAuthority(new Authority(user.getUsername(), userRole));
        return user;
    }

    private User selfTransferUserInputDtoToUser(User currentUser, UserInputDto userInputDto) {
        if (currentUser.getIsDeleted() != userInputDto.getIsDeleted()) {
            throw new AccessDeniedException("Not allowed to change user status.");
        }

        userInputDto.setEmail(userInputDto.getEmail().toLowerCase());
        if (!userInputDto.getEmail().equals(currentUser.getEmail()) && emailExists(userInputDto.getEmail())) {
            throw new IsNotUniqueException("Email is not unique");
        }

        if (!hasRole(currentUser, userInputDto.getUserRole())) {
            throw new AccessDeniedException("Not allowed to change authority");
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

    private boolean hasRole(User user, String roleName) {
        UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
        for (Authority auth : user.getAuthorities()) {
            if (auth.getAuthority().equals(userRole.getRoleAsString())) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdmin(User user) {
        return hasRole(user, "Admin");
    }

    private boolean isSelf(User user) {
        return (user.getUsername().equals(getCurrentUserId()));
    }

    private UserOutputDto updateAsAdmin(User user, UserInputDto userInputDto) {
        if (isSelf(user) && !userInputDto.getUserRole().toUpperCase().equals("ADMIN")) {
            throw new AccessDeniedException("You can't demote your own authority role");
        }

        User updatedUser = adminTransferUserInputDtoToUser(user, userInputDto);
        userRepository.save(updatedUser);
        return transferUserToUserOutputDto(updatedUser);
    }

    private UserOutputDto updateAsSelf(User userToUpdate, UserInputDto userInputDto) {
        User updatedUser = selfTransferUserInputDtoToUser(userToUpdate, userInputDto);
        userRepository.save(updatedUser);
        return transferUserToUserOutputDto(updatedUser);
    }


}

