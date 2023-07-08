package tech.cristianciobanu.contactsapi.contact;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.cristianciobanu.contactsapi.skill.Skill;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String address;
    private String email;
    private String phoneNumber;
    private Set<Skill> skills;
}
