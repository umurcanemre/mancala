package com.umurcanemre.mancala.serviceimpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.umurcanemre.mancala.entity.Game;
import com.umurcanemre.mancala.service.GameService;

@Service
public class GameServiceImpl implements GameService {
	private static final Map<Long,Game> games = new ConcurrentHashMap<>();
	private static final Map<Long,String> finishedGames = new ConcurrentHashMap<>();

	@Override
	public boolean isGameStarted(final long gameId) {
		return games.containsKey(gameId);
	}

	@Override
	public void initiateGame(long gameId, String player1, String player2) {
		if(isGameStarted(gameId)) {//on the off chance two gamers manage to hit the same id
			throw new IllegalStateException("Game id collision, game will be discarded. Please try again.");
		}
		
		games.put(gameId, new Game(player1, player2));
	}

	@Override
	public String displayGame(long gameId) {
		Game game = games.get(gameId);		
		if(game != null) {
			return game.displayBoard();
		}
		
		String finishedGameResult = finishedGames.get(gameId);
		if(finishedGameResult != null) {
			return finishedGameResult;
		}
		else {
			throw new IllegalStateException("Game with id "+gameId+" doesn't exist");
		}
	}

	@Override
	public void makeMove(final long gameId, final String player, final int move) {
		Game game = games.get(gameId);
		
		if(game == null) {
			throw new IllegalArgumentException("Game with id "+gameId+" doesn't exist");
		}
		
		game.makeMove(player, move);
		
		if(!game.isActiveGame()) {
			games.remove(gameId);
			finishedGames.put(gameId, game.displayBoard());
		}
	}

	@Override
	public int activeGameCount() {
		return games.size();
	}

	@Override
	public int finishedGameCount() {
		return finishedGames.size();
	}

	@Override
	public void clearActiveGames() {
		games.clear();
	}

	@Override
	public void clearFinishedGames() {
		finishedGames.clear();
	}
}
