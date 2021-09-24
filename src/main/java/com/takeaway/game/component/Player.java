package com.takeaway.game.component;

import com.takeaway.game.handler.AutomaticGameHandler;
import com.takeaway.game.handler.ConnectionHandler;
import com.takeaway.game.handler.GameHandler;
import com.takeaway.game.handler.ManualGameHandler;
import com.takeaway.game.listener.PrivateMessageListener;
import com.takeaway.game.model.Connection;
import com.takeaway.game.model.Message;
import com.takeaway.game.producer.KafkaProducer;
import com.takeaway.game.type.GameMode;
import com.takeaway.game.type.MessageType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Data
@Component
public class Player {

    @Value(value = "${kafka.topic}")
    private String topic;

    @Autowired
    private ConnectionHandler connectionHandler;

    @Autowired
    private KafkaProducer producer;

    @Autowired
    private PrivateMessageListener listener;

    private GameHandler handler;

    private Integer id;

    private GameMode gameMode;

    private int value;

    private Connection connection;

    public Player() {
        id = new Random().nextInt(12) + 1;
        reset();
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
        String me = String.valueOf(id);
        String opponent = connection.getOpponent(me);

        value = handler.init();

        producer.send(topic, Integer.parseInt(opponent), me, Message.builder()
                .content(value)
                .sender(me)
                .receiver(opponent)
                .type(MessageType.IN_GAME)
                .build());
    }

    public void play() {
        String me = String.valueOf(id);
        String opponent = connection.getOpponent(me);

        value = handler.next(value);

        producer.send(topic, Integer.parseInt(opponent), me, Message.builder()
                .content(value)
                .sender(me)
                .receiver(opponent)
                .type(MessageType.IN_GAME)
                .build());
    }

    public void waitOpponent() {
        try {
            listener.start();
            listener.setMessageType(MessageType.IN_GAME);
            listener.getLatch().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        value = listener.getPayload().getContent();
    }

    public void reset() {
        gameMode = GameMode.AUTOMATIC;
        handler = new AutomaticGameHandler();
        value = -1;
        connection = null;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        handler = gameMode.equals(GameMode.AUTOMATIC)
                ? new AutomaticGameHandler()
                : new ManualGameHandler();

    }

}
