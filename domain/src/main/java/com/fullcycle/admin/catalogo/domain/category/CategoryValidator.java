package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private final Category category;

    public CategoryValidator(final Category category, final ValidationHandler validationHandler) {
        super(validationHandler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        if (this.category.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }

        if (this.category.getName().isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
        }

        if (this.category.getName().trim().length() < 3 || this.category.getName().trim().length() > 255) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
