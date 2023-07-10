package tech.cristianciobanu.contactsapi.validation.skill;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import tech.cristianciobanu.contactsapi.skill.ESkillLevel;

import java.util.Arrays;
import java.util.List;

@Component
public class SkillLevelValidator implements ConstraintValidator<SkillLevel, String> {
    private List<String> skillValues = Arrays.stream(ESkillLevel.values()).map(Enum::name).toList();
    @Override
    public boolean isValid(String skillLevel, ConstraintValidatorContext constraintValidatorContext) {
        if (skillLevel == null) {
            return true;
        }
        return skillValues.contains(skillLevel);
    }
}
