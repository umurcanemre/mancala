package com.umurcanemre.mancala.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umurcanemre.mancala.service.GameService;

@RestController
@RequestMapping(value = "game/")
public class GameController {
	@Autowired
	GameService gameService;
	
	@GetMapping(value = "check/{gameId}")
	public String checkGameStatus(@PathVariable long gameId) {
		return gameService.isGameStarted(gameId) ? "Started" : "Waiting for opponent";
	}
	
	@GetMapping
	public String trysmt() {
		return "Jenkins with GH hook try";
	}
	
	@GetMapping(value = "display/{gameId}")
	public String displayGame(@PathVariable long gameId) {
		return gameService.displayGame(gameId);
	}
	

	//This normally would have been a POST but for testing easily through browser, went with GET method
	@GetMapping(value = "makemove/{gameId}/{player}/{move}")
	public String makeMove(@PathVariable long gameId, @PathVariable String player, @PathVariable int move) {
		gameService.makeMove(gameId, player, move);
		return gameService.displayGame(gameId);
	}
	
}
