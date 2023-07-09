package tech.cristianciobanu.contactsapi.skill.exception;

import tech.cristianciobanu.contactsapi.skill.SkillRepository;

public class SkillUsedByAnotherContact extends RuntimeException{
    public SkillUsedByAnotherContact(String message){
        super(message);
    }
}
