package com.umurcanemre.mancala.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {
	private Game game;
	private String p1Name = "p1";
	private String p2Name = "p2";

	@BeforeEach
	public void initGame() {
		game = new Game(p1Name, p2Name);
	}

	@Test
	public void constructorTest() {
		// game instance created in @BeforeEach

		assertNotNull(game);
		assertEquals(2, game.getPlayers().size());
		assertEquals(2, game.getBoard().size());
		assertEquals(p1Name, game.getTurn());
		assertTrue(game.isActiveGame());
	}

	@Test
	public void constructorTest_blankName() {
		assertThrows(IllegalArgumentException.class, () -> {
			String p1Name = null;
			String p2Name = "p2";
			new Game(p1Name, p2Name);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			String p1Name = "p1";
			String p2Name = null;
			new Game(p1Name, p2Name);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			String p1Name = "p1";
			String p2Name = "   ";
			new Game(p1Name, p2Name);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			String p1Name = "p1";
			String p2Name = "p1";
			new Game(p1Name, p2Name);
		});
	}

	@Test
	public void displayBoardTest() {
		String displayedResult = game.displayBoard();
		assertDisplayResult(displayedResult);
	}

	public static void assertDisplayResult(String displayedResult) {
		// clear HTML tags and game-static texts
		displayedResult = displayedResult.replaceAll("<[^>]*>", "");
		displayedResult = displayedResult.substring(0, displayedResult.indexOf("Pit"));

		assertEquals(13, displayedResult.split(String.valueOf(Game.getISC())).length); // 12 ISCs split the text to 13
																						// pieces
		assertEquals(2, displayedResult.split("0").length); // 2 0s split the text to 2 pieces(second one at the end)
	}

	@Test
	public void makeMoveTest_concuquerScenario() {
		// manipulate the table to conquer conditions
		game.getBoard().put(p1Name, Arrays.asList(Game.getISC(), Game.getISC(), Game.getISC(), 1, 0, Game.getISC(), 0));

		game.makeMove(p1Name, 3);// conquer

		// 7 stone pieces collected to p1's mancala
		assertEquals(1 + Game.getISC(), game.getBoard().get(p1Name).get(Game.getMancalaPosition() - 1));

		// for p1, except for conquered cell and moved cell, all cells should be as it
		// was
		for (int i = 0; i < Game.getMancalaPosition() - 1; i++) {
			if (i != 3 && i != 4) {
				assertEquals(Game.getISC(), game.getBoard().get(p1Name).get(i));
			} else {// conquered cell and moved cell should have 0 stones
				assertEquals(0, game.getBoard().get(p1Name).get(i));
			}
		}

		// for p2, except for conquered cell, all cells should be as it was
		for (int i = 0; i < Game.getMancalaPosition() - 1; i++) {
			if (i != 1) {
				assertEquals(Game.getISC(), game.getBoard().get(p2Name).get(i));
			} else {// conquered cell should have 0 stones
				assertEquals(0, game.getBoard().get(p2Name).get(i));
			}
		}
	}

	@Test
	public void makeMoveTest_mancalaScenario() {
		assertEquals(p1Name, game.getTurn());
		game.makeMove(p1Name, Game.getISC());// if initial stone count changes, test should still pass, that's why
												// played cell is ISC
		assertEquals(p1Name, game.getTurn());
	}

	@Test // testing the scenario where a pit that ha +12 stones is distributed
	public void makeMoveTest_bigPitDistributeScenario() {
		int cellPlayed = 1;
		// manipulate the table to crowded pit conditions
		// Note, when chosen 13 instead of 26, also a conquer scenario appears, thats
		// why to ensure 2 tour therefore stopping conquer from happening 26 is chosen
		game.getBoard().put(p1Name,
				Arrays.asList(Game.getISC(), Game.getISC(), Game.getISC(), Game.getISC(), Game.getISC(), 26, 0));

		game.makeMove(p1Name, cellPlayed);// distribute/sow

		// for p1, except for played cell and mancala, all cells should have 2 more than
		// ISC
		// both mancala and played cell should have 2
		for (int i = 0; i < Game.getMancalaPosition(); i++) {
			if (i != ((Game.getMancalaPosition() - 1)) - cellPlayed && i != ((Game.getMancalaPosition() - 1))) {
				assertEquals(Game.getISC() + 2, game.getBoard().get(p1Name).get(i));
			} else {// conquered cell and moved cell should have 0 stones
				assertEquals(2, game.getBoard().get(p1Name).get(i));
			}
		}

		// for p2, mancala should have 0, all other cells should have one more than ISC
		for (int i = 0; i < Game.getMancalaPosition() - 1; i++) {
			if (i != Game.getMancalaPosition() - 1) {
				assertEquals(Game.getISC() + 2, game.getBoard().get(p2Name).get(i));
			} else {// mancala cell should have 0 stones
				assertEquals(0, game.getBoard().get(p2Name).get(i));
			}
		}
	}

	@Test
	public void makeMoveTest_wrongTurn() {
		assertThrows(IllegalStateException.class, () -> {
			game.makeMove(p2Name, 1);
		});
	}

	@Test
	public void makeMoveTest_InvalidMove() {
		assertThrows(IllegalArgumentException.class, () -> {
			game.makeMove(p1Name, 7);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			game.makeMove(p1Name, 0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			game.makeMove(p1Name, 7241);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			game.makeMove(p1Name, -24);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			game.makeMove(p1Name, Integer.MAX_VALUE);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			game.makeMove(p1Name, Integer.MIN_VALUE);
		});

	}

	@Test
	public void makeMoveTest_emptyCell() {
		game.makeMove(p1Name, 1);
		game.makeMove(p2Name, 1);

		// on the array that holds the values of player one,
		// 5th is what corresponds to 1st cell in player's perspective
		assertEquals(0, game.getBoard().get(p1Name).get(5)); // check 1st cell of player 1 is really empty

		assertThrows(IllegalStateException.class, () -> {
			game.makeMove(p1Name, 1);// try to move it again
		});
	}
}
