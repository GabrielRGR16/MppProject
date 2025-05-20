package com.myapp.PlayerManager.repository;

import com.myapp.PlayerManager.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {

}
