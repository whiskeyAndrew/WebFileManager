package com.FileManager.FileManager.Entity;

import jakarta.persistence.*;

@Entity
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    Long id;
    String username;
}
