package com.takeaway.game.component;

import com.takeaway.game.handler.ConnectionHandler;
import com.takeaway.game.handler.GameHandler;
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

    public Game() {
        id = new Random().nextInt(12) + 1;
        gameMode = GameMode.AUTOMATIC;
    }

    private Integer id;

    private GameMode gameMode;

    private Connection connection;

    public Connection init() {
        Connection connection = connectionHandler.handle(String.valueOf(id));
        if(connection.isConnected()) {
            setConnection(connection);
            return connection;
        }
        return init();
    }

    public void close() {
        SpringApplication.exit(ctx, () -> 0);
        System.exit(0);
    }
}
