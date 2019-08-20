package com.fivem.alderalife.controller;

import com.fivem.alderalife.model.Role;
import com.fivem.alderalife.model.User;
import com.fivem.alderalife.repository.UserRepository;
import org.apache.catalina.util.ResourceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(WhitelistController.class);

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Iterable<User> getAllPatients() {
        // This returns a JSON
        LOGGER.info("Show the list of all users in JSON format");
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/setrole/{id}")
    public @ResponseBody ResponseEntity<?> setPatientRole(@RequestBody Role role, @PathVariable Long id) {
        // This returns a JSON
        User user = userRepository.findById(id).get();
        Set<Role> newRole = new ResourceSet<>();
        newRole.add(role);
        user.setRoles(newRole);

        userRepository.save(user);
        LOGGER.info("Set the role of an user");

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
