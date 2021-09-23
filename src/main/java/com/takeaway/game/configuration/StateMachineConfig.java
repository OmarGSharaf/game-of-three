package com.takeaway.game.configuration;

import com.takeaway.game.component.GameEngine;
import com.takeaway.game.type.GameEvents;
import com.takeaway.game.type.GameStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<GameStates, GameEvents> {

    @Autowired
    private GameEngine gameEngine;

    @Override
    public void configure(StateMachineConfigurationConfigurer<GameStates, GameEvents> config) throws Exception {
        config.withConfiguration().autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<GameStates, GameEvents> states) throws Exception {
        states
                .withStates()
                .initial(GameStates.INIT)
                .state(GameStates.INIT, initAction())
                .state(GameStates.CONFIGURING, configuringAction())
                .state(GameStates.CONNECTING, connectingAction())
                .state(GameStates.PLAYING, playAction())
                .state(GameStates.WAITING, waitAction())
                .state(GameStates.OVER, overAction())
                .state(GameStates.END, endAction());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<GameStates, GameEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(GameStates.INIT).target(GameStates.CONFIGURING).event(GameEvents.CONFIGURE)
                .and().withExternal()
                .source(GameStates.CONFIGURING).target(GameStates.CONNECTING).event(GameEvents.CONNECT)
                .and().withExternal()
                .source(GameStates.CONNECTING).target(GameStates.PLAYING).event(GameEvents.PLAY)
                .and().withExternal()
                .source(GameStates.PLAYING).target(GameStates.WAITING).event(GameEvents.WAIT)
                .and().withExternal()
                .source(GameStates.WAITING).target(GameStates.PLAYING).event(GameEvents.PLAY)
                .and().withExternal()
                .source(GameStates.PLAYING).target(GameStates.OVER).event(GameEvents.ANNOUNCE_WINNER)
                .and().withExternal()
                .source(GameStates.WAITING).target(GameStates.OVER).event(GameEvents.ANNOUNCE_WINNER)
                .and().withExternal()
                .source(GameStates.OVER).target(GameStates.END).event(GameEvents.TERMINATE)
                .and().withExternal()
                .source(GameStates.OVER).target(GameStates.CONFIGURING).event(GameEvents.PLAY_AGAIN);
    }

    @Bean
    public Action<GameStates, GameEvents> initAction() {
        return ctx -> gameEngine.welcome(ctx);
    }

    @Bean
    public Action<GameStates, GameEvents> configuringAction() {
        return ctx -> gameEngine.pickGameMode(ctx);
    }

    @Bean
    public Action<GameStates, GameEvents> connectingAction() {
        return ctx -> gameEngine.findOpponent(ctx);
    }

    @Bean
    public Action<GameStates, GameEvents> playAction() {
        return ctx -> gameEngine.play();
    }

    @Bean
    public Action<GameStates, GameEvents> waitAction() {
        return ctx -> gameEngine.waitTurn();
    }

    @Bean
    public Action<GameStates, GameEvents> overAction() {
        return ctx -> gameEngine.gameOver();
    }

    @Bean
    public Action<GameStates, GameEvents> endAction() {
        return ctx -> gameEngine.playAgain();
    }

}