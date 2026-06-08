package com.epw.skillswap.config;

import com.epw.skillswap.entity.User;
import com.epw.skillswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.first-name}")
    private String adminFirstName;

    @Value("${app.admin.last-name}")
    private String adminLastName;

    private static final String ADMIN_PASSWORD = "Admin1234!";

    @Bean
    CommandLineRunner initData(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            var existingAdmin = repo.findByEmail(adminEmail);

            if (existingAdmin.isEmpty()) {
                User admin = User.builder()
                        .firstName(adminFirstName)
                        .lastName(adminLastName)
                        .email(adminEmail)
                        .password(encoder.encode(ADMIN_PASSWORD))
                        .currentCreditBalance(1000.0)
                        .build();

                repo.save(admin);
            } else {
                User admin = existingAdmin.get();
                admin.setPassword(encoder.encode(ADMIN_PASSWORD));
                repo.save(admin);
            }
        };
    }
}
