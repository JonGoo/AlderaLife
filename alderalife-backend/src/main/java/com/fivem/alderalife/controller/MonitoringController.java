package com.fivem.alderalife.controller;

import com.fivem.alderalife.model.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhitelistController.class);

    boolean isWindows = System.getProperty("os.name")
            .toLowerCase().startsWith("windows");

    @RequestMapping(method = RequestMethod.GET, path = "/getServers/")
    public @ResponseBody List<String> getServerList() throws IOException {
        // This returns a list of String
        Process process;
        if (isWindows) {
            LOGGER.info("Windows");
            process = Runtime.getRuntime()
                    .exec(String.format("cmd.exe /c dir g:\\drive"));
        } else {
            LOGGER.info("linux");
            process = Runtime.getRuntime()
                    .exec("sudo ls ../fivem/servers/");
        }
        List<String> output = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.add(line);
        }

        return output;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/isOnline/")
    public ResponseEntity<?> getOnlineServer(@RequestBody String name) throws IOException {
        Process process;
        String cmd = "sudo screen -ls";
        process = Runtime.getRuntime()
                .exec(cmd);
        String output = "empty";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(name)) {
                output = line;
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }
        LOGGER.info(output);
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        /*if (output == "") {
            return false;
        } else {
            return true;
        }*/

    }

    @RequestMapping(method = RequestMethod.POST, path = "/shutdown/")
    public boolean shutdownServer(@RequestBody String name) throws IOException {
        Process process;
        String cmd = "sudo screen -S " + name + " -X stuff ^C";
        List<String> output = new ArrayList<>();
        if(getOnlineServer(name).getStatusCode() == HttpStatus.OK) {
            while (getOnlineServer(name).getStatusCode() == HttpStatus.OK) {
                process = Runtime.getRuntime()
                        .exec(cmd);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/start/")
    public boolean startServer(@RequestBody String name) throws IOException {
        Process process;
        String cmd = "sudo bash ../fivem/servers/"+name+"/runserver.sh";
        List<String> output = new ArrayList<>();
        final boolean[] serverExist = {false};
        getServerList().forEach((k) -> {if(k.equals(name)) {
            serverExist[0] = true;
        }
        });
        if(getOnlineServer(name).getStatusCode() == HttpStatus.BAD_REQUEST && serverExist[0]) {
            process = Runtime.getRuntime()
                    .exec(cmd);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/command/")
    public boolean commandServer(@RequestBody Command commande) throws IOException {
        Process process;
        String cmd = "sudo screen -S " + commande.getServer() + " -X stuff \"" + commande.getCommand() + "^M\"";
        LOGGER.info(cmd);
        List<String> output = new ArrayList<>();
        process = Runtime.getRuntime()
                .exec(cmd);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.add(line);
        }
        return true;
    }
}
