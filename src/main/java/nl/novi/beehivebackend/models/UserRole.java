package nl.novi.beehivebackend.models;

import java.util.List;
import java.util.Set;

public enum UserRole {
    ADMIN,
    MANAGER,
    USER;


    public String getRoleAsString() {
        String name = this.name(); // Returns the name of the enum constant as a string
        return "ROLE_" + name;
    }
}
