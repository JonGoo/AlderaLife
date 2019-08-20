package com.fivem.alderalife.controller;

import com.fivem.alderalife.model.Role;
import com.fivem.alderalife.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    RoleRepository roleRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(WhitelistController.class);

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Iterable<Role> getAllPatients() {
        // This returns a JSON
        LOGGER.info("Show the list of all whitelist in JSON format");
        return roleRepository.findAll();
    }
}
