package pl.przemek.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = URLUniqueValidator.class)
@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
public @interface URLUnique {
	String message() default "{pl.przemek.validation.URLUnique.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
