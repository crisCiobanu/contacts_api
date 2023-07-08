package tech.cristianciobanu.contactsapi.contact;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.cristianciobanu.contactsapi.skill.Skill;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String address;
    private String email;
    private String phoneNumber;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "contact_skills",
            joinColumns = { @JoinColumn(name = "contact_id") },
            inverseJoinColumns = { @JoinColumn(name = "skill_id") })
    private Set<Skill> skills = new HashSet<>();
}
