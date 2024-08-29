package com.fullcycle.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);
    ValidationHandler validate(Validation validation);


    default boolean hasErrors(Validation validation, String message) {
        return getErrors() != null && !getErrors().isEmpty();
    }

    List<Error> getErrors();

    interface Validation {

        void validate();
    }
}
