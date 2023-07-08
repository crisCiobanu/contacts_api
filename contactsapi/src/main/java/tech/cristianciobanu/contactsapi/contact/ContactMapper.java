package tech.cristianciobanu.contactsapi.contact;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);
    ContactDto contactToContactDto(Contact contact);
    Contact contactDtoToContact(ContactDto contactDto);
}
