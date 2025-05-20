package com.myapp.PlayerManager.repository;

import com.myapp.PlayerManager.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}