package com.fivem.alderalife.controller;

import com.fivem.alderalife.config.MailConfig;
import com.fivem.alderalife.model.User;
import com.fivem.alderalife.model.Whitelist;
import com.fivem.alderalife.repository.UserRepository;
import com.fivem.alderalife.repository.WhitelistRepository;
import com.fivem.alderalife.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/whitelist")
public class WhitelistController {

    @Autowired
    WhitelistRepository whitelistRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    private static final Logger LOGGER = LoggerFactory.getLogger(WhitelistController.class);

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createWhitelist (@RequestBody Whitelist whitelist){


        Whitelist newWhitelist = whitelistRepository.save(whitelist);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newWhitelist.getId()).toUri();
        LOGGER.info(location.toString());
        return ResponseEntity.created(location).build();
    }


    /**
     * Method that creates a mapping to get all the whitelists into the database
     * @return All the whitelists that are present into the database
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<Whitelist> getAllPatients() {
        // This returns a JSON
        LOGGER.info("Show the list of all whitelist in JSON format");
        return whitelistRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/canWhitelist/{id}")
    public @ResponseBody ResponseEntity<?> canAskWhitelist(@PathVariable Long id) {

        User user = userRepository.findById(id).get();
        Long count = whitelistRepository.countByUser(user);

        if (count == 0) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else if (count == 1) {
            Whitelist whitelist = whitelistRepository.findByUser(user).get();
            if (whitelist.isAccepted()) {
                return new ResponseEntity<>("Votre demande a été acceptée, allez vérifier vos mails !", HttpStatus.OK);
            } else if (whitelist.isRefused()) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Votre demande est en cours de traitement, veuillez patienter", HttpStatus.OK);
            }
        } else if (count == 2) {
            Whitelist whitelist = whitelistRepository.findLastByUser(user).get(1);
            if (whitelist.isAccepted()) {
                return new ResponseEntity<>("Votre demande a été acceptée, allez vérifier vos mails !", HttpStatus.OK);
            } else if (whitelist.isRefused()) {
                return new ResponseEntity<>("Vous avez atteint votre nombre d'essai maximum ! Désolé mais vous êtes recalé !", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Votre demande est en cours de traitement, veuillez patienter", HttpStatus.OK);
            }
        }
        else {
            return new ResponseEntity<>("Vous avez atteint votre nombre d'essai maximum ! Désolé mais vous êtes recalé !", HttpStatus.OK);
        }


    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/accept/{id}")
    public @ResponseBody ResponseEntity<?> acceptWhitelist(@PathVariable Long id) {
        Whitelist whitelist = whitelistRepository.findById(id).get();
        User user = userRepository.findById(whitelist.getUser().getId()).get();

        whitelist.setAccepted(true);

        whitelistRepository.save(whitelist);

        emailSenderService = new EmailSenderService(new MailConfig().getJavaMailSender());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Demande de whitelist acceptée");
        mailMessage.setFrom("alderalifecontact@gmail.com");
        mailMessage.setText("Félicitation, votre candidature a été acceptée ! Il ne vous reste plus qu'une étape à faire avant d'être whitelist.\n" +
                    "Rendez-vous sur le discord: https://discord.gg/xYVQW5b. Des canaux sont disponible pour votre entretien.\n\n" +
                    "Bon jeu à vous et encore félicitation,\n" +
                    "Le staff d'Aldera-Life");


        emailSenderService.sendEmail(mailMessage);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/refuse/{id}")
    public @ResponseBody ResponseEntity<?> refuseWhitelist(@PathVariable Long id) {
        Whitelist whitelist = whitelistRepository.findById(id).get();
        User user = userRepository.findById(whitelist.getUser().getId()).get();

        Long count = whitelistRepository.countByUser(user);
        LOGGER.info(count.toString());
        whitelist.setRefused(true);

        emailSenderService = new EmailSenderService(new MailConfig().getJavaMailSender());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Demande de whitelist refusée");
        mailMessage.setFrom("alderalifecontact@gmail.com");

        whitelistRepository.save(whitelist);

        if (count == 1) {
            mailMessage.setText("Malheureusement, votre candidature a été refusée ! Il vous reste une tentative pour prétendre être whitelist...\n" +
                    "Rendez-vous sur le discord: https://discord.gg/xYVQW5b. Vous pourrez y trouvez des conseils pour a réaliser au mieux.\n\n" +
                    "Bonne chance à vous et courage,\n" +
                    "Le staff d'Aldera-Life");
        } else if(count >=2) {
            mailMessage.setText("Malheureusement, votre candidature a été refusée ! Toutes vos tentatives ont été épuisée...\n" +
                    "Nous vous souhaitons une bonne continuation et nous excusons pour ce refus.\n\n" +
                    "Bonne chance à vous,\n" +
                    "Le staff d'Aldera-Life");
        }
        emailSenderService.sendEmail(mailMessage);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }



}
