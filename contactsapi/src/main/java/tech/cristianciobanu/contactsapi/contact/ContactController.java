package tech.cristianciobanu.contactsapi.contact;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

        }
        if (contacts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }




}
