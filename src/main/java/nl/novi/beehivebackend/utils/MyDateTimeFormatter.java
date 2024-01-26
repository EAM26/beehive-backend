package nl.novi.beehivebackend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MyDateTimeFormatter {

    public static LocalDateTime getDateTime(String dateTimeAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return(LocalDateTime.parse(dateTimeAsString, formatter));
    }
}
