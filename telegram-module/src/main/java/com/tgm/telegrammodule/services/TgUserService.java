package com.tgm.telegrammodule.services;

import com.tgm.telegrammodule.entity.TgUser;
import com.tgm.telegrammodule.repos.TgUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TgUserService {
    private final TgUserRepo userRepo;

    public List<TgUser> getAuthorizedUsers(){
        return userRepo.getAllByIsActiveTrueAndIsBannedFalse();
    }
}
