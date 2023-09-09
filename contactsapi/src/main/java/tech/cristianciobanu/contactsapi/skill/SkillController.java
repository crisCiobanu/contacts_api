package tech.cristianciobanu.contactsapi.skill;

import io.swagger.v3.oas.annotations.Operation;
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
import tech.cristianciobanu.contactsapi.skill.exception.SkillUsedByAnotherContact;

import java.net.URI;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/skills")
@Tag(name = "Skills", description = "Skill.java management APIs")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @Operation(
            summary = "Retrieve all the skills",
            description = "Retrieve all the skills, filter the results by the parameter name if provided")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = SkillDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public ResponseEntity<List<SkillDto>> findAllSkills(@RequestParam(required = false) String name){
        try {
            List<SkillDto> skills = this.skillService.findAll(name);
            return ResponseEntity.ok().body(skills);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Retrieve skill",
            description = "Retrieve a skill by it's id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = SkillDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> findSkillById(@PathVariable("id") UUID id){
        try {
            Optional<SkillDto> skill = skillService.findById(id);
            return ResponseEntity.of(skill);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Create skill",
            description = "Create a new skill")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = SkillDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    public ResponseEntity<SkillDto> createSkill(@Valid @RequestBody SkillDto skill){
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

    @Operation(
            summary = "Update skill;",
            description = "Update an existing skill using it's id, if the skill is not found a new one will be created" +
                    ", accessible just for the user who created the skill, otherwise returns 401")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = SkillDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/{id}")
    public ResponseEntity<SkillDto> updateSkill(
            @PathVariable("id") UUID id,
            @Valid @RequestBody SkillDto updatedSkill
    ){
        try {
            return skillService.update(id, updatedSkill)
                    .map(skill -> ResponseEntity.ok().body(skill))
                    .orElseGet(() -> {
                        SkillDto createdSkill = skillService.create(updatedSkill);
                        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(createdSkill.getId())
                                .toUri();
                        return ResponseEntity.created(location).body(createdSkill);
                    });
        } catch (NotAuthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Delete skill",
            description = "Delete an existing skill using it's id, accessible just for the user who created the skill, otherwise returns 401")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = SkillDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "409", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    public ResponseEntity<SkillDto> deleteSkill(@PathVariable("id") UUID id){
        try {
            skillService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SkillUsedByAnotherContact e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}
