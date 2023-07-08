package tech.cristianciobanu.contactsapi.skill;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillDto>> findAllSkills(@RequestParam(required = false) String name){
        List<SkillDto> skills = new ArrayList<>();

        if (name == null){
            skillService.findAll().forEach(skills::add);
        } else {
            skillService.findAllByName(name).forEach(skills::add);
        }
        if (skills.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(skills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> findSkillById(@PathVariable("id") UUID id){
        Optional<SkillDto> skill = skillService.findById(id);

        return ResponseEntity.of(skill);
    }

    @PostMapping
    public ResponseEntity<SkillDto> createSkill(@RequestBody SkillDto skill){
        try {
            SkillDto createdSkill = skillService.create(skill);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdSkill.getId())
                    .toUri();
            return ResponseEntity.created(location).body(createdSkill);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillDto> updateSkill(
            @PathVariable("id") UUID id,
            @RequestBody SkillDto updatedSkill
    ){
        Optional<SkillDto> updated = skillService.update(id, updatedSkill);

        return updated
                .map(skill -> ResponseEntity.ok().body(skill))
                .orElseGet(() -> {
                    SkillDto createdSkill = skillService.create(updatedSkill);
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(createdSkill.getId())
                            .toUri();
                    return ResponseEntity.created(location).body(createdSkill);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SkillDto> deleteSkill(@PathVariable("id") UUID id){
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
