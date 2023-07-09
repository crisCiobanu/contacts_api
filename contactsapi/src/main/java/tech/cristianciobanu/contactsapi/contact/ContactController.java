package tech.cristianciobanu.contactsapi.contact;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.cristianciobanu.contactsapi.auth.exception.NotAuthorizedException;
import tech.cristianciobanu.contactsapi.skill.SkillDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/contacts")
@Tag(name = "Contacts", description = "Contact management APIs")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Operation(
            summary = "Retrieve all the contacts",
            description = "Retrieve all the contacts, filter the results by the parameter name if provided")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ContactDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public ResponseEntity<List<ContactDto>> findAllContacts(
            @Parameter(description = "Search Contacts by first or lastname") @RequestParam(required = false) String name){
        try {
            List<ContactDto> contacts = contactService.findAll(name);
            return ResponseEntity.ok().body(contacts);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> findContactById(@PathVariable("id") UUID id){
        try {
            Optional<ContactDto> contact = contactService.findById(id);
            return ResponseEntity.of(contact);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<ContactDto> createContact(@Valid @RequestBody ContactDto contact){
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
            @Valid @RequestBody ContactDto updatedContact
    ){
        try {
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
        } catch (NotAuthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ContactDto> deleteContact(@PathVariable("id") UUID id){
        try {
            contactService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}
