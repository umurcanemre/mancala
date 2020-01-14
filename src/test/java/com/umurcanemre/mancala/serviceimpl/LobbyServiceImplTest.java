package com.umurcanemre.mancala.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.umurcanemre.mancala.service.GameService;

public class LobbyServiceImplTest {
	private GameService mockGameService = Mockito.mock(GameServiceImpl.class);
	private LobbyServiceImpl lobbyService = new LobbyServiceImpl(mockGameService);
	private static final String PLAYERPREFIX = "player";
	private static final String P1 = PLAYERPREFIX+1;
	private static final String P2 = PLAYERPREFIX+2;
	
	@BeforeEach
	public void initTest() {
		lobbyService.clearLobby();
	}

	@Test
	public void applyTest() {
		assertEquals(0,lobbyService.getLobbyCount());
		Long idP1 = lobbyService.apply(P1);
		assertEquals(1,lobbyService.getLobbyCount());
		assertNotNull(idP1);

		//users with same name should not be matched to avoid confusion
		Long idP1_2 = lobbyService.apply(P1);		
		assertEquals(2, lobbyService.getLobbyCount());
		assertNotEquals(idP1, idP1_2);
		
		//first different user 
		Long idP2 = lobbyService.apply(P2);		
		assertEquals(1, lobbyService.getLobbyCount());
		assertEquals(idP1, idP2);
	}
	
	@Test
	public void clearLobbyTest() {
		assertEquals(0,lobbyService.getLobbyCount());
		lobbyService.apply(P1);
		assertEquals(1,lobbyService.getLobbyCount());
		lobbyService.clearLobby();
		assertEquals(0,lobbyService.getLobbyCount());
	}
	
	@Test
	public void getLobbyCountTest() {
		clearLobbyTest();
	}
}
