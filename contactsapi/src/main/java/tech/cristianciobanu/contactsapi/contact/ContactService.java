package tech.cristianciobanu.contactsapi.contact;

import org.springframework.stereotype.Service;
import tech.cristianciobanu.contactsapi.skill.Skill;
import tech.cristianciobanu.contactsapi.skill.SkillDto;

import java.util.*;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;


    public ContactService(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    public List<ContactDto> findAll() {
        List<ContactDto> contactList = new ArrayList<>();
        contactRepository.findAll().forEach(contact -> contactList.add(contactMapper.contactToContactDto(contact)));
        return contactList;
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
        Contact createdContact = contactRepository.save(contactMapper.contactDtoToContact(contact));
        return contactMapper.contactToContactDto(createdContact);
    }

    public Optional<ContactDto> update(UUID id, ContactDto updatedContact) {
        return contactRepository.findById(id)
                .map(foundContact -> {
                    Contact newContact = updateContactWith(foundContact, updatedContact);
                    return contactMapper.contactToContactDto(contactRepository.save(newContact));});
    }

    public void delete(UUID id) {
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
                new HashSet<>(newContact.getSkills())
        );
    }
}
