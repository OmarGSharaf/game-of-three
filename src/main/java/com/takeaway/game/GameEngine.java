package com.takeaway.game;

import com.takeaway.game.type.GameEvents;
import com.takeaway.game.type.GameStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class GameEngine implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private StateMachine<GameStates, GameEvents> stateMachine;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        stateMachine.start();
    }

    public void welcome() {
        System.out.println("Welcome");
    }

    public void pickGameMode() {
        System.out.println("Pick Game Mode");
    }

    public void findOpponent() {
        System.out.println("Find Opponent");
    }

    public void retryFindOpponent() {
        System.out.println("Retry Find Opponent");
    }

    public void play() {
        System.out.println("Play");
    }

    public void waitTurn() {
        System.out.println("Wait Turn");
    }

    public void gameOver() {
        System.out.println("Game Over");
    }

    public void playAgain() {
        System.out.println("Play Again");
    }

    public void exit() {
        System.out.println("Exit");
    }
}
