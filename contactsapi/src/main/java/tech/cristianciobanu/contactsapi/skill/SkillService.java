package tech.cristianciobanu.contactsapi.skill;

import org.springframework.stereotype.Service;
import tech.cristianciobanu.contactsapi.contact.ContactDto;

import java.util.*;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public SkillService(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    public List<SkillDto> findAll(){
        List<SkillDto> skillList = new ArrayList<>();
        skillRepository.findAll().forEach(skill -> skillList.add(skillMapper.skillToSkillDto(skill)));
        return skillList;
    }

    public Optional<SkillDto> findById(UUID id){
        return skillRepository.findById(id).map(skillMapper::skillToSkillDto);
    }

    public List<SkillDto> findAllByName(String name){
        List<SkillDto> skillList = new ArrayList<>();
        skillRepository.findByNameContainingIgnoreCase(name).forEach(skill -> skillList.add(skillMapper.skillToSkillDto(skill)));
        return skillList;
    }

    public SkillDto create(SkillDto skillDto){
        Skill createdSkill = skillRepository.save(skillMapper.skillDtoToSkill(skillDto));
        return skillMapper.skillToSkillDto(createdSkill);
    }

    public Optional<SkillDto> update(UUID id, SkillDto updatedSkill){
        return skillRepository.findById(id)
                .map(foundSkill -> {
                    Skill newSkill = updateSkillWith(foundSkill, updatedSkill);
                    return skillMapper.skillToSkillDto(skillRepository.save(newSkill));});
    }

    public void delete(UUID id){
        skillRepository.deleteById(id);
    }

    private Skill updateSkillWith(Skill oldSkill, SkillDto newSkill){
        return new Skill(
                oldSkill.getId(),
                newSkill.getName(),
                newSkill.getLevel(),
                new HashSet<>(newSkill.getContacts())
        );
    }

}
