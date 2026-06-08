package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.GameMessage;

public class BoardUpdateEvent {
    private final GameMessage message;

    public BoardUpdateEvent(GameMessage message) {
        this.message = message;
    }

    public GameMessage getMessage() {
        return message;
    }
}