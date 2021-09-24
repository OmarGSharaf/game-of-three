package com.takeaway.game.component;

import com.takeaway.game.handler.AutomaticGameHandler;
import com.takeaway.game.handler.ManualGameHandler;
import com.takeaway.game.type.GameEvents;
import com.takeaway.game.type.GameStates;
import com.takeaway.game.util.ConsoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class GameEngine {

    @Autowired
    private Player player;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

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

        player.setHandler(option.equalsIgnoreCase("a") ? new AutomaticGameHandler() : new ManualGameHandler());
        ctx.getStateMachine().sendEvent(GameEvents.CONNECT);

        ConsoleUtils.clearConsole();
    }

    public void findOpponent(StateContext<GameStates, GameEvents> ctx) {
        System.out.println("\nFinding opponent! Please wait...\n");

        player.init();

        if (player.getConnectionManager().isPLayer1()) {
            ConsoleUtils.clearConsole();

            player.start();
            ctx.getStateMachine().sendEvent(GameEvents.PLAY);
        } else {
            ctx.getStateMachine().sendEvent(GameEvents.WAIT);
        }

        ConsoleUtils.clearConsole();
    }

    public void play(StateContext<GameStates, GameEvents> ctx) {
        player.play();
        System.out.println(player.getValue());

        ctx.getStateMachine().sendEvent(player.getValue() == 1
                ? GameEvents.ANNOUNCE_WINNER
                : GameEvents.WAIT);
    }

    public void waitTurn(StateContext<GameStates, GameEvents> ctx) {
        player.waitOpponent();

        ctx.getStateMachine().sendEvent(player.getValue() == 1
                ? GameEvents.ANNOUNCE_WINNER
                : GameEvents.PLAY);
    }

    public void gameOver(StateContext<GameStates, GameEvents> ctx) {
        System.out.println("\n...GAME OVER...\n");
        String option = ConsoleUtils.scan("\nDo you want to play again ? [Y/m]");

        player.reset();

        ctx.getStateMachine().sendEvent(option.equalsIgnoreCase("y")
                ? GameEvents.PLAY_AGAIN
                : GameEvents.TERMINATE);

        ConsoleUtils.clearConsole();
    }

    public void exit() {
        System.out.println("Bye bye!!!");
        SpringApplication.exit(applicationContext, () -> 0);
        System.exit(0);
    }
}
