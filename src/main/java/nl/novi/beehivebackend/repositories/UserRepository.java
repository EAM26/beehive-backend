package nl.novi.beehivebackend.repositories;


import nl.novi.beehivebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.domain.Sort;
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAll(Sort sort);

    User findByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByIsDeleted(Boolean isDeleted);

}
