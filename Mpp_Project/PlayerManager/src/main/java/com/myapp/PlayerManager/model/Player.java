package com.myapp.PlayerManager.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="player")

public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(
            name = "player_seq",
            sequenceName = "player_sequence",
            allocationSize = 1
    )
    private Long id;
    private String name;
    private String position;
    private int age;
    private String team;
    private String photo;
}

