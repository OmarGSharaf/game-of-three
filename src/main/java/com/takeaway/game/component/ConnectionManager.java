package com.takeaway.game.component;

public interface ConnectionManager {

    boolean connect(String me);

    void send(Integer message);

    Integer receive();

    boolean isPLayer1();
}
