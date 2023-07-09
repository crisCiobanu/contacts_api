package tech.cristianciobanu.contactsapi.skill;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.cristianciobanu.contactsapi.auth.exception.NotAuthorizedException;
import tech.cristianciobanu.contactsapi.skill.exception.SkillUsedByAnotherContact;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;


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

    public SkillDto create(SkillDto skill){
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        skill.setCreatedBy(loggedUsername);

        Skill createdSkill = skillRepository.save(skillMapper.skillDtoToSkill(skill));
        return skillMapper.skillToSkillDto(createdSkill);
    }

    public Optional<SkillDto> update(UUID id, SkillDto updatedSkill){
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return skillRepository.findById(id)
                .map(foundSkill -> {
                    if (!foundSkill.getCreatedBy().equals(loggedUsername)){
                        throw new NotAuthorizedException("User is not authorized to modify this skill");
                    }
                    Skill newSkill = updateSkillWith(foundSkill, updatedSkill);
                    return skillMapper.skillToSkillDto(skillRepository.save(newSkill));});
    }

    public void delete(UUID id){
        var loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        var foundSkill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        if (!foundSkill.getCreatedBy().equals(loggedUsername)){
            throw new NotAuthorizedException("User is not authorized to delete this skill");
        }
        if (!foundSkill.getContacts().isEmpty()){
            throw new SkillUsedByAnotherContact("Skill is used by other contacts");
        }
        skillRepository.deleteById(id);
    }

    private Skill updateSkillWith(Skill oldSkill, SkillDto newSkill){
        return new Skill(
                oldSkill.getId(),
                newSkill.getName(),
                newSkill.getLevel(),
                new HashSet<>(newSkill.getContacts()),
                oldSkill.getCreatedBy()
        );
    }

}
