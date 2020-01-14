package com.umurcanemre.mancala.serviceimpl;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import service.GameService;
import service.LobbyService;

@Service
@AllArgsConstructor
public final class LobbyServiceImpl implements LobbyService {
	private static final ConcurrentLinkedQueue<Map.Entry<Long, String>> lobby = new ConcurrentLinkedQueue<>();

	@Autowired
	private GameService gameService;

	/**
	 * Returns game id 
	 * If opponent found, initiates game
	 * 
	 * @param applyer id
	 * @return id of game
	 */
	@Override
	public Long apply(final String applyer) {
		Map.Entry<Long, String> lobbyGame = null;
		boolean insertFlag = false;

		synchronized (this) {
			insertFlag = lobby.size() > 0 && lobby.peek().getValue().equals(applyer);
			if (!insertFlag)
				lobbyGame = lobby.poll();
		}

		if (lobbyGame != null && !insertFlag) {
			gameService.initiateGame(lobbyGame.getKey(), lobbyGame.getValue(), applyer);
			return lobbyGame.getKey();
		} else {
			long gameIdCandidate = System.nanoTime();
			lobby.add(new SimpleEntry<Long, String>(gameIdCandidate, applyer));
			return gameIdCandidate;
		}
	}

	@Override
	public void clearLobby() {
		lobby.clear();
	}

	@Override
	public int getLobbyCount() {
		return lobby.size();
	}
}
