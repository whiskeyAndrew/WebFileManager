package com.FileManager.FileManager.Entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    long fileId;
    String extension;

    @OneToOne
    @JoinColumn(name = "metadata_id")
    Metadata metadata;

    java.io.File file;
}
