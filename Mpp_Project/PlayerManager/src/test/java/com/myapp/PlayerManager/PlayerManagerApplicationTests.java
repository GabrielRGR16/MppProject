package com.myapp.PlayerManager;

import com.myapp.PlayerManager.test.PlayerServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlayerManagerApplicationTests {

	@Test
	void contextLoads() {
		// Ensures the application context loads correctly
	}

	@Test
	void runPlayerServiceTests() throws Exception {

		PlayerServiceTest playerServiceTest = new PlayerServiceTest();


		playerServiceTest.setUp();
		playerServiceTest.testGetAllPlayers();
		playerServiceTest.testGetPlayerById_Found();
		playerServiceTest.testGetPlayerById_NotFound();
		playerServiceTest.testAddPlayer();
		playerServiceTest.testUpdatePlayer_Success();
		playerServiceTest.testUpdatePlayer_NotFound();
		playerServiceTest.testDeletePlayer_Success();
		playerServiceTest.testDeletePlayer_NotFound();
	}
}
