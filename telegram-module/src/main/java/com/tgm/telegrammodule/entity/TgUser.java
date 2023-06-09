package com.tgm.telegrammodule.entity;

import com.tgm.telegrammodule.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TgUser {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "autho_key_id")
    private AuthorizationKey key;
    private Role role;
    private Boolean isActive;
    private Boolean isBanned;
    private Instant authorizedAt;
}
