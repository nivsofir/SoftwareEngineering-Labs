package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.GameMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.io.IOException;

public class PrimaryController {

	@FXML private Label statusLabel;

	@FXML private Button btn00;
	@FXML private Button btn01;
	@FXML private Button btn02;
	@FXML private Button btn10;
	@FXML private Button btn11;
	@FXML private Button btn12;
	@FXML private Button btn20;
	@FXML private Button btn21;
	@FXML private Button btn22;

	private String mySymbol = "";
	private boolean gameOver = false;

	@FXML
	void initialize() {
		EventBus.getDefault().register(this);
		setButtonActions();
		disableBoard(false);

		try {
			if (SimpleClient.hasClient()) {
				SimpleClient.getClient().sendToServer(new GameMessage("JOIN"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void newGame(ActionEvent event) {
		try {
			SimpleClient.getClient().sendToServer(new GameMessage("NEW_GAME"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setButtonActions() {
		btn00.setOnAction(e -> sendMove(0, 0));
		btn01.setOnAction(e -> sendMove(0, 1));
		btn02.setOnAction(e -> sendMove(0, 2));
		btn10.setOnAction(e -> sendMove(1, 0));
		btn11.setOnAction(e -> sendMove(1, 1));
		btn12.setOnAction(e -> sendMove(1, 2));
		btn20.setOnAction(e -> sendMove(2, 0));
		btn21.setOnAction(e -> sendMove(2, 1));
		btn22.setOnAction(e -> sendMove(2, 2));
	}

	private void sendMove(int row, int col) {
		if (gameOver) return;

		Button button = getButton(row, col);
		if (button == null || !button.getText().isEmpty()) return;

		try {
			SimpleClient.getClient().sendToServer(new GameMessage("MOVE", row, col));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Subscribe
	public void onBoardUpdate(BoardUpdateEvent event) {
		Platform.runLater(() -> {
			GameMessage message = event.getMessage();
			updateBoard(message.getRow(), message.getCol(), message.getSymbol());
		});
	}

	@Subscribe
	public void onStatus(StatusEvent event) {
		Platform.runLater(() -> {
			GameMessage message = event.getMessage();
			String type = message.getType();

			switch (type) {
				case "WAIT":
					statusLabel.setText(message.getText());
					gameOver = true;
					disableBoard(true);
					break;

				case "START":
					mySymbol = message.getSymbol();
					gameOver = false;
					clearBoard();
					statusLabel.setText("Game started! You are " + mySymbol + ". " + message.getText());
					disableBoard(!"Your turn".equals(message.getText()));
					break;

				case "YOUR_TURN":
					statusLabel.setText(message.getText());
					disableBoard(false);
					break;

				case "WAIT_TURN":
				case "NOT_YOUR_TURN":
				case "INVALID_MOVE":
					statusLabel.setText(message.getText());
					break;

				case "WIN":
				case "LOSE":
				case "DRAW":
				case "FULL":
				case "OPPONENT_LEFT":
					statusLabel.setText(message.getText());
					gameOver = true;
					disableBoard(true);
					break;
			}
		});
	}

	private void updateBoard(int row, int col, String symbol) {
		Button button = getButton(row, col);
		if (button != null) {
			button.setText(symbol);
			button.setDisable(true);
		}
	}

	private Button getButton(int row, int col) {
		if (row == 0 && col == 0) return btn00;
		if (row == 0 && col == 1) return btn01;
		if (row == 0 && col == 2) return btn02;
		if (row == 1 && col == 0) return btn10;
		if (row == 1 && col == 1) return btn11;
		if (row == 1 && col == 2) return btn12;
		if (row == 2 && col == 0) return btn20;
		if (row == 2 && col == 1) return btn21;
		if (row == 2 && col == 2) return btn22;
		return null;
	}

	private void disableBoard(boolean disable) {
		btn00.setDisable(disable || !btn00.getText().isEmpty());
		btn01.setDisable(disable || !btn01.getText().isEmpty());
		btn02.setDisable(disable || !btn02.getText().isEmpty());
		btn10.setDisable(disable || !btn10.getText().isEmpty());
		btn11.setDisable(disable || !btn11.getText().isEmpty());
		btn12.setDisable(disable || !btn12.getText().isEmpty());
		btn20.setDisable(disable || !btn20.getText().isEmpty());
		btn21.setDisable(disable || !btn21.getText().isEmpty());
		btn22.setDisable(disable || !btn22.getText().isEmpty());
	}

	private void clearBoard() {
		btn00.setText("");
		btn01.setText("");
		btn02.setText("");
		btn10.setText("");
		btn11.setText("");
		btn12.setText("");
		btn20.setText("");
		btn21.setText("");
		btn22.setText("");

		btn00.setDisable(false);
		btn01.setDisable(false);
		btn02.setDisable(false);
		btn10.setDisable(false);
		btn11.setDisable(false);
		btn12.setDisable(false);
		btn20.setDisable(false);
		btn21.setDisable(false);
		btn22.setDisable(false);
	}

	@FXML
	void leaveGame(ActionEvent event) {
		if (SimpleClient.hasClient() && SimpleClient.getClient() != null) {
			SimpleClient.getClient().disconnect();
		}
		Platform.exit();
	}
}