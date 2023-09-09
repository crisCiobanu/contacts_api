package tech.cristianciobanu.contactsapi.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.cristianciobanu.contactsapi.auth.exception.NotAuthorizedException;
import tech.cristianciobanu.contactsapi.skill.Skill;
import tech.cristianciobanu.contactsapi.skill.SkillRepository;
import tech.cristianciobanu.contactsapi.user.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final SkillRepository skillRepository;
    private final ContactMapper contactMapper;

    private final UserRepository userRepository;

    public List<ContactDto> findAll(String name) {
        if (name == null){
            List<ContactDto> contactList = new ArrayList<>();
            contactRepository.findAll().forEach(contact -> contactList.add(contactMapper.contactToContactDto(contact)));
            return contactList;
        } else {
            return findAllByName(name);
        }
    }

    public Optional<ContactDto> findById(UUID id) {
        return contactRepository.findById(id).map(contactMapper::contactToContactDto);
    }

    public List<ContactDto> findAllByName(String name) {
        List<ContactDto> contactList = new ArrayList<>();
        contactRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .forEach(contact -> contactList.add(contactMapper.contactToContactDto(contact)));
        return contactList;
    }

    public ContactDto create(ContactDto contactDTO) {
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        var loggedUser = userRepository.findByUsername(loggedUsername).orElseThrow(RuntimeException::new);

        contactDTO.setSkills(this.setContactSkills(contactDTO));

        Contact contact = contactMapper.contactDtoToContact(contactDTO);
        loggedUser.addContact(contact);

        Contact createdContact = contactRepository.save(contact);
        return contactMapper.contactToContactDto(createdContact);
    }

    public Optional<ContactDto> update(UUID id, ContactDto updatedContact) {
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return contactRepository.findById(id)
                .map(foundContact -> {
//                    if (!foundContact.getCreatedBy().equals(loggedUsername)){
//                        throw new NotAuthorizedException("User is not authorized to modify this contact");
//                    }
                    if (!foundContact.getOwner().getUsername().equals(loggedUsername)){
                        throw new NotAuthorizedException("User is not authorized to modify this contact");
                    }
                    Contact newContact = updateContactWith(foundContact, updatedContact);
                    return contactMapper.contactToContactDto(contactRepository.save(newContact));});
    }

    public void delete(UUID id) {
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        var loggedUser = userRepository.findByUsername(loggedUsername).orElseThrow(RuntimeException::new);
        var foundContact = contactRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Contact not found"));
        if (!foundContact.getOwner().getUsername().equals(loggedUsername)){
            throw new NotAuthorizedException("User is not authorized to modify this contact");
        }
        loggedUser.removeContact(foundContact);
        contactRepository.deleteById(id);
    }

    private Contact updateContactWith(Contact oldContact, ContactDto newContact){
        return new Contact(
                oldContact.getId(),
                newContact.getFirstName(),
                newContact.getLastName(),
                newContact.getFullName(),
                newContact.getAddress(),
                newContact.getEmail(),
                newContact.getPhoneNumber(),
                setContactSkills(newContact),
//                oldContact.getCreatedBy(),
                oldContact.getOwner()
        );
    }

    private Set<Skill> setContactSkills(ContactDto contact){
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        var loggedUser = userRepository.findByUsername(loggedUsername).orElseThrow(RuntimeException::new);
        HashSet<Skill> skills = new HashSet<>();
        contact.getSkills().forEach(skill -> {
            Skill foundSkill = this.skillRepository.findSkillByNameContainingIgnoreCaseAndLevel(skill.getName(), skill.getLevel());
            if (foundSkill != null) {
                skills.add(foundSkill);
            } else {
                loggedUser.addSkill(skill);
//                skill.setCreatedBy(loggedUsername);
                skills.add(this.skillRepository.save(skill));
            }
        });
        return skills;
    }
}
