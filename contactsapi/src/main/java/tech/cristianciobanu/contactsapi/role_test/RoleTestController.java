package tech.cristianciobanu.contactsapi.role_test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
public class RoleTestController {
    @GetMapping("/admin")
    public ResponseEntity<String> getAdminInfo(){
        return ResponseEntity.ok("You have the admin rights");
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUserInfo(){
        return ResponseEntity.ok("You have the user rights");
    }
}
