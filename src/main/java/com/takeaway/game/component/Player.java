package com.takeaway.game.component;

import com.takeaway.game.handler.AutomaticGameHandler;
import com.takeaway.game.handler.GameHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Data
@Component
public class Player {

    @Autowired
    private ConnectionManager connectionManager;

    private GameHandler handler;

    private Integer id;

    private int value;

    public Player() {
        id = new Random().nextInt(12) + 1;
        reset();
    }

    public boolean init() {
        return connectionManager.connect(String.valueOf(id));
    }

    public void start() {
        value = handler.init();
        connectionManager.send(value);
    }

    public void play() {
        value = handler.next(value);
        connectionManager.send(value);
    }

    public void waitOpponent() {
        value = connectionManager.receive();
    }

    public void reset() {
        handler = new AutomaticGameHandler();
        value = -1;
    }

    public void setHandler(GameHandler handler) {
        this.handler = handler;
    }
}
