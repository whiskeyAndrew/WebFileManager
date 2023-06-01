package com.FileManager.FileManager.Entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Metadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metadata_id")
    long metadataId;
    String name;

    @OneToOne
    @JoinColumn(name = "folder_id")
    Folder parrentFolder;
    Instant createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    Person createdBy;
}
