package com.tgm.telegrammodule.repos;

import com.tgm.telegrammodule.entity.AuthorizationKey;
import org.hibernate.mapping.UniqueKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationKeyRepo extends JpaRepository<AuthorizationKey, Long> {

}
