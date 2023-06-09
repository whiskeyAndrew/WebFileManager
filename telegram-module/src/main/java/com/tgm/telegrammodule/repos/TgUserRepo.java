package com.tgm.telegrammodule.repos;

import com.tgm.telegrammodule.entity.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TgUserRepo extends JpaRepository<TgUser,Long> {
    Optional<TgUser> findTgUserById(Long id);
}
