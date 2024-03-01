package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "image_data")
@Getter
@Setter
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Lob
    private byte[] imageData;

//    @OneToOne
//    @JoinColumn(name  = "user_id", referencedColumnName = "username")
//    private User user;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name  = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
