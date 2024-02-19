package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email ;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Employee employee;


    @OneToOne(mappedBy = "user")
    private ImageData imageData;
    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public Long getEmployeeId() {
        return employee.getId();
    }


//    public void setImage(ImageData savedImage) {
//    }
}
