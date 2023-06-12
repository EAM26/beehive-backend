package nl.novi.beehivebackend.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ValidationUtils {

    public StringBuilder validationMessage(BindingResult br) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField() + " ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }
        return sb;
    }
}
