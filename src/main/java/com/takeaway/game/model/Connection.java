package com.takeaway.game.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Connection {

    private String player1;

    private String player2;

    private long timestamp;

    public boolean isConnected() {
        return player1.isEmpty() || player2.isEmpty();
    }

    public String getId() {
        return isConnected() ? String.format("%s-%s", player1, player2) : null;
    }

}
