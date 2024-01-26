package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.output.AbsenceOutputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDto;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.RosterRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import nl.novi.beehivebackend.utils.MyDateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    RosterRepository rosterRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamService teamService;

    ArgumentCaptor<Team> team;

    Employee emp1;
    Employee emp2;
    List<Employee> employees;
    List<Shift> shifts;
    List<Roster> rosters;
    Roster roster;
    Shift shift1;
    Shift shift2;
    Team team1;
    Team team2;
    TeamOutputDto teamOutputDto1;

    @BeforeEach
    void setUp() {
//        Arrange
        emp1 = new Employee();
        emp1.setId(101L);
        emp1.setShortName("jan");
        emp1.setShifts(shifts);
        shift1 = new Shift(201L, MyDateTimeFormatter.getDateTime("2023-02-20 09:00:00"), MyDateTimeFormatter.getDateTime("2023-02-20 17:00:00"), 1, 2023, team1, emp1, roster);
        shift2 = new Shift(202L, MyDateTimeFormatter.getDateTime("2023-02-21 09:00:00"), MyDateTimeFormatter.getDateTime("2023-02-21 17:00:00"), 1, 2023, team1, emp1, roster);
        roster = new Roster(601L, 52, 2022, shifts, team1);
        team1 = new Team("Kitchen", true, employees, shifts, rosters);
        team2 = new Team("Kitchen", false, employees, shifts, rosters);
        teamOutputDto1 = new TeamOutputDto("Kitchen", true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldGetAllTeams() {
//        Arrange
        List<TeamOutputDto> expectedTeams = Collections.singletonList(teamOutputDto1);
        when(teamRepository.findAll()).thenReturn(List.of(team1));

//        Act
        Iterable<TeamOutputDto> actualTeamDtos = teamService.getAllTeams();
        List<TeamOutputDto> actualTeams = new ArrayList<>();
        actualTeamDtos.forEach(actualTeams::add);

        // Assert
        assertNotNull(actualTeams);
        assertEquals(expectedTeams.size(), actualTeams.size(), "Returned list size should match.");

        for (int i = 0; i < expectedTeams.size(); i++) {
            TeamOutputDto expected = expectedTeams.get(i);
            TeamOutputDto actual = actualTeams.get(i);

            assertEquals(expected.getTeamName(), actual.getTeamName());
            assertEquals(expected.getIsActive(), actual.getIsActive());
        }
    }

    @Test
    void shouldGetAllTeamsActive() {
//        Arrange
        List<TeamOutputDto> expectedTeams = Collections.singletonList(teamOutputDto1);
        when(teamRepository.findAll()).thenReturn(List.of(team1));

//        Act
        Iterable<TeamOutputDto> actualTeamDtos = teamService.getAllTeams(team1.getIsActive());
        List<TeamOutputDto> actualTeams = new ArrayList<>();
        actualTeamDtos.forEach(actualTeams::add);

        // Assert
        assertNotNull(actualTeams);
        assertEquals(expectedTeams.size(), actualTeams.size(), "Returned list size should match.");

        for (int i = 0; i < expectedTeams.size(); i++) {
            TeamOutputDto expected = expectedTeams.get(i);
            TeamOutputDto actual = actualTeams.get(i);

            assertEquals(expected.getTeamName(), actual.getTeamName());
            assertEquals(expected.getIsActive(), actual.getIsActive());
        }
    }

    @Test
    void testGetAllTeams() {
    }

    @Test
    void getTeam() {
    }

    @Test
    void createTeam() {
    }

    @Test
    void updateTeam() {
    }

    @Test
    void deleteTeam() {
    }
}