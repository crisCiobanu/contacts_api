package tech.cristianciobanu.contactsapi.contact;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;


    public ContactService(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    public List<ContactDto> findAll() {
        List<ContactDto> list = new ArrayList<>();
        contactRepository.findAll().forEach(contact -> list.add(contactMapper.contactToContactDto(contact)));
        return list;
    }

    public Optional<Contact> findById(UUID id) {
        return contactRepository.findById(id);
    }

    public Contact create(ContactDto contact) {
        Contact copy = new Contact();
        copy.setFirstName(contact.getFirstName());
        copy.setLastName(contact.getLastName());
        copy.setFullName(contact.getFullName());
        copy.setAddress(contact.getAddress());
        copy.setEmail(contact.getEmail());
        copy.setPhoneNumber(contact.getPhoneNumber());
        return contactRepository.save(copy);
    }

    public Optional<Contact> update(UUID id, ContactDto newContact) {
        Optional<Contact> foundContact = contactRepository.findById(id);
        if (foundContact.isPresent()){
            Contact contact = foundContact.get();
            contact.setFirstName(newContact.getFirstName());
            contact.setLastName(newContact.getLastName());
            contact.setFullName(newContact.getFullName());
            contact.setAddress(newContact.getAddress());
            contact.setEmail(newContact.getEmail());
            contact.setPhoneNumber(newContact.getPhoneNumber());

        }
        return contactRepository.findById(id)
                .map(oldContact -> {
                    oldContact.setFirstName(newContact.getFirstName());
                    oldContact.setLastName(newContact.getLastName());
                    oldContact.setFullName(newContact.getFullName());
                    oldContact.setAddress(newContact.getAddress());
                    oldContact.setEmail(newContact.getEmail());
                    oldContact.setPhoneNumber(newContact.getPhoneNumber());
                    return contactRepository.save(oldContact);
                });
    }

    public void delete(UUID id) {
        contactRepository.deleteById(id);
    }
}
