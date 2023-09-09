package tech.cristianciobanu.contactsapi.user;

import jakarta.persistence.*;
import lombok.*;
import tech.cristianciobanu.contactsapi.contact.Contact;
import tech.cristianciobanu.contactsapi.role.Role;
import tech.cristianciobanu.contactsapi.skill.Skill;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Skill> skills = new HashSet<>();

    public void addContact(Contact contact) {
        contacts.add(contact);
        contact.setOwner(this);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
        contact.setOwner(null);
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
        skill.setOwner(this);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
        skill.setOwner(null);
    }
}
