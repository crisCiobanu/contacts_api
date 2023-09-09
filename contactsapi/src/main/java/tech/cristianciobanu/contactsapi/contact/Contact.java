package tech.cristianciobanu.contactsapi.contact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.cristianciobanu.contactsapi.skill.Skill;
import tech.cristianciobanu.contactsapi.user.User;

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

//    private String createdBy;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact )) return false;
        return id != null && id.equals(((Contact) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
