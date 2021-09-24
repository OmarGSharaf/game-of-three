package com.takeaway.game.component;

import com.takeaway.game.handler.AutomaticGameHandler;
import com.takeaway.game.handler.ConnectionHandler;
import com.takeaway.game.handler.GameHandler;
import com.takeaway.game.handler.ManualGameHandler;
import com.takeaway.game.model.Connection;
import com.takeaway.game.type.GameMode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Random;

@Data
@Component
public class Game {

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Autowired
    private ConnectionHandler connectionHandler;

    private GameHandler handler;

    private Integer id;

    private GameMode gameMode;

    private Connection connection;

    public Game() {
        id = new Random().nextInt(12) + 1;
        gameMode = GameMode.AUTOMATIC;
        handler = new AutomaticGameHandler();
    }

    public Connection init() {
        Connection connection = connectionHandler.handle(String.valueOf(id));
        if (connection.isConnected()) {
            setConnection(connection);
            return connection;
        }
        return init();
    }

    public void start() {

    }

    public void reset() {
        gameMode = GameMode.AUTOMATIC;
        handler = new AutomaticGameHandler();
        connection = null;
    }

    public void close() {
        SpringApplication.exit(ctx, () -> 0);
        System.exit(0);
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        handler = gameMode.equals(GameMode.AUTOMATIC)
                ? new AutomaticGameHandler()
                : new ManualGameHandler();

    }

}
