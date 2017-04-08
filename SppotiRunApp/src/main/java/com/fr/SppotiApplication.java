package com.fr;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by djenanewail on 1/22/17.
 */
@SpringBootApplication
@PropertySources({
        @PropertySource(value = "classpath:pagination.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
        @PropertySource(value = "classpath:headerConfig.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
        @PropertySource(value = "classpath:email.properties", ignoreResourceNotFound = true, encoding = "UTF-8")
})
public class SppotiApplication implements CommandLineRunner{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SppotiApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... strings) throws Exception {

    }
}