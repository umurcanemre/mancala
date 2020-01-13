package com.umurcanemre.mancala.serviceimpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.umurcanemre.mancala.entity.Game;

import service.GameService;

@Service
public final class GameServiceImpl implements GameService {
	private static final Map<Long,Game> games = new ConcurrentHashMap<>();

	@Override
	public boolean isGameStarted(long gameId) {
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
		
		if(game == null) {
			throw new IllegalArgumentException("Game with id "+gameId+" doesn't exist");
		}
		
		return game.displayBoard();
	}

	@Override
	public void makeMove(long gameId, String player, int move) {
		Game game = games.get(gameId);
		
		if(game == null) {
			throw new IllegalArgumentException("Game with id "+gameId+" doesn't exist");
		}
		
		game.makeMove(player, move);
	}
	
	
}
