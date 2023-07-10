package tech.cristianciobanu.contactsapi.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.cristianciobanu.contactsapi.auth.exception.NotAuthorizedException;
import tech.cristianciobanu.contactsapi.skill.Skill;
import tech.cristianciobanu.contactsapi.skill.SkillRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final SkillRepository skillRepository;
    private final ContactMapper contactMapper;

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

    public ContactDto create(ContactDto contact) {
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        contact.setCreatedBy(loggedUsername);
        contact.setSkills(this.setContactSkills(contact));

        Contact createdContact = contactRepository.save(contactMapper.contactDtoToContact(contact));
        return contactMapper.contactToContactDto(createdContact);
    }

    public Optional<ContactDto> update(UUID id, ContactDto updatedContact) {
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return contactRepository.findById(id)
                .map(foundContact -> {
                    if (!foundContact.getCreatedBy().equals(loggedUsername)){
                        throw new NotAuthorizedException("User is not authorized to modify this contact");
                    }
                    Contact newContact = updateContactWith(foundContact, updatedContact);
                    return contactMapper.contactToContactDto(contactRepository.save(newContact));});
    }

    public void delete(UUID id) {
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        var foundContact = contactRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Contact not found"));
        if (!foundContact.getCreatedBy().equals(loggedUsername)){
            throw new NotAuthorizedException("User is not authorized to modify this contact");
        }
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
                oldContact.getCreatedBy()
        );
    }

    private Set<Skill> setContactSkills(ContactDto contact){
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        HashSet<Skill> skills = new HashSet<>();
        contact.getSkills().forEach(skill -> {
            Skill foundSkill = this.skillRepository.findSkillByNameContainingIgnoreCaseAndLevel(skill.getName(), skill.getLevel());
            if (foundSkill != null) {
                skills.add(foundSkill);
            } else {
                skill.setCreatedBy(loggedUsername);
                skills.add(this.skillRepository.save(skill));
            }
        });
        return skills;
    }
}
