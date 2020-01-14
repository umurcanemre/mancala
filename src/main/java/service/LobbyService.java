package service;

public interface LobbyService {

	Long apply(String applyer);
	void clearLobby();
	int getLobbyCount();
}
