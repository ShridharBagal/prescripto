package com.shridhar.prescripto.backend.controller;

import com.shridhar.prescripto.backend.model.User;
import com.shridhar.prescripto.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        // Avoid duplicate phone numbers
        if (userRepo.findByPhone(user.getPhone()).isPresent()) {
            return ResponseEntity.badRequest().body("User with this phone already exists.");
        }

        userRepo.save(user);
        return ResponseEntity.ok("User created successfully.");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
