package nl.novi.beehivebackend.models;

public enum UserRole {
    ADMIN,
    MANAGER,
    USER;

    public String getRoleAsString() {
        String name = this.name(); // Returns the name of the enum constant as a string
        return "ROLE_" + name;
    }
}
