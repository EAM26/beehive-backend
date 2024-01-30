package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.TeamInputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDtoDetails;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.models.*;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.RosterRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import nl.novi.beehivebackend.utils.MyDateTimeFormatter;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Captor
    ArgumentCaptor<Team> captor;

    Employee emp1;
    List<Employee> employees = new ArrayList<>();
    List<Shift> shifts;
    List<Roster> rosters = new ArrayList<>();
    Roster roster;
    Shift shift1;
    Shift shift2;
    Team team1;
    Team team2;
    TeamInputDto teamInputDto1;
    TeamInputDto teamInputDto2;
    TeamOutputDto teamOutputDto1;
    TeamOutputDto teamOutputDto2;
    TeamOutputDtoDetails teamOutputDtoDetails1;

    @BeforeEach
    void setUp() {
//        Arrange
        emp1 = new Employee();
        emp1.setId(101L);
        emp1.setShortName("jan");
        emp1.setShifts(shifts);
        employees.add(emp1);
        shift1 = new Shift(201L, MyDateTimeFormatter.getDateTime("2023-02-20 09:00:00"), MyDateTimeFormatter.getDateTime("2023-02-20 17:00:00"), 1, 2023, team1, emp1, roster);
        shift2 = new Shift(202L, MyDateTimeFormatter.getDateTime("2023-02-21 09:00:00"), MyDateTimeFormatter.getDateTime("2023-02-21 17:00:00"), 1, 2023, team1, emp1, roster);
        roster = new Roster(601L, 52, 2022, shifts, team1);
        rosters.add(roster);
        team1 = new Team("Kitchen", true, employees, shifts, rosters);
        team2 = new Team("Kitchen", false, employees, shifts, rosters);
        teamInputDto1 = new TeamInputDto("Kitchen", true);
        teamInputDto2 = new TeamInputDto("Kitchen", false);
        teamOutputDto1 = new TeamOutputDto("Kitchen", true);
        teamOutputDto2 = new TeamOutputDto("Kitchen", false);
        teamOutputDtoDetails1 = new TeamOutputDtoDetails("Kitchen", true, employees, rosters);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateTeamShouldThrowException() {
//        Arrange
        String originalTeamName = "Kitchen";
        teamInputDto1.setTeamName("badTeamName");
        when(teamRepository.findById(originalTeamName)).thenReturn(Optional.of(team1));

//        Act and Assert
        BadRequestException thrownException = assertThrows(BadRequestException.class, () -> teamService.updateTeam(originalTeamName, teamInputDto1));
        assertEquals("Not allowed to change the Team name.", thrownException.getMessage());
    }


    @Test
    void shouldUpdateTeam() {
//        Arrange
        String teamName = "Kitchen";
        when(teamRepository.findById(teamName)).thenReturn(Optional.of(team1));
        when(teamRepository.save(any(Team.class))).thenReturn(team2);

//        Act
        TeamOutputDto actual = teamService.updateTeam(teamInputDto2.getTeamName(), teamInputDto2);

//        Assert
        verify(teamRepository).save(captor.capture());
        Team capturedTeam = captor.getValue();
        assertNotNull(capturedTeam);
        assertEquals(teamName, capturedTeam.getTeamName());
        assertEquals(teamInputDto2.getIsActive(), capturedTeam.getIsActive());
        assertNotNull(actual);
        assertEquals(teamName, actual.getTeamName());
        assertEquals(teamInputDto2.getIsActive(), actual.getIsActive());
    }

    @Test
    void shouldCreateTeam() {
//        Arrange
        when(teamRepository.save(any(Team.class))).thenReturn(team1);
//        Act
        teamService.createTeam(teamInputDto1);

//        Assert
        verify(teamRepository, times(1)).save(captor.capture());
        Team actual = captor.getValue();

        assertNotNull(actual);
        assertEquals(team1.getTeamName(), actual.getTeamName());
        assertEquals(team1.getIsActive(), actual.getIsActive());
    }


    @Test
    void createTeamShouldThrowException() {
//        Arrange
        when(teamRepository.existsById(teamInputDto2.getTeamName())).thenReturn(true);

//        Act and Assert
        BadRequestException thrownException = assertThrows(BadRequestException.class, () -> teamService.createTeam(teamInputDto2));
        assertEquals("A team with that name already exists.", thrownException.getMessage());
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
    void shouldGetAllActiveTeams() {
//        Arrange
        List<TeamOutputDto> expectedTeams = Collections.singletonList(teamOutputDto1);
        when(teamRepository.findAll()).thenReturn(List.of(team1));

//        Act
        Iterable<TeamOutputDto> actualTeamDtos = teamService.getAllTeams(true);
        List<TeamOutputDto> actualTeams = new ArrayList<>();
        actualTeamDtos.forEach(actualTeams::add);

        // Assert
        assertNotNull(actualTeams);
        assertEquals(expectedTeams.size(), actualTeams.size());

        for (int i = 0; i < expectedTeams.size(); i++) {
            TeamOutputDto expected = expectedTeams.get(i);
            TeamOutputDto actual = actualTeams.get(i);

            assertEquals(expected.getTeamName(), actual.getTeamName());
            assertEquals(expected.getIsActive(), actual.getIsActive());
        }
    }

    @Test
    void shouldGetAllInactiveTeams() {
//        Arrange
        List<TeamOutputDto> expectedTeams = Collections.singletonList(teamOutputDto2);
        when(teamRepository.findAll()).thenReturn(List.of(team2));

//        Act
        Iterable<TeamOutputDto> actualTeamDtos = teamService.getAllTeams(false);
        List<TeamOutputDto> actualTeams = new ArrayList<>();
        actualTeamDtos.forEach(actualTeams::add);

        // Assert
        assertNotNull(actualTeams);
        assertEquals(expectedTeams.size(), actualTeams.size());

        for (int i = 0; i < expectedTeams.size(); i++) {
            TeamOutputDto expected = expectedTeams.get(i);
            TeamOutputDto actual = actualTeams.get(i);

            assertEquals(expected.getTeamName(), actual.getTeamName());
            assertEquals(expected.getIsActive(), actual.getIsActive());
        }

    }


    @Test
    void shouldGetTeamByTeamName() {
        // Arrange

        when(teamRepository.findById(team1.getTeamName())).thenReturn(Optional.of(team1));
        List<Employee> employees1 = Collections.singletonList(emp1);
        when(employeeRepository.findAllByTeam(team1)).thenReturn(employees1);
        List<Roster> rosters1 = Collections.singletonList(roster);
        when(rosterRepository.findAllByTeam(team1)).thenReturn(rosters1);

        // Act
        TeamOutputDtoDetails actual = teamService.getTeam(team1.getTeamName());

        // Assert
        assertEquals(teamOutputDtoDetails1.getTeamName(), actual.getTeamName());

        assertEquals(teamOutputDtoDetails1.getEmployees(), actual.getEmployees());
        System.out.println(teamOutputDtoDetails1.getRosters());
        System.out.println(actual.getRosters());
        System.out.println(teamOutputDtoDetails1.getEmployees());
        System.out.println(actual.getEmployees());
        assertEquals(teamOutputDtoDetails1.getRosters(), actual.getRosters());
        assertEquals(teamOutputDtoDetails1.getIsActive(), actual.getIsActive());
    }




}