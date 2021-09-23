package com.takeaway.game.component;

import com.takeaway.game.handler.ConnectionHandler;
import com.takeaway.game.model.Connection;
import com.takeaway.game.type.GameEvents;
import com.takeaway.game.type.GameMode;
import com.takeaway.game.type.GameStates;
import com.takeaway.game.util.ConsoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class GameEngine {

    @Autowired
    private ConnectionHandler connectionHandler;

    private Enum<GameMode> gameMode;

    private Connection connection;

    public void welcome(StateContext<GameStates, GameEvents> ctx) {
        ConsoleUtils.clearConsole();

        System.out.println("");
        System.out.println("████████╗██╗  ██╗██████╗ ███████╗███████╗         ");
        System.out.println("╚══██╔══╝██║  ██║██╔══██╗██╔════╝██╔════╝         ");
        System.out.println("   ██║   ███████║██████╔╝█████╗  █████╗           ");
        System.out.println("   ██║   ██╔══██║██╔══██╗██╔══╝  ██╔══╝           ");
        System.out.println("   ██║   ██║  ██║██║  ██║███████╗███████╗██╗██╗██╗");
        System.out.println("   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝╚═╝╚═╝");

        System.out.println("\nWelcome to the game of three, a mini console multiplayer game. The Rules are simple: ");
        System.out.println("\n1. The first player selects a whole random number, and sends it to the other player. ");
        System.out.println("2. The receiving player can now always choose between adding one of {-1, 0, 1} to get to a number that is divisible by 3. ");
        System.out.println("3. The number is then divided by three and the resulting whole number is then sent back to the original sender.");
        System.out.println("4. The same rules are applied until one player reaches 1 after the division.\n");

        ConsoleUtils.pressEnterKeyToContinue();
        ConsoleUtils.clearConsole();

        ctx.getStateMachine().sendEvent(GameEvents.CONFIGURE);
    }

    public void pickGameMode(StateContext<GameStates, GameEvents> ctx) {
        System.out.println("\nThe game provides two gaming modes {Automatic, Manual}: ");
        System.out.println("\n1. Manual mode: the player can adjust the input manually, by adding {-1, 0, 1}.");
        System.out.println("2. Automatic mode: the added value is randomly selected.  ");

        String option = ConsoleUtils.scan("\nDo you want to select automatic mode ? [A/m]");

        gameMode = option.equalsIgnoreCase("a") ? GameMode.AUTOMATIC : GameMode.MANUAL;
        ctx.getStateMachine().sendEvent(GameEvents.CONNECT);

        ConsoleUtils.clearConsole();
    }

    public void findOpponent(StateContext<GameStates, GameEvents> ctx) {
        System.out.println("\nFinding opponent! Please wait...");

        connection = connectionHandler.handle();

        if(connection.isConnected()) {
            ctx.getStateMachine().sendEvent(GameEvents.CONNECT);

            ConsoleUtils.clearConsole();
        }
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
