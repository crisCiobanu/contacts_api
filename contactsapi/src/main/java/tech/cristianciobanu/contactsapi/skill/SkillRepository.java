package tech.cristianciobanu.contactsapi.skill;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillRepository extends CrudRepository<Skill, UUID> {
    Skill findSkillByNameContainingIgnoreCaseAndLevel(String name, ESkillLevel level);
    List<Skill> findByNameContainingIgnoreCase(String name);
}
