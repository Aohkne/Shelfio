package com.huukhoa.backend.config;

import com.huukhoa.backend.entity.Role;
import com.huukhoa.backend.entity.User;
import com.huukhoa.backend.repository.RoleRepository;
import com.huukhoa.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            seedRole(roleRepository, com.huukhoa.backend.enums.Role.ADMIN.name(), "Administrator");
            seedRole(roleRepository, com.huukhoa.backend.enums.Role.LIBRARIAN.name(), "Library staff");
            seedRole(roleRepository, com.huukhoa.backend.enums.Role.MEMBER.name(), "Library member");

            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.findById(com.huukhoa.backend.enums.Role.ADMIN.name())
                        .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("Login123@"))
                        .email("admin@gmail.com")
                        .fullname("Administrator")
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(user);
                log.warn("Admin user created with role ADMIN");
            }
        };
    }

    private void seedRole(RoleRepository roleRepository, String name, String description) {
        if (!roleRepository.existsById(name)) {
            roleRepository.save(Role.builder()
                    .name(name)
                    .description(description)
                    .build());
            log.info("Role {} seeded", name);
        }
    }
}
