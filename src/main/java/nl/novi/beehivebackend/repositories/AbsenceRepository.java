package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
}
