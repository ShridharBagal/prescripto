package com.shridhar.prescripto.backend.controller;

import com.shridhar.prescripto.backend.model.User;
import com.shridhar.prescripto.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping
    public String createUser(@RequestBody User user) {
        userRepository.save(user);
        return "User created";
    }

    @GetMapping("/phone/{phone}")
    public User getUserByPhone(@PathVariable String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }
}
