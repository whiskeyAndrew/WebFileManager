package com.tgm.telegrammodule.services;

import com.tgm.telegrammodule.entity.TgUser;
import com.tgm.telegrammodule.enums.Role;
import com.tgm.telegrammodule.repos.TgUserRepo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorizationService {

    private final TgUserRepo tgUserRepo;
    private final AuthorizationKeyService keysService;


    @PostConstruct
    public void init() {

    }

    @Cacheable(value = "authorizedUsers")
    public boolean isUserAuthorized(Long id) {
        Optional<TgUser> checkableUser = tgUserRepo.findTgUserById(id);
        if (checkableUser.isEmpty()) {
            return false;
        }

        return checkableUser.get().getIsActive();
    }
    @CachePut(value = "authorizedUsers", key="#id")
    public boolean authorizeUser(Long id, Role role) {
        if (isUserAuthorized(id)) {
            return false;
        }

        Optional<TgUser> user = tgUserRepo.findTgUserById(id);
        if(user.isPresent()){
            user.get().setIsActive(true);
            user.get().setKey(keysService.getKeyByRole(role));
            tgUserRepo.save(user.get());
            return true;
        }

        TgUser newUser = TgUser.builder()
                .id(id)
                .key(keysService.getKeyByRole(role))
                .role(role)
                .isActive(true)
                .isBanned(false)
                .authorizedAt(Instant.now())
                .build();
        tgUserRepo.save(newUser);
        return true;
    }

    @CacheEvict(value = "authorizedUsers")
    public void deauthorizeUser(Long id){
        Optional<TgUser> user = tgUserRepo.findTgUserById(id);
        if(user.isEmpty()){
            return;
        }
        user.get().setIsActive(false);
        tgUserRepo.save(user.get());

    }


}
