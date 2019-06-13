package com.fivem.alderalife;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Pattern;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        AlderalifeApplication.class,
        Jsr310JpaConverters.class
})
public class AlderalifeApplication {


    @Value("${app.location:classpath:/META-INF/resources}") private String sourceLocation;
    @Bean
    public ResourceHttpRequestHandler resourceHttpRequestHandler() throws MalformedURLException {
        final Pattern HTML_INDEX = Pattern.compile("^((index|patient|worklist|report)?(/.*)?)?$");
        ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler() {
            @Override
            protected String processPath(String path) {
                String processedPath = super.processPath(path);
                if (HTML_INDEX.matcher(processedPath).matches()) {
                    return "/index.html";
                }
                return processedPath;
            }
        };
        ArrayList<Resource> locations = new ArrayList<>();
        locations.add(new UrlResource(sourceLocation));
        requestHandler.setLocations(locations);
        return requestHandler;
    }

    @Bean
    public SimpleUrlHandlerMapping sampleServletMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MAX_VALUE - 2);
        Properties urlProperties = new Properties();
        urlProperties.put("/**", "resourceHttpRequestHandler");
        mapping.setMappings(urlProperties);
        return mapping;
    }
    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(AlderalifeApplication.class, args);
    }

}
