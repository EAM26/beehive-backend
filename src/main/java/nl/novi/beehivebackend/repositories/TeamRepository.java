package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {
    Boolean existsByTeamNameIgnoreCase(String teamName);

}
