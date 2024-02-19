package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.ImageData;
import nl.novi.beehivebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {

    boolean existsByUser (User user);
    Optional<ImageData> findByUser(User user);

}
