package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.UserInputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDto;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Authority;
import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.models.UserRole;
import nl.novi.beehivebackend.repositories.UserRepository;
import nl.novi.beehivebackend.utils.RandomStringGenerator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        List<UserOutputDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(transerUserToUserOutputDto(user));
        }
        return collection;
    }

    public UserOutputDto getUser(String username) {
        UserOutputDto userOutputDto = new UserOutputDto();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            userOutputDto = transerUserToUserOutputDto(user.get());
        }else {
            throw new UsernameNotFoundException(username);
        }
        return userOutputDto;
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUserName(UserInputDto userInputDto) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userInputDto.setApikey(randomString);
        User newUser = userRepository.save(transerUserInputDtoToUser(userInputDto));
        return newUser.getUsername();
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserInputDto newUser) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException();
        User user = userRepository.findById(username).get();
        user.setPassword(newUser.getPassword());
        userRepository.save(user);
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserOutputDto userOutputDto = transerUserToUserOutputDto(user);
        return userOutputDto.getAuthorities();
    }

    public void addAuthority(String username, UserRole userRole) {

        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, userRole));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }

    public static UserOutputDto transerUserToUserOutputDto(User user){

        var outputDto = new UserOutputDto();

        outputDto.username = user.getUsername();
        outputDto.password = user.getPassword();
        outputDto.enabled = user.isEnabled();
        outputDto.apikey = user.getApikey();
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
//        user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setEnabled(userInputDto.getEnabled());

        user.setApikey(userInputDto.getApikey());
        user.setEmail(userInputDto.getEmail());

        return user;
    }

}

