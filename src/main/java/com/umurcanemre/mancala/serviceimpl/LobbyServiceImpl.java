package com.umurcanemre.mancala.serviceimpl;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.GameService;
import service.LobbyService;

@Service
public final class LobbyServiceImpl implements LobbyService {
	private static final ConcurrentNavigableMap<Long, String> lobby = new ConcurrentSkipListMap<>();

	@Autowired
	GameService gameService;
	/**
	 * Returns game id
	 * If opponent found, initiates game
	 * @param applyer id
	 * @return id of game
	 */	
	@Override
	public Long apply(final String applyer) {		
		Map.Entry<Long, String> lobbyGame = lobby.pollFirstEntry();
		
		if(lobbyGame != null && !lobbyGame.getValue().equals(applyer)) {
			gameService.initiateGame(lobbyGame.getKey(), lobbyGame.getValue(), applyer);
			return lobbyGame.getKey();
		}
		else {
			long gameIdCandidate = System.nanoTime();
			lobby.put(gameIdCandidate, applyer);
			return gameIdCandidate;
		}
	}
}
