package com.umurcanemre.mancala.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.LobbyService;

@RestController
@RequestMapping(value = "lobby/")
public class LobbyController {
	@Autowired
	LobbyService lobbyService;
	
	@GetMapping(value = "join/{name}")
	public long joinGame(@PathVariable String name) {
		return lobbyService.apply(name);
	}
	
}
