package tech.cristianciobanu.contactsapi.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.cristianciobanu.contactsapi.contact.Contact;
import tech.cristianciobanu.contactsapi.validation.skill.SkillLevel;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SkillDto {
    private UUID id;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String name;
    @NotNull
    @SkillLevel
    private String level;
    private Set<Contact> contacts;
//    private String createdBy;
}
