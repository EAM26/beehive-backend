package nl.novi.beehivebackend;

import nl.novi.beehivebackend.dtos.input.RosterInputDto;
import nl.novi.beehivebackend.models.Roster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

import static org.junit.jupiter.api.Assertions.*;

public class RosterTester {
    @Test
    public void testSetters() {
        Roster r = new Roster();
        assertNull(r.getStartOfWeek());
        assertNull(r.getEndOfWeek());
        r.setYear(2023);
        assertNull(r.getStartOfWeek());
        assertNull(r.getEndOfWeek());
        r.setWeekNumber(42);
        assertNotNull(r.getStartOfWeek());
        assertNotNull(r.getEndOfWeek());
        assertEquals(r.getStartOfWeek(), LocalDate.parse("2023-10-16"));
        assertEquals(r.getEndOfWeek(), LocalDate.parse("2023-10-22"));
        r.setWeekNumber(43);
        assertEquals(r.getStartOfWeek(), LocalDate.parse("2023-10-23"));
        assertEquals(r.getEndOfWeek(), LocalDate.parse("2023-10-29"));
    }

    @Test
    public void testDtoMapper() {
        ModelMapper mm = new ModelMapper();

        RosterInputDto rid = new RosterInputDto();
        rid.setYear(2023);
        rid.setWeekNumber(42);

        Roster r = new Roster();

        mm.map(rid, r);

        assertEquals(r.getStartOfWeek(), LocalDate.parse("2023-10-16"));
        assertEquals(r.getEndOfWeek(), LocalDate.parse("2023-10-22"));
    }
}
