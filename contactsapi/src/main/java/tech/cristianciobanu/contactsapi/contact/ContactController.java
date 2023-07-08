package tech.cristianciobanu.contactsapi.contact;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.cristianciobanu.contactsapi.skill.SkillDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/contacts")
public class ContactController {
    private final ContactService contactService;


    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<ContactDto>> findAllContacts(@RequestParam(required = false) String name){
        List<ContactDto> contacts = new ArrayList<>();

        if (name == null){
            contactService.findAll().forEach(contacts::add);
        } else {
            contactService.findAllByName(name).forEach(contacts::add);
        }
        if (contacts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> findContactById(@PathVariable("id") UUID id){
        Optional<ContactDto> contact = contactService.findById(id);

        return ResponseEntity.of(contact);
    }

    @PostMapping
    public ResponseEntity<ContactDto> createContact(@RequestBody ContactDto contact){
        try {
            ContactDto createdContact = contactService.create(contact);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdContact.getId())
                    .toUri();
            return ResponseEntity.created(location).body(createdContact);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDto> updateContact(
            @PathVariable("id") UUID id,
            @RequestBody ContactDto updatedContact
    ){
        Optional<ContactDto> updated = contactService.update(id, updatedContact);

        return updated
                .map(skill -> ResponseEntity.ok().body(skill))
                .orElseGet(() -> {
                    ContactDto createdContact = contactService.create(updatedContact);
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(createdContact.getId())
                            .toUri();
                    return ResponseEntity.created(location).body(createdContact);
                });
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ContactDto> deleteContact(@PathVariable("id") UUID id){
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
