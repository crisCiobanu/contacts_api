package tech.cristianciobanu.contactsapi.validation.skill;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SkillLevelValidator.class)
public @interface SkillLevel {
    String message() default "Skill level should be one of <BEGINNER, COMPETENT, ADVANCED, PROFICIENT, EXPERT>";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
