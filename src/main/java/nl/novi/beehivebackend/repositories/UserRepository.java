package nl.novi.beehivebackend.repositories;


import nl.novi.beehivebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
