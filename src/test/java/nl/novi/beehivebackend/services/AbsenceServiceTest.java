package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.output.AbsenceOutputDto;
import nl.novi.beehivebackend.models.Absence;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.repositories.AbsenceRepository;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbsenceServiceTest {

    @Mock
    AbsenceRepository absenceRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    AbsenceService absenceService;

    @Captor
    ArgumentCaptor<Absence> captor;

    Absence absence1;
    Absence absence2;
    AbsenceOutputDto absenceOutputDto1;
    AbsenceOutputDto absenceOutputDto2;

    Employee emp1;

    @BeforeEach
    void setUp() {

        emp1 = new Employee();
        emp1.setId(101L);
        emp1.setShortName("jan");

        absence1 = new Absence(301L, LocalDate.parse("2023-01-15"), LocalDate.parse("2023-01-18"), emp1);
        absence2 = new Absence(302L, LocalDate.parse("2023-01-20"), LocalDate.parse("2023-01-30"), emp1);

        absenceOutputDto1 = new AbsenceOutputDto(301L, LocalDate.parse("2023-01-15"), LocalDate.parse("2023-01-18"), 101L, "jan");
        absenceOutputDto2 = new AbsenceOutputDto(302L, LocalDate.parse("2023-01-20"), LocalDate.parse("2023-01-30"), 101L, "jan");

    }

    @AfterEach
    void tearDown() {
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
        assertEquals(expectedAbsences.size(), ((List<?>) actualAbsences).size(), "Returned list size should match.");

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
        when(absenceRepository.findById(absence1.getId())).thenReturn(Optional.ofNullable(absence1));

        // Act
        AbsenceOutputDto actual = absenceService.getSingleAbsence(absence1.getId());

        // Assert
        assertEquals(absenceOutputDto1.getId(), actual.getId());
        assertEquals(absenceOutputDto1.getStartDate(), actual.getStartDate());
        assertEquals(absenceOutputDto1.getEndDate(), actual.getEndDate());
        assertEquals(absenceOutputDto1.getEmployeeId(), actual.getEmployeeId());

    }



    @Test
    void createAbsence() {
    }

    @Test
    void deleteAbsence() {
    }
}