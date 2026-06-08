package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.GameMessage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof GameMessage) {
			GameMessage message = (GameMessage) msg;

			switch (message.getType()) {
				case "WAIT":
				case "START":
				case "YOUR_TURN":
				case "WAIT_TURN":
				case "WIN":
				case "LOSE":
				case "DRAW":
				case "INVALID_MOVE":
				case "NOT_YOUR_TURN":
				case "FULL":
				case "OPPONENT_LEFT":
					EventBus.getDefault().post(new StatusEvent(message));
					break;

				case "BOARD_UPDATE":
					EventBus.getDefault().post(new BoardUpdateEvent(message));
					break;
			}
		}
	}

	public static SimpleClient getClient() {
		return client;
	}

	public static void createClient(String host, int port) {
		client = new SimpleClient(host, port);
	}

	public static boolean hasClient() {
		return client != null;
	}

	public void connect() throws IOException {
		openConnection();
	}

	public void disconnect() {
		try {
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}