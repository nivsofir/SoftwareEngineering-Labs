package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.GameMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.Random;

public class SimpleServer extends AbstractServer {

	private ConnectionToClient player1;
	private ConnectionToClient player2;

	private String player1Symbol;
	private String player2Symbol;

	private ConnectionToClient currentPlayer;

	private char[][] board = new char[3][3];
	private boolean gameStarted = false;

	public SimpleServer(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (!(msg instanceof GameMessage)) {
			return;
		}

		GameMessage message = (GameMessage) msg;

		if (message.getType().equals("JOIN")) {
			handleJoin(client);
		} else if (message.getType().equals("MOVE")) {
			handleMove(message, client);
		} else if (message.getType().equals("NEW_GAME")) {
			if (player1 != null && player2 != null) {
				startGame();
			}
		}
	}

	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		try {
			ConnectionToClient remainingPlayer = null;

			if (client == player1) {
				remainingPlayer = player2;
				player1 = remainingPlayer;
				player2 = null;
			} else if (client == player2) {
				remainingPlayer = player1;
				player2 = null;
			} else {
				return;
			}

			resetBoardOnly();

			if (remainingPlayer != null) {
				remainingPlayer.sendToClient(
						new GameMessage("OPPONENT_LEFT",
								"Your opponent disconnected. Waiting for new player...")
				);
			}

			System.out.println("A player disconnected. Waiting for a new player.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleJoin(ConnectionToClient client) {
		try {
			if (player1 == null) {
				player1 = client;
				client.sendToClient(new GameMessage("WAIT", "Waiting for second player..."));
				System.out.println("Player 1 connected");
				return;
			}

			if (player2 == null && client != player1) {
				player2 = client;
				System.out.println("Player 2 connected");
				startGame();
				return;
			}

			client.sendToClient(new GameMessage("FULL", "Game already has two players."));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startGame() {
		try {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					board[i][j] = ' ';
				}
			}

			gameStarted = true;

			Random random = new Random();

			if (random.nextBoolean()) {
				player1Symbol = "X";
				player2Symbol = "O";
			} else {
				player1Symbol = "O";
				player2Symbol = "X";
			}

			currentPlayer = random.nextBoolean() ? player1 : player2;

			player1.sendToClient(new GameMessage(
					"START",
					-1,
					-1,
					player1Symbol,
					currentPlayer == player1 ? "Your turn" : "Opponent's turn"
			));

			player2.sendToClient(new GameMessage(
					"START",
					-1,
					-1,
					player2Symbol,
					currentPlayer == player2 ? "Your turn" : "Opponent's turn"
			));

			System.out.println("Game started");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMove(GameMessage message, ConnectionToClient client) {
		try {
			if (!gameStarted) {
				client.sendToClient(new GameMessage("INVALID_MOVE", "Game has not started yet"));
				return;
			}

			if (client != currentPlayer) {
				client.sendToClient(new GameMessage("NOT_YOUR_TURN", "It is not your turn"));
				return;
			}

			int row = message.getRow();
			int col = message.getCol();

			if (row < 0 || row > 2 || col < 0 || col > 2) {
				client.sendToClient(new GameMessage("INVALID_MOVE", "Invalid cell"));
				return;
			}

			if (board[row][col] != ' ') {
				client.sendToClient(new GameMessage("INVALID_MOVE", "Cell already taken"));
				return;
			}

			String symbol = (client == player1) ? player1Symbol : player2Symbol;
			board[row][col] = symbol.charAt(0);

			player1.sendToClient(new GameMessage("BOARD_UPDATE", row, col, symbol, "Board updated"));
			player2.sendToClient(new GameMessage("BOARD_UPDATE", row, col, symbol, "Board updated"));

			if (checkWin(symbol.charAt(0))) {
				client.sendToClient(new GameMessage("WIN", "You win!"));
				ConnectionToClient otherPlayer = (client == player1) ? player2 : player1;
				otherPlayer.sendToClient(new GameMessage("LOSE", "You lose!"));
				gameStarted = false;
				return;
			}

			if (isBoardFull()) {
				player1.sendToClient(new GameMessage("DRAW", "Draw!"));
				player2.sendToClient(new GameMessage("DRAW", "Draw!"));
				gameStarted = false;
				return;
			}

			currentPlayer = (currentPlayer == player1) ? player2 : player1;

			currentPlayer.sendToClient(new GameMessage("YOUR_TURN", "Your turn"));
			ConnectionToClient otherPlayer = (currentPlayer == player1) ? player2 : player1;
			otherPlayer.sendToClient(new GameMessage("WAIT_TURN", "Opponent's turn"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkWin(char symbol) {
		for (int i = 0; i < 3; i++) {
			if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true;
			if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true;
		}

		if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) return true;

		return board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol;
	}

	private boolean isBoardFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == ' ') return false;
			}
		}
		return true;
	}

	private void resetBoardOnly() {
		player1Symbol = null;
		player2Symbol = null;
		currentPlayer = null;
		gameStarted = false;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = ' ';
			}
		}
	}
}