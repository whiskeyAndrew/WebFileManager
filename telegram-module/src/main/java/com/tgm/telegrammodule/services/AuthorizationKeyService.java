package com.tgm.telegrammodule.services;

import com.tgm.telegrammodule.entity.AuthorizationKey;
import com.tgm.telegrammodule.enums.Role;
import com.tgm.telegrammodule.repos.AuthorizationKeyRepo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j
public class AuthorizationKeyService {
    @Getter(AccessLevel.NONE)
    private final AuthorizationKeyRepo keyRepo;
    private AuthorizationKey userKey;
    private AuthorizationKey adminKey;

    @PostConstruct
    void init() {
        generateKeys();
        log.info("UserKey: " + userKey.getKey());
        log.info("AdminKey: " + adminKey.getKey());
    }

    private void generateKeys() {
        String currentUserKey = RandomStringUtils.randomAlphanumeric(20);
        String currentAdminKey = RandomStringUtils.randomAlphanumeric(20);

        while (currentAdminKey.equals(currentUserKey)) {
            currentAdminKey = RandomStringUtils.randomAlphanumeric(20);
        }

        userKey = AuthorizationKey.builder()
                .key(currentUserKey)
                .role(Role.USER)
                .generationTime(Instant.now())
                .build();

        adminKey = AuthorizationKey.builder()
                .key(currentAdminKey)
                .role(Role.ADMIN)
                .generationTime(Instant.now())
                .build();

        keyRepo.save(userKey);
        keyRepo.save(adminKey);
    }

    public Role isKeyValid(String key) {
        if (userKey.getKey().equals(key)) {
            return Role.USER;
        } else if (adminKey.getKey().equals(key)) {
            return Role.ADMIN;
        } else {
            return Role.NOT_VALID;
        }
    }

    public AuthorizationKey getKeyByRole(Role role) {
        if (role.equals(Role.USER)) {
            return userKey;
        } else if (role.equals(Role.ADMIN)) {
            return adminKey;
        } else {
            return null;
        }
    }
}
