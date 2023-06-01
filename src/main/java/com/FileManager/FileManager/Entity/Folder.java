package com.FileManager.FileManager.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.io.Serial;
import java.time.Instant;

@Entity
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    long folderId;

    @OneToOne
    @JoinColumn(name = "metadata_id")
    Metadata metadata;
}
