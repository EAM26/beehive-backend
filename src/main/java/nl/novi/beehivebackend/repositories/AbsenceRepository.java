package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findByEmployeeId(Long id);
}
