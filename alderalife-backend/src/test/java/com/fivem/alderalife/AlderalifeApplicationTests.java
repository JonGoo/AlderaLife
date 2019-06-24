package com.fivem.alderalife;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlderalifeApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    /**
     * Test on mapping for the patient creation
     * @throws URISyntaxException Exception if the URI is not working correctly
     */
    @Test
    public void testRegister()throws URISyntaxException {
        final String baseUrl = "http://localhost:"+randomServerPort+"/api/auth/signup";
        URI uri= new URI(baseUrl);
        String sending = "{"+
                "\"username\": \"Goostry\"," +
                "\"email\": \"jonathan.goossens@hotmail.com\","+
                "\"password\": \"springtest\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("CONTENT-TYPE", "application/json");

        HttpEntity<String> request = new HttpEntity<>(sending, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        //Verify request succeed
        Assert.assertEquals(201, result.getStatusCode().value());

    }
}

