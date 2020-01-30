package com.umurcanemre.mancala.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.umurcanemre.mancala.entity.GameTest;
import com.umurcanemre.mancala.helper.StaticTestCase;
import com.umurcanemre.mancala.service.GameService;

public class GameServiceImplTest {
	private GameService gameService = new GameServiceImpl();
	private static final String PLAYERPREFIX = "player";
	private static final String P1 = PLAYERPREFIX+1;
	private static final String P2 = PLAYERPREFIX+2;

	@BeforeEach
	public void initTest() {
		gameService.clearActiveGames();
		gameService.clearFinishedGames();
	}
	
	@Test
	public void isGameStartedTest() {
		assertEquals(0, gameService.activeGameCount());
		final int gamesToInit = 10;
		
		for(int i=0; i<gamesToInit; i++) {
			gameService.initiateGame(Long.valueOf(i), PLAYERPREFIX+(i*2), PLAYERPREFIX+((i*2)+1));
			assertEquals(i+1, gameService.activeGameCount());
		}
	}
	
	@Test
	public void initiateGameTest() {
		assertEquals(0, gameService.activeGameCount());

		initiateGameTestForId(Long.MAX_VALUE);
		initiateGameTestForId(Long.MIN_VALUE);
		initiateGameTestForId(-3L);
		initiateGameTestForId(0L);
		initiateGameTestForId(125L);
		initiateGameTestForId(123092768230965L);
	}
	
	private void initiateGameTestForId(final long id) {
		gameService.initiateGame(id, P1, P2);
		
		//Since we cannot get Game object, we're asserting the display result against Game tests
		GameTest.assertDisplayResult(gameService.displayGame(id));
	}
	
	@Test
	public void displayGameTest() {
		assertEquals(0, gameService.activeGameCount());
		gameService.initiateGame(0L, P1, P2);
		GameTest.assertDisplayResult(gameService.displayGame(0L));
	}
	
	@Test
	public void displayGameTest_nonExistentGame() {
		assertEquals(0, gameService.activeGameCount());
		// not initiating game -> gameService.initiateGame(0L, PLAYERPREFIX+1, PLAYERPREFIX+2);
		assertThrows(IllegalStateException.class, ()-> GameTest.assertDisplayResult(gameService.displayGame(0L))); 
	}
	
	@Test
	public void makeMoveTest() {
		long gameId = 0l;
		assertEquals(0, gameService.activeGameCount());
		gameService.initiateGame(gameId, P1, P2);
		
		String initialDisplayResult = gameService.displayGame(gameId);
		gameService.makeMove(gameId, P1, 3);
		assertNotEquals(initialDisplayResult, gameService.displayGame(gameId));
	}
	
	@Test
	public void makeMoveTest_nonExistentGame() {
		assertEquals(0, gameService.activeGameCount());
		// not initiating game -> gameService.initiateGame(0L, P1, P2);
		assertThrows(IllegalArgumentException.class, ()-> gameService.makeMove(0L, P1, 3));
	}
	
	@Test
	public void makeMoveTest_finishedGame() {
		assertEquals(0, gameService.activeGameCount());
		gameService.initiateGame(0L, P1, P2);
		
		for(int[] move : StaticTestCase.realGameMoves()) {
			gameService.makeMove(0L, move[0] == 1? P1 :P2, move[1]);
		}
		assertEquals(1, gameService.finishedGameCount());
		assertEquals(0, gameService.activeGameCount());//Game starts and plays as expected
		
		//after game finished, make move should't be allowed
		assertThrows(IllegalArgumentException.class,()-> gameService.makeMove(0L, P1, 3));//random, doesn't matter
	}
	
	@Test
	public void activeGameCountTest() {
		for (int i = 0; i < 10; i++) {
			assertEquals(i, gameService.activeGameCount());
			gameService.initiateGame(Long.valueOf(i), PLAYERPREFIX+(i*2), PLAYERPREFIX+((i*2)+1));
			assertEquals(i+1, gameService.activeGameCount());			
		}
	}
	
	@Test
	public void finishedGameCount() {
		assertEquals(0, gameService.finishedGameCount());
	}

	@Test
	public void finishedGameCount_present() {
		assertEquals(0, gameService.activeGameCount());
		gameService.initiateGame(0L, P1, P2);
		
		for(int[] move : StaticTestCase.realGameMoves()) {
			gameService.makeMove(0L, move[0] == 1? P1 :P2, move[1]);
		}
		
		assertEquals(1, gameService.finishedGameCount());
	}
}
