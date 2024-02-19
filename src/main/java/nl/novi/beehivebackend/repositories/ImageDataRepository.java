package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {

}
