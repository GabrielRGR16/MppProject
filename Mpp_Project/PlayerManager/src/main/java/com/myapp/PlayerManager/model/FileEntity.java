package com.myapp.PlayerManager.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "file_content")
    @JdbcTypeCode(SqlTypes.BINARY) // Forces BYTEA in PostgreSQL
    private byte[] fileContent;

    @Column(name = "content_type") // New field
    private String contentType;
}