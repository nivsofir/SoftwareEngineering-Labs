package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.GameMessage;

public class StatusEvent {
    private final GameMessage message;

    public StatusEvent(GameMessage message) {
        this.message = message;
    }

    public GameMessage getMessage() {
        return message;
    }
}