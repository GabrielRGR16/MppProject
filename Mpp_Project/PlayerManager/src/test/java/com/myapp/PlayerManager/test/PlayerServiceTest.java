package com.myapp.PlayerManager.test;

import com.myapp.PlayerManager.model.Player;
import com.myapp.PlayerManager.repository.PlayerRepository;
import com.myapp.PlayerManager.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    @Mock
    public PlayerRepository playerRepository;

    @InjectMocks
    public PlayerService playerService;

    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        player1 = new Player();
        player1.setId(1L);
        player1.setName("Lionel Messi");
        player1.setPosition("Forward");
        player1.setAge(36);
        player1.setTeam("Inter Miami");
        player1.setPhoto("messi.jpg");

        player2 = new Player();
        player2.setId(2L);
        player2.setName("Cristiano Ronaldo");
        player2.setPosition("Forward");
        player2.setAge(39);
        player2.setTeam("Al Nassr");
        player2.setPhoto("ronaldo.jpg");
    }

    @Test
    public void testGetAllPlayers() {
        when(playerRepository.findAll()).thenReturn(Arrays.asList(player1, player2));

        var players = playerService.getAllPlayers();

        assertEquals(2, players.size());
        assertEquals("Lionel Messi", players.get(0).getName());
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    public void testGetPlayerById_Found() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player1));

        Player foundPlayer = playerService.getPlayerById(1L);

        assertNotNull(foundPlayer);
        assertEquals("Lionel Messi", foundPlayer.getName());
    }

    @Test
    public void testGetPlayerById_NotFound() {
        when(playerRepository.findById(3L)).thenReturn(Optional.empty());

        Player foundPlayer = playerService.getPlayerById(3L);

        assertNull(foundPlayer);
    }

    @Test
    public void testAddPlayer() {
        when(playerRepository.save(player1)).thenReturn(player1);

        Player savedPlayer = playerService.addPlayer(player1);

        assertNotNull(savedPlayer);
        assertEquals("Lionel Messi", savedPlayer.getName());
        verify(playerRepository, times(1)).save(player1);
    }

    @Test
    public void testUpdatePlayer_Success() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player1));
        when(playerRepository.save(any(Player.class))).thenReturn(player1);

        Player updatedPlayer = new Player();
        updatedPlayer.setName("Leo Messi");
        updatedPlayer.setPosition("Forward");
        updatedPlayer.setAge(37);
        updatedPlayer.setTeam("Inter Miami");
        updatedPlayer.setPhoto("leo.jpg");

        Player result = playerService.updatePlayer(1L, updatedPlayer);

        assertNotNull(result);
        assertEquals("Leo Messi", result.getName());
        assertEquals(37, result.getAge());
    }

    @Test
    public void testUpdatePlayer_NotFound() {
        when(playerRepository.findById(3L)).thenReturn(Optional.empty());

        Player updatedPlayer = new Player();
        updatedPlayer.setName("Neymar Jr.");

        Player result = playerService.updatePlayer(3L, updatedPlayer);

        assertNull(result);
    }

    @Test
    public void testDeletePlayer_Success() {
        when(playerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(playerRepository).deleteById(1L);

        boolean result = playerService.deletePlayer(1L);

        assertTrue(result);
        verify(playerRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePlayer_NotFound() {
        when(playerRepository.existsById(3L)).thenReturn(false);

        boolean result = playerService.deletePlayer(3L);

        assertFalse(result);
        verify(playerRepository, never()).deleteById(3L);
    }
}
