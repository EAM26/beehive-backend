package nl.novi.beehivebackend.dtos.output;

import nl.novi.beehivebackend.models.Authority;

import java.util.Set;

public class UserOutputDto {

    public String username;
    public String email;
    public Long employeeId;

    public Set<Authority> authorities;

    public String getUsername() {
        return username;
    }



    public String getEmail() {
        return email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}

