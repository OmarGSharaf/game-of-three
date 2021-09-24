package com.takeaway.game.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class Connection {

    private String player1;

    private String player2;

    private Long timestamp;

    public boolean isConnected() {
        return !Objects.isNull(player1) && !Objects.isNull(player2) && !Objects.isNull(timestamp);
    }
}
