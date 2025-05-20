package com.myapp.PlayerManager.service;

import com.myapp.PlayerManager.model.Player;
import com.myapp.PlayerManager.repository.PlayerRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final List<SseEmitter> emitters = new ArrayList<>();

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {
        Optional<Player> player = playerRepository.findById(id);
        return player.orElse(null);
    }

    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, Player playerDetails) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            player.setName(playerDetails.getName());
            player.setPosition(playerDetails.getPosition());
            player.setAge(playerDetails.getAge());
            player.setTeam(playerDetails.getTeam());
            player.setPhoto(playerDetails.getPhoto());
            return playerRepository.save(player);
        }
        return null;
    }

    public boolean deletePlayer(Long id) {
        // Check if player exists
        boolean exists = playerRepository.existsById(id);
        if (!exists) {
            return false;
        }

        // Delete the player
        playerRepository.deleteById(id);
        return true;
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Keep connection open indefinitely
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

//    @PostConstruct
//    public void startPlayerGeneration() {
//        new Thread(() -> {
//            Random random = new Random();
//            String[] names = {"John Doe", "Jane Smith", "Mike Johnson", "Emily Davis"};
//            String[] teams = {"Team A", "Team B", "Team C"};
//            String[] positions = {"Forward", "Midfielder", "Defender"};
//
//            while (true) {
//                try {
//                    Player player = new Player();
//                    player.setName(names[random.nextInt(names.length)]);
//                    player.setTeam(teams[random.nextInt(teams.length)]);
//                    player.setPosition(positions[random.nextInt(positions.length)]);
//                    player.setAge(18 + random.nextInt(20));
//                    player.setPhoto("https://example.com/photo" + random.nextInt(100) + ".jpg");
//
//                    Player savedPlayer = playerRepository.save(player);
//                    broadcastPlayer(savedPlayer);
//
//                    Thread.sleep(50000); // Generate a new player every 5 min
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    private void broadcastPlayer(Player player) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("player-added").data(player));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }
        emitters.removeAll(deadEmitters);
    }
}
