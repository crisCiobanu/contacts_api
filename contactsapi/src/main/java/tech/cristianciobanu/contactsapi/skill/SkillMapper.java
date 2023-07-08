package tech.cristianciobanu.contactsapi.skill;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    SkillMapper INSTANCE = Mappers.getMapper(SkillMapper.class);

    SkillDto skillToSkillDto(Skill skill);
    Skill skillDtoToSkill(SkillDto skillDto);
}
