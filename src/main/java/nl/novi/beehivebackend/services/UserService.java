package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.UserInputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDtoDetails;
import nl.novi.beehivebackend.exceptions.*;
import nl.novi.beehivebackend.models.Authority;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.models.UserRole;
import nl.novi.beehivebackend.repositories.UserRepository;
import nl.novi.beehivebackend.utils.UserData;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserData userData;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserData userData) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userData = userData;
    }


    public List<UserOutputDto> getUsers(Boolean hasEmployee, Boolean isDeleted) {
        List<User> users = userRepository.findAll(Sort.by("username"));
        List<UserOutputDto> filteredUsersList = new ArrayList<>();
//        all Users
        if (hasEmployee == null && isDeleted == null) {
            for (User user : users) {
                filteredUsersList.add(transferUserToUserOutputDto(user));
            }
        } else if (hasEmployee == null && isDeleted != null) {
            for (User user : users) {
                if (user.getIsDeleted().equals(isDeleted)) {
                    filteredUsersList.add(transferUserToUserOutputDto(user));
                }
            }
        } else if (hasEmployee != null && isDeleted == null) {
            for (User user : users) {
                if (userHasEmployee(user) == hasEmployee) {
                    filteredUsersList.add(transferUserToUserOutputDto(user));
                }
            }
        } else {
            for (User user : users) {
                if (userHasEmployee(user) == hasEmployee && user.getIsDeleted().equals(isDeleted)) {
                    filteredUsersList.add(transferUserToUserOutputDto(user));
                }
            }
        }
        return filteredUsersList;
    }

    public UserOutputDtoDetails getSingleUser(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + username));
        return createProfile(user);
    }

    public UserOutputDtoDetails getSelfAsUser() {
        User user = userData.getLoggedInUser();
        return createProfile(user);
    }


    public UserOutputDto createUser(UserInputDto userInputDto) {
        if (userInputDto.getPassword() == null) {
            throw new BadRequestException("Password is required.");
        }
        User user = dtoToUserAsAdmin(new User(), userInputDto);
        userRepository.save(user);
        return transferUserToUserOutputDto(user);
    }

    public UserOutputDto updateSelf(UserInputDto userInputDto) {
        User currentUser = userRepository.findById(getCurrentUserId()).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + getCurrentUserId()));
//        User currentUser = userRepository.findByUsername(getCurrentUserId());
        User updatedUser = dtoToUserAsSelf(currentUser, userInputDto);
        userRepository.save(updatedUser);
        return transferUserToUserOutputDto(updatedUser);

    }


    public UserOutputDto updateUser(String username, UserInputDto userInputDto) {

        User userToUpdate = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("User with name " + username + " doesn't exist."));
        User currentUser = userRepository.findById(getCurrentUserId()).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + username));


//        Check if username is not changed
        if (!username.equals(userInputDto.getUsername())) {
            throw new AccessDeniedException("Not allowed to change username.");
        }

//      Check is Self or Admin
        if (isSelf(userToUpdate)) {
            return updateSelf(userInputDto);
        } else if (isAdmin(currentUser)) {
            User updatedUser = dtoToUserAsAdmin(userToUpdate, userInputDto);
            userRepository.save(updatedUser);
            return transferUserToUserOutputDto(updatedUser);
        } else {
            throw new AccessDeniedException("Insufficient permission for updating user.");
        }
    }

    public void deleteUser(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("No user found with name " + username));
        if(isSelf(user)) {
            throw new BadRequestException("Self delete not allowed");
        }
        if(userHasEmployee(user)) {
            throw new BadRequestException("User has employee data.");
        }
        userRepository.delete(user);
    }

//    Helper methods


    private UserOutputDtoDetails createProfile(User user) {
//        User data
        UserOutputDtoDetails userOutputDtoDetails = new UserOutputDtoDetails();
        userOutputDtoDetails.setUsername(user.getUsername());
        userOutputDtoDetails.setEmail(user.getEmail());
        userOutputDtoDetails.setAuthorities(user.getAuthorities());
        userOutputDtoDetails.setIsDeleted(user.getIsDeleted());

//        Employee data
        if (user.getEmployee() != null) {
            Employee employee = user.getEmployee();
            userOutputDtoDetails.setEmployee(employee);

            userOutputDtoDetails.setTeam(employee.getTeam());
            userOutputDtoDetails.setShifts(employee.getShifts());
            userOutputDtoDetails.setAbsences(employee.getAbsences());
        }

        return userOutputDtoDetails;
    }

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
            outputDto.setEmployee(user.getEmployee());
        }
        return outputDto;
    }

    private User dtoToUserAsAdmin(User user, UserInputDto userInputDto) {

//        Check username for new user
        if (user.getUsername() == null && userExists(userInputDto.getUsername())) {
            throw new IsNotUniqueException("Username is not unique");
        }

        if (!isRole(userInputDto.getUserRole())) {
            throw new BadRequestException("Unknown user role");
        }

//        Check email for new user
        userInputDto.setEmail(userInputDto.getEmail().toLowerCase());
        if (user.getEmail() == null) {
            if (emailExists(userInputDto.getEmail())) {
                throw new BadRequestException("Email is not unique");
            }
        }
//        Check email for existing user
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
//        If password == null in update -> keep old password
        if (userInputDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        }

        user.setEmail(userInputDto.getEmail());
        if (userInputDto.getIsDeleted() != null) {
            user.setIsDeleted(userInputDto.getIsDeleted());
        }
        UserRole userRole = UserRole.valueOf(userInputDto.getUserRole().toUpperCase());
        user.getAuthorities().clear();
        user.addAuthority(new Authority(user.getUsername(), userRole));
        return user;
    }

    private User dtoToUserAsSelf(User currentUser, UserInputDto userInputDto) {
        if (!currentUser.getUsername().equals(userInputDto.getUsername())) {
            throw new BadRequestException("Not allowed to change user name.");
        }

//          If password == null in update -> keep old password
        if (userInputDto.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        }

        if (!isRole(userInputDto.getUserRole())) {
            throw new BadRequestException("Unknown user role");
        }

        if (!hasRole(currentUser, userInputDto.getUserRole())) {
            throw new AccessDeniedException("Not allowed to change authority");
        }

        userInputDto.setEmail(userInputDto.getEmail().toLowerCase());
        if (!userInputDto.getEmail().equals(currentUser.getEmail()) && emailExists(userInputDto.getEmail())) {
            throw new IsNotUniqueException("Email is not unique");
        }

        if (currentUser.getIsDeleted() != userInputDto.getIsDeleted()) {
            throw new AccessDeniedException("Not allowed to change user status.");
        }

        currentUser.setEmail(userInputDto.getEmail());
        return currentUser;
    }

    private boolean isRole(String roleName) {
        for (UserRole userRole : UserRole.values()) {
            if (roleName.equalsIgnoreCase(userRole.name())) {
                return true;
            }
        }
        return false;
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


    private boolean userHasEmployee(User user) {
        return user.getEmployee() != null;
    }

}

