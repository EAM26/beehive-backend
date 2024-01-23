package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

    @Mock
    ShiftRepository shiftRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    RosterRepository rosterRepository;

    @Mock
    AbsenceRepository absenceRepository;

    @InjectMocks
    private ShiftService shiftService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void isValidShiftDuration_WithValidDuration_ShouldReturnTrue() {
        ShiftInputDto shiftInputDto = new ShiftInputDto();
        shiftInputDto.setStartShift(LocalDateTime.of(2023, 1, 1, 9, 0));
        shiftInputDto.setEndShift(LocalDateTime.of(2023, 1, 1, 17, 0));

        assertTrue(shiftService.isValidShiftDuration(shiftInputDto));
    }

//    @Test
//    void isValidShiftDuration_WithEndBeforeStart() {
//        ShiftInputDto shiftInputDto = new ShiftInputDto();
//        shiftInputDto.setStartShift(LocalDateTime.of(2023, 1, 1, 9, 0));
//        shiftInputDto.setEndShift(LocalDateTime.of(2023, 1, 1, 8, 0));
//
//        assertThrows(BadRequestException.class)
//        assertTrue(shiftService.isValidShiftDuration(shiftInputDto));
//    }
}