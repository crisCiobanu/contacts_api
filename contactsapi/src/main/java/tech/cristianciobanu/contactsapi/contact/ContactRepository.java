package tech.cristianciobanu.contactsapi.contact;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends CrudRepository<Contact, UUID> {
    List<Contact> findByFirstName(String firstName);
    List<Contact> findByEmail(String email);
}
