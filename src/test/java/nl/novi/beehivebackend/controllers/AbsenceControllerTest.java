package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.models.Absence;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.AbsenceRepository;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
class AbsenceControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AbsenceRepository absenceRepository;

    Employee emp1;
    Absence absence1;
    Absence absence2;
    Team team1;


    @BeforeEach
    void setUp() {

        absenceRepository.deleteAll();

        team1 = new Team();
        team1.setTeamName("Office");
        team1.setIsActive(true);
        teamRepository.save(team1);

        emp1 = new Employee();
        emp1.setId(1L);
        emp1.setFirstName("testFirstName");
        emp1.setShortName("testEmployee");
        emp1.setShifts(null);
        emp1.setTeam(team1);
        emp1.setUser(null);
        emp1.setIsActive(true);
        emp1.setLastName("testLastName");
        employeeRepository.save(emp1);
        absence1 = new Absence(1L, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 20), emp1);
        absence2 = new Absence(2L, LocalDate.of(2023, 11, 1), LocalDate.of(2023, 11, 20), emp1);
        absenceRepository.save(absence1);
        absenceRepository.save(absence2);
    }

    @AfterEach
    void tearDown() {
        absenceRepository.deleteAll();
    }

    @Test
    void getAllAbsences() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/absences"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].startDate").value(absence1.getStartDate().toString()))
                .andExpect(jsonPath("$[0].endDate").value(absence1.getEndDate().toString()))
                .andExpect(jsonPath("$[0].employeeId").value(absence1.getEmployee().getId()))
                .andExpect(jsonPath("$[0].employeeShortName").value(absence1.getEmployee().getShortName()))
                .andExpect(jsonPath("$[1].startDate").value(absence2.getStartDate().toString()))
                .andExpect(jsonPath("$[1].endDate").value(absence2.getEndDate().toString()))
                .andExpect(jsonPath("$[1].employeeId").value(absence2.getEmployee().getId()))
                .andExpect(jsonPath("$[1].employeeShortName").value(absence2.getEmployee().getShortName()))
        ;

    }

    @Test
    void getSingleAbsence() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/absences/"+ absence1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(absence1.getId()))
                .andExpect(jsonPath("$.startDate").value(absence1.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(absence1.getEndDate().toString()))
                .andExpect(jsonPath("$.employeeId").value(absence1.getEmployee().getId()))
                .andExpect(jsonPath("$.employeeShortName").value(absence1.getEmployee().getShortName()));
    }


}