package com.umurcanemre.mancala.service;

public interface GameService {
	boolean isGameStarted(long gameId);
	int activeGameCount();
	int finishedGameCount();
	void initiateGame(long gameId, String player1, String player2);
	String displayGame(long gameId);
	void makeMove(long gameId,String player, int move);
	void clearActiveGames();
	void clearFinishedGames();
}
