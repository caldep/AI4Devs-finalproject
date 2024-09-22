package com.spme.maintenance.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistingEquipmentValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingEquipment {
    String message() default "El ID del equipo debe existir en la tabla Equipment";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
