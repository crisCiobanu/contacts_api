package tech.cristianciobanu.contactsapi.skill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.cristianciobanu.contactsapi.contact.Contact;
import tech.cristianciobanu.contactsapi.user.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ESkillLevel level;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "skills")
    private Set<Contact> contacts = new HashSet<>();
//    private String createdBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill )) return false;
        return id != null && id.equals(((Skill) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
