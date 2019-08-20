package com.fivem.alderalife.controller;

import com.fivem.alderalife.config.MailConfig;
import com.fivem.alderalife.exception.AppException;
import com.fivem.alderalife.model.ConfirmationToken;
import com.fivem.alderalife.model.Role;
import com.fivem.alderalife.model.RoleName;
import com.fivem.alderalife.model.User;
import com.fivem.alderalife.payload.ApiResponse;
import com.fivem.alderalife.payload.JwtAuthenticationResponse;
import com.fivem.alderalife.payload.LoginRequest;
import com.fivem.alderalife.payload.SignUpRequest;
import com.fivem.alderalife.repository.ConfirmationTokenRepository;
import com.fivem.alderalife.repository.RoleRepository;
import com.fivem.alderalife.repository.UserRepository;
import com.fivem.alderalife.security.JwtTokenProvider;
import com.fivem.alderalife.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Value( "${server.port}" )
    private String port;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest,
                                          ModelAndView modelAndView) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Le pseudo est déjà utilisé !"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "L'adresse mail est déjà utilisée !"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        emailSenderService = new EmailSenderService(new MailConfig().getJavaMailSender());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Finis ton inscription !");
        mailMessage.setFrom("alderalifecontact@gmail.com");
        if (port == "80") {
            mailMessage.setText("Pour confirmer votre inscription cliquez ici : "
                    +"http://localhost/api/auth/confirm?token="+confirmationToken.getConfirmationToken());
        } else {
            mailMessage.setText("Pour confirmer votre inscription cliquez ici : "
                    +"https://aldera-life.fr/confirm-email?token="+confirmationToken.getConfirmationToken());
        }


        emailSenderService.sendEmail(mailMessage);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @RequestMapping(value="/confirm", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        ResponseEntity response;

        if(token != null)
        {
            Optional<User> optUser = userRepository.findByEmail(token.getUser().getEmail());
            User user = optUser.get();
            user.setEnabled(true);
            userRepository.save(user);
            HttpHeaders headers = new HttpHeaders();
            headers.add("confirmation", "true");


            response = new ResponseEntity<>("Votre Email a bien été confirmé", headers, HttpStatus.OK);
        }
        else
        {
            HttpHeaders headers = new HttpHeaders();
            headers.add("confirmation", "false");


            response = new ResponseEntity<>("Le lien est invalide ou cassé", headers, HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
