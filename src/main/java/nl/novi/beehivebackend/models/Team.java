package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="teams")
public class Team {

    @Id
    private String teamName;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<Employee> employees;




}
