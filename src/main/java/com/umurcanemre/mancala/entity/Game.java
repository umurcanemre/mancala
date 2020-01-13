package com.umurcanemre.mancala.entity;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Game {
	private static final int MANCALA_POSITION = 7;
	private static final int ISC =6;// Initial stone/piece count
	private List<String> players;
	private Map<String, List<Integer>> board;
	private String turn;
	private boolean activeGame = true;

	public Game(final String player1, final String player2) {
		if(StringUtils.isBlank(player1) || StringUtils.isBlank(player2) || player1.equals(player2)) {
			throw new IllegalArgumentException("Player names must be nonblank and distinct");
		}
		
		players = Arrays.asList(player1, player2);
		board = new LinkedHashMap<>();

		// first 6 members of the list are regular pits of the board, 7th is mancala(big
		// pit)
		board.put(player1, Arrays.asList(ISC, ISC, ISC, ISC, ISC, ISC, 0));
		board.put(player2, Arrays.asList(ISC, ISC, ISC, ISC, ISC, ISC, 0));
		turn = player1; // Game starts with player 1
	}

	/**
	 * get the initial stone count of the game
	 *
	 * @return initial stone count for the game
	 */
	public static int getISC() {
		return ISC;
	}
	/**
	 * get the mancala position for game
	 *
	 * @return mancala position
	 */
	public static int getMancalaPosition() {
		return MANCALA_POSITION;
	}

	public void makeMove(final String player, int cell) {
		if(!isActiveGame()) {
			throw new IllegalStateException("This game has ended, you can not make any more moves");
		}		
		if (!turn.equals(player)) {
			throw new IllegalStateException("Other player's turn");
		}
		if(cell< 1 || cell >= MANCALA_POSITION) {
			throw new IllegalArgumentException("Invalid move");
		}
		cell = normalizeCellNo(player, cell);// Player 1's cell numbers are inverted
		String opponent = players.get(1 - players.indexOf(player));
		if (board.get(player).get(cell).equals(0)) {
			throw new IllegalStateException("Cannot play empty cell");
		}

		int stonesAtHand = pickChosenPitStones(player, cell);

		Map.Entry<Integer,Boolean> sowResult = sow(stonesAtHand, cell, player, opponent);
		
		int lastEditedPosition = sowResult.getKey();// last PostIncrenment neutralized
		boolean sowToOpponent = sowResult.getValue();
		
		if (!sowToOpponent && lastEditedPosition != (MANCALA_POSITION - 1)
				&& board.get(player).get(lastEditedPosition).equals(1)) {
			capture(player, opponent, lastEditedPosition);
		}
		
		if (isGameOver(player)) {
			collectOpponentStones(opponent);
			turn = null;
		}
		else if(lastEditedPosition < MANCALA_POSITION - 1) {
			turn = opponent;
		}
	}
	
	private Map.Entry<Integer,Boolean> sow(int stonesAtHand, int cell, String player,String opponent){
		int lastEditedPosition= cell + 1;
		for (lastEditedPosition = cell + 1; lastEditedPosition < MANCALA_POSITION
				&& stonesAtHand > 0; lastEditedPosition++) {
			board.get(player).set(lastEditedPosition, board.get(player).get(lastEditedPosition) + 1);
			stonesAtHand--;
		}

		boolean sowToOpponent = stonesAtHand > 0;
		while (stonesAtHand > 0) {
			if (sowToOpponent) {
				for (lastEditedPosition = 0; lastEditedPosition < MANCALA_POSITION - 1
						&& stonesAtHand > 0; lastEditedPosition++) {
					board.get(opponent).set(lastEditedPosition, board.get(opponent).get(lastEditedPosition) + 1);
					stonesAtHand--;
				}
				sowToOpponent = !(stonesAtHand > 0);
			} else {
				for (lastEditedPosition = 0; lastEditedPosition < MANCALA_POSITION
						&& stonesAtHand > 0; lastEditedPosition++) {
					board.get(player).set(lastEditedPosition, board.get(player).get(lastEditedPosition) + 1);
					stonesAtHand--;
				}
				sowToOpponent = stonesAtHand > 0;
			}
		}
		lastEditedPosition--;// last PostIncrenment neutralized
		
		return new AbstractMap.SimpleEntry<Integer,Boolean>(lastEditedPosition,sowToOpponent);
	}

	private Map.Entry<Integer,Boolean> sow_new(int stonesAtHand, int cell, String player){

		return new AbstractMap.SimpleEntry<Integer,Boolean>(0,false);
	}
	
	/**
	 * 
	 * @param startPoint
	 * @param stones
	 * @param player
	 * @return map entry, key is lastEditedPosition, value is remaining stone count
	 */
	private Map.Entry<Integer,Integer> sow(int startPoint, int stones, String player) {
		int lastEditedPosition;
		for (lastEditedPosition=startPoint; startPoint < MANCALA_POSITION && stones > 0; lastEditedPosition++) {
			board.get(player).set(lastEditedPosition, board.get(player).get(lastEditedPosition) + 1);
			stones--;
		}
		return new AbstractMap.SimpleEntry<Integer,Integer>(lastEditedPosition,stones);
	}
	
	private int pickChosenPitStones(final String player, final int cell) {
		int stones = board.get(player).get(cell);
		board.get(player).set(cell, 0);
		return stones;
	}

	private void collectOpponentStones(final String opponent) {
		int remainingStones = 0;
		for (int i = 0; i < MANCALA_POSITION; i++) {
			remainingStones += board.get(opponent).get(i);
			board.get(opponent).set(i, 0);
		}
		board.get(opponent).set(MANCALA_POSITION - 1, remainingStones);
	}

	private boolean isGameOver(final String player) {
		for (int i = 0; activeGame && i < MANCALA_POSITION - 1; i++) {
			if (board.get(player).get(i) > 0) {
				return false;
			}
		}
		activeGame = false;
		return true;
	}

	private void capture(String player, String opponent, int lastEditedPosition) {
		int capturedStones = 0;
		capturedStones += board.get(player).get(lastEditedPosition);
		board.get(player).set(lastEditedPosition, 0);
		capturedStones += board.get(opponent).get((MANCALA_POSITION - 1) - lastEditedPosition - 1);
		board.get(opponent).set((MANCALA_POSITION - 1) - lastEditedPosition - 1, 0);
		board.get(player).set(MANCALA_POSITION - 1, board.get(player).get(MANCALA_POSITION - 1) + capturedStones);
	}

	private int normalizeCellNo(final String player, int cell) {
		boolean isP1 = players.indexOf(player) == 0;
		if (isP1) {
			cell = MANCALA_POSITION - cell;
		}
		return cell - 1;// from this point on, cell position is in array convention (first cell's
						// address 0)
	}

	public String displayBoard() {
		StringBuilder builder = new StringBuilder();
		Iterator<List<Integer>> boardIterator = board.values().iterator();
		List<Integer> playersBoard = boardIterator.next();

		// PLAYER1's Board
		builder.append(String.format("<table><tr><th>Player "+this.getPlayers().get(0)+"</th><th>%2d</th>", playersBoard.get(playersBoard.size() - 1)));
		for (int i = 2; i <= playersBoard.size(); i++) {
			builder.append(String.format("<th>%2d</th>", playersBoard.get(playersBoard.size() - i)));
		}
		builder.append("</tr>");

		// PLAYER2's Board
		playersBoard = boardIterator.next();
		builder.append("<tr><th>Player "+this.getPlayers().get(1)+"</th><th></th>");
		for (int i = 0; i < playersBoard.size() - 1; i++) {
			builder.append(String.format("<th>%2d</th>", playersBoard.get(i)));
		}
		builder.append(String.format("<th>%2d</th>", playersBoard.get(playersBoard.size() - 1)));
		builder.append("</tr>");

		builder.append("<tr><th>Pit No</th><th>M</th>");
		for (int i = 1; i < 7; i++) {
			builder.append(String.format("<th>%2d</th>", i));
		}
		builder.append("<th>M</th></tr></table>");

		if (activeGame) {
			builder.append("<br>turn of player " + (players.indexOf(turn) + 1));
		} else {
			int p1Point = board.get(players.get(0)).get(MANCALA_POSITION - 1);
			int p2Point = board.get(players.get(1)).get(MANCALA_POSITION - 1);
			String winner;
			if (p1Point > p2Point) {
				winner = "Winner is P1";
			} else if (p1Point < p2Point) {
				winner = "Winner is P2";
			} else {
				winner = "Stalemate";
			}
			builder.append("<br>Game over." + winner);
		}
		return builder.toString();
	}
}
