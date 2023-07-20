package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.exceptions.IllegalValueException;
import nl.novi.beehivebackend.exceptions.IsNotEmptyException;
import nl.novi.beehivebackend.exceptions.IsNotUniqueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalValueException.class)
        public ResponseEntity<Object> handleIllegalValueException(IllegalValueException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IsNotEmptyException.class)
    public ResponseEntity<Object> handleIsNotEmptyException(IsNotEmptyException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IsNotUniqueException.class)
    public ResponseEntity<Object> handleIsNotUniqueException(IsNotUniqueException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }



}
