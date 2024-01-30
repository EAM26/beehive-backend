package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.AbsenceInputDto;
import nl.novi.beehivebackend.dtos.output.AbsenceOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.models.*;
import nl.novi.beehivebackend.repositories.AbsenceRepository;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.ShiftRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AbsenceServiceTest {

    @Mock
    AbsenceRepository absenceRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    ShiftRepository shiftRepository;

    @InjectMocks
    AbsenceService absenceService;

    @Captor
    ArgumentCaptor<Absence> captor;

    Absence absence1;
    Absence absence2;
    Absence absenceNoOverlap;
    Absence absenceOverlap;
    AbsenceInputDto absenceInputDto1;
    AbsenceInputDto absenceInputDtoNoOverlap;
    AbsenceInputDto absenceInputDtoOverlap;
    AbsenceOutputDto absenceOutputDto1;
    AbsenceOutputDto absenceOutputDto2;
    Shift shiftOverlap;
    Shift shiftNoOverlap;
    List<Shift> shifts;
    Team team;
    Roster roster;

    Employee emp1;

    @BeforeEach
    void setUp() {

        shifts = new ArrayList<>();
        emp1 = new Employee();
        emp1.setId(101L);
        emp1.setShortName("jan");
        emp1.setShifts(shifts);
        team = new Team();
        roster = new Roster();

        absence1 = new Absence(301L, LocalDate.parse("2023-01-15"), LocalDate.parse("2023-01-18"), emp1);
        absence2 = new Absence(302L, LocalDate.parse("2023-01-20"), LocalDate.parse("2023-01-30"), emp1);
        shiftOverlap = new Shift(201L, MyDateTimeFormatter.getDateTime("2023-02-20 09:00:00"), MyDateTimeFormatter.getDateTime("2023-02-20 17:00:00"), 1, 2023, team, emp1, roster);
        shiftNoOverlap = new Shift(201L, MyDateTimeFormatter.getDateTime("2023-01-20 09:00:00"), MyDateTimeFormatter.getDateTime("2023-01-20 17:00:00"), 1, 2023, team, emp1, roster);
        absenceNoOverlap = new Absence(303L, LocalDate.parse("2023-01-20"), LocalDate.parse("2023-01-30"), emp1);
        absenceOverlap = new Absence(303L, LocalDate.parse("2023-02-20"), LocalDate.parse("2023-02-28"), emp1);


        absenceInputDto1 = new AbsenceInputDto(LocalDate.parse("2023-01-15"), LocalDate.parse("2023-01-18"), 101L);
        absenceInputDtoNoOverlap = new AbsenceInputDto(LocalDate.parse("2024-01-20"), LocalDate.parse("2024-01-30"), 101L);
        absenceInputDtoOverlap = new AbsenceInputDto(LocalDate.parse("2023-02-20"), LocalDate.parse("2023-02-28"), 101L);

        absenceOutputDto1 = new AbsenceOutputDto(301L, LocalDate.parse("2023-01-15"), LocalDate.parse("2023-01-18"), 101L, "jan");
        absenceOutputDto2 = new AbsenceOutputDto(302L, LocalDate.parse("2023-01-20"), LocalDate.parse("2023-01-30"), 101L, "jan");

    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void deleteAbsence() {
//        Arrange
        when(absenceRepository.findById(absence1.getId())).thenReturn(Optional.of(absence1));

//        Act
        absenceService.deleteAbsence(absence1.getId());

//        Assert
        verify(absenceRepository).delete(absence1);
    }


    @Test
    void shouldCreateAbsence() {
        // Arrange
        when(employeeRepository.findById(101L)).thenReturn(Optional.of(emp1));
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence1);

        // Act
        absenceService.createAbsence(absenceInputDto1);

        // Assert
        verify(absenceRepository, times(1)).save(captor.capture());
        Absence actual = captor.getValue();

        assertNotNull(actual);
        assertEquals(absence1.getStartDate(), actual.getStartDate());
        assertEquals(absence1.getEndDate(), actual.getEndDate());
        assertEquals(emp1.getId(), actual.getEmployee().getId());
    }

    @Test
    void AbsenceToAbsenceOverlap() {
        // Arrange
        when(employeeRepository.findById(101L)).thenReturn(Optional.of(emp1));
        List<Absence> existingAbsences = List.of(absence1, absenceOverlap);
        when(absenceRepository.findByEmployeeId(101L)).thenReturn(existingAbsences);

        // Act

        // Assert
        BadRequestException thrownException = assertThrows(BadRequestException.class,
                () -> absenceService.createAbsence(absenceInputDtoOverlap));
        assertEquals("Period is overlapping existing absence", thrownException.getMessage());


    }

    @Test
    void AbsenceToShiftOverlap() {
        // Arrange
        when(employeeRepository.findById(101L)).thenReturn(Optional.of(emp1));
        List<Shift> existingShifts = List.of(shiftNoOverlap, shiftOverlap);
        when(shiftRepository.findByEmployeeId(101L)).thenReturn(existingShifts);

        // Act

        // Assert
        BadRequestException thrownException = assertThrows(BadRequestException.class,
                () -> absenceService.createAbsence(absenceInputDtoOverlap));
        assertEquals("Period is overlapping shift", thrownException.getMessage());


    }

    @Test
    void shouldGetAllAbsences() {
        // Arrange
        List<AbsenceOutputDto> expectedAbsences = List.of(absenceOutputDto1, absenceOutputDto2);
        when(absenceRepository.findAll()).thenReturn(List.of(absence1, absence2));

        // Act
        Iterable<AbsenceOutputDto> actualAbsencesIterable = absenceService.getAllAbsences();
        List<AbsenceOutputDto> actualAbsences = new ArrayList<>();
        actualAbsencesIterable.forEach(actualAbsences::add);

        // Assert
        assertNotNull(actualAbsences, "Returned list should not be null.");
        assertEquals(expectedAbsences.size(), actualAbsences.size(), "Returned list size should match.");

        for (int i = 0; i < expectedAbsences.size(); i++) {
            AbsenceOutputDto expected = expectedAbsences.get(i);
            AbsenceOutputDto actual = actualAbsences.get(i);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getStartDate(), actual.getStartDate());
            assertEquals(expected.getEndDate(), actual.getEndDate());
            assertEquals(expected.getEmployeeId(), actual.getEmployeeId());

        }
    }

    @Test
    void shouldGetSingleAbsence() {
        // Arrange
        when(absenceRepository.findById(absence1.getId())).thenReturn(Optional.of(absence1));

        // Act
        AbsenceOutputDto actual = absenceService.getSingleAbsence(absence1.getId());

        // Assert
        assertEquals(absenceOutputDto1.getId(), actual.getId());
        assertEquals(absenceOutputDto1.getStartDate(), actual.getStartDate());
        assertEquals(absenceOutputDto1.getEndDate(), actual.getEndDate());
        assertEquals(absenceOutputDto1.getEmployeeId(), actual.getEmployeeId());

    }



}